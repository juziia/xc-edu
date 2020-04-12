package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component // 交给spring容器管理
public class MediaProcessTask {

    @Value("${xc-service-manage-media.video-location}")
    private String video_location;      // 视频文件存放的根目录

    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpeg_path; // ffmpeg.exe程序路径


    @Autowired
    private MediaFileRepository mediaFileRepository;

    @RabbitListener(queues = {"${xc-service-manage-media.mq.queue-media-video-processor}"},
                    containerFactory="simpleRabbitListenerContainerFactory")
    public void processTask(String msgJson){
        // 判断json是否为空或者null
        if(StringUtils.isBlank(msgJson)){
            return;
        }
        // 转换json为Map
        Map map = JSON.parseObject(msgJson, Map.class);
        // 获取mediaId
        String mediaId = (String) map.get("mediaId");
        // 根据mediaId查询数据库
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        // 判断是否为空
        if(!optional.isPresent()) {
            return;
        }
        MediaFile mediaFile = optional.get();
        // 获取文件类型
        String mediaFileType = mediaFile.getFileType();
        if(!StringUtils.equals("avi",mediaFileType)){
            // 不是avi文件,无需处理 303004
            mediaFile.setProcessStatus("303004");
            // 保存到数据库
            mediaFileRepository.save(mediaFile);
            return;
        }
        // 文件类型是avi类型,转换为mp4类型
        mediaFile.setProcessStatus("303001");   // 修改状态为处理中 303001
        mediaFileRepository.save(mediaFile);

        // 获取视频文件的路径  文件存放的根目录 + 文件存放的相对路径 + 文件名称
        String videoPath = this.video_location + mediaFile.getFilePath() + mediaFile.getFileName();
        // 定义mp4文件目录名称  文件存放的根目录 + 文件存放的相对路径
        String mp4FloderPath = this.video_location + mediaFile.getFilePath();
        String mp4FileName = mediaFile.getFileId() + ".mp4";
        //将其转换为mp4文件  String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,videoPath,mp4FileName,mp4FloderPath);
        // 生成mp4文件, 返回状态信息  success为 处理成功
        String result = mp4VideoUtil.generateMp4();
        if(!StringUtils.equals("success",result)){
            mediaFile.setProcessStatus("303003");   // 处理失败
            MediaFileProcess_m3u8 mediaFileProcessM3u8 = new MediaFileProcess_m3u8();
            mediaFileProcessM3u8.setErrormsg(result);   // 将错误信息保存
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcessM3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //  将处理完成的mp4文件生成hls文件
        // 定义m3u8文件存放路径
        String m3u8FloderPath = this.video_location + mediaFile.getFilePath() + "hls/";
        // 定义m3u8文件名称
        String m3u8FileName = mediaFile.getFileId() + ".m3u8";
        String mp4FilePath = mp4FloderPath + mp4FileName;
        // String ffmpeg_path, String video_path, String m3u8_name,String m3u8folder_path
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path,mp4FilePath,m3u8FileName,m3u8FloderPath);
        // 生成hls文件
        String hlsResult = hlsVideoUtil.generateM3u8();
        MediaFileProcess_m3u8 mediaFileProcessM3u8 = new MediaFileProcess_m3u8();
        if(!StringUtils.equals("success",hlsResult)){
            mediaFileProcessM3u8.setErrormsg(hlsResult);    // 设置错误信息
            mediaFile.setProcessStatus("303003");   // 处理失败
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcessM3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        // 获取ts文件列表
        List<String> tslist = hlsVideoUtil.get_ts_list();
        mediaFileProcessM3u8.setTslist(tslist);
        mediaFile.setProcessStatus("303002");   // 处理成功
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcessM3u8);
        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8FileName;
        mediaFile.setFileUrl(fileUrl);
        // 保存到数据库中
        mediaFileRepository.save(mediaFile);
    }


}
