package com.xuecheng.manage_media.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.MD5Util;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.*;
import java.util.*;

@Service
public class MediaUploadServiceImpl implements MediaUploadService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Value("${xc-service-manage-media.upload-location}")
    private String uploadLocation;

    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    private String routingkey_media_video;      // 视频处理队列的路由key

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 根据md5获取文件路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        // 查询是否存在
        boolean exists = file.exists();
        MediaFile mediaFile = this.findById(fileMd5);
        if(mediaFile == null && exists){
            CastException.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }

        // 文件不存在,创建目录
        String chunkFloderPath = this.getFileFloderPath(fileMd5);
        File chunkFloder = new File(chunkFloderPath);
        if(!chunkFloder.exists()){
            // 创建目录
            chunkFloder.mkdirs();
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Long chunkSize) {
        // 获取块文件目录
        String chunkFileFloderPath = this.getChunkFileFloderPath(fileMd5);
        // 定义chunk块文件对象  块文件目录: 文件存放路径 + fileMd5的第一位字符 + fileMd5的第二位字符 + fileMd5 + 'chunk' + 块文件下标
        String chunkFilePath = chunkFileFloderPath +chunk;
        File file = new File(chunkFilePath);
        // 文件是否存在
        boolean exists = file.exists();

        if(exists){
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,true);
        }else{
            return new CheckChunkResult(CommonCode.SUCCESS,false);
        }
    }

    @Override
    public ResponseResult uploadChunk(MultipartFile file, Integer chunk, String fileMd5) {
        // 获取块文件目录
        String chunkFileFloderPath = this.getChunkFileFloderPath(fileMd5);
        // 定义块文件目录文件对象
        File chunkFileFloder = new File(chunkFileFloderPath);
        // 判断目录是否存在
        if(!chunkFileFloder.exists()){
            // 不存在,创建目录
            chunkFileFloder.mkdirs();
        }

        // 定义块文件路径, 块文件目录 + 块文件下标
        String chunkFilePath = chunkFileFloderPath + chunk;
        InputStream input = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 获取块文件内容
            input = file.getInputStream();
            fileOutputStream = new FileOutputStream(chunkFilePath);
            // 写出文件内容
            IOUtils.copy(input,fileOutputStream);

            return new ResponseResult(CommonCode.SUCCESS);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!= null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new ResponseResult(MediaCode.MEDIA_CHUNK_UPLOAD_ERROR);
    }

    @Override
    public ResponseResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 获取块文件目录
        String chunkFileFloderPath = this.getChunkFileFloderPath(fileMd5);
        // 定义块文件目录对象
        File chunkFileFloder = new File(chunkFileFloderPath);
        // 获取文件列表
        File[] files = chunkFileFloder.listFiles();
        // 将文件合并
        File mergeFile = this.mergeChunksFile(files, fileMd5, fileExt);
        if(mergeFile == null){
            CastException.cast(MediaCode.MERGE_FILE_ERROR);
        }
        // 合并文件成功,向Mongodb中存储数据
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);                   // 文件唯一标识md5值
        mediaFile.setFileOriginalName(fileName);        // 原始文件名称
        mediaFile.setFileName(fileMd5+"."+fileExt);     // 文件名称
        mediaFile.setFileSize(fileSize);                // 文件大小
        mediaFile.setMimeType(mimetype);                // mime类型
        mediaFile.setUploadTime(new Date());             // 上传时间
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5)); // 设置文件目录相对路径
        mediaFile.setFileType(fileExt);                 // 扩展名
        // 校验文件的md5
        boolean result = this.checkFileMd5(mergeFile,fileMd5);
        if(!result){
            mediaFile.setFileStatus("301003");  // 上传失败状态码
            mediaFileRepository.save(mediaFile);
            CastException.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }else{
            mediaFile.setFileStatus("301002");  // 上传成功状态码
            mediaFileRepository.save(mediaFile);
            this.sendMediaProcessTaskMsg(mediaFile.getFileId());      // 发送消息给mq
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    public ResponseResult sendMediaProcessTaskMsg(String mediaId){
        // 判断id是否为null
        if(StringUtils.isBlank(mediaId)){
            CastException.cast(CommonCode.PARAMS_ILLEGAL);
        }
        // 根据id查询mediaFile
        MediaFile mediaFile = this.findById(mediaId);
        if(mediaFile == null){
            CastException.cast(CommonCode.FAIL);
        }
        // 将mediaId存入Map,再转换为json
        Map<String,String> map = new HashMap<>();
        map.put("mediaId",mediaId);
        String msgJson = JSON.toJSONString(map);
        try {
            // 发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, msgJson);
        }catch (Exception e){
            CastException.cast(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }



    private boolean checkFileMd5(File mergeFile, String fileMd5) {
        try {
            String fileMD5Str = MD5Util.getFileMD5String(mergeFile);
            if(fileMd5.equalsIgnoreCase(fileMD5Str)){
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private File mergeChunksFile(File[] files, String fileMd5,String fileExt) {
        // 将文件转换为list集合,然后根据名称进行排序
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;
            }
        });
        // 根据fileMd5获取合并文件路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        File mergeFile = new File(filePath);

        if(mergeFile.exists()){
            mergeFile.delete();
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(mergeFile,"rw");
            // 循环读取所有块文件内容,进行合并
            byte[] cache = new byte[1024];
            int len = -1;
            for (File file : fileList) {
                FileInputStream input = new FileInputStream(file);
                while((len=input.read(cache))!= -1){
                    randomAccessFile.write(cache,0,len);
                }
                input.close();
            }
            return mergeFile;

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }







    public MediaFile findById(String id){
        Optional<MediaFile> optional = mediaFileRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    // 获取块文件存放目录
    private String getFileFloderPath(String fileMd5) {

        return this.uploadLocation +fileMd5.charAt(0)+"/"+fileMd5.charAt(1)+"/"+fileMd5;
    }
    // 获取合并文件存放路径
    private String getFilePath(String fileMd5,String fileExt){

        return this.getFileFloderPath(fileMd5)+"/"+fileMd5+"."+fileExt;
    }

    // 获取块文件目录
    private String getChunkFileFloderPath(String fileMd5){
        return this.getFileFloderPath(fileMd5)+"/chunk/";
    }

    private String getFileFolderRelativePath(String fileMd5){
        return fileMd5.charAt(0)+"/"+fileMd5.charAt(1)+"/"+fileMd5+"/";
    }


}
