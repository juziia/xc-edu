package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

//
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestChunkFile {


    @Test
    public void testUpload() throws Exception {
        // 定义源文件
        File file = new File("C:\\workdir\\javascript\\video\\hls\\lucene.avi");
        // 定义每块的大小
        long chunkSize = 1 * 1024 * 1024;

        // 获取文件的块数
        long fileSize = file.length();
        long fileChunkNum = fileSize % chunkSize == 0 ? fileSize/chunkSize : (fileSize/chunkSize) +1;

        // 定义文件存放的目录
        String targetFileFloder = "C:\\workdir\\javascript\\video\\chunk\\";
        // 创建随机流对象
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
        // 定义缓冲区
        byte[] cacheBytes = new byte[1024];
        int len = -1;
        for (int i = 0; i < fileChunkNum; i++) {
            // 定义块文件名称
            File chunkFile = new File(targetFileFloder+i);
            RandomAccessFile writerRandomAccessFile = new RandomAccessFile(chunkFile,"rw");
            while((len = randomAccessFile.read(cacheBytes))!= -1){
                writerRandomAccessFile.write(cacheBytes,0,len); // 将字节输出
                // 判断文件大小是否等于块文件大小
                if(chunkFile.length() >= chunkSize) {
                    writerRandomAccessFile.close();
                    break;
                }
            }
            System.out.println("----------------------------");
            System.out.println("第"+i+"块文件已输出完成");
            System.out.println("大小为:"+chunkFile.length());


        }
        randomAccessFile.close();


    }

    @Test
    public void test() throws IOException {
        String chunkFileFloderPath = "C:\\workdir\\javascript\\video\\chunk";
        File chunkFileFloader = new File(chunkFileFloderPath);
        File[] files = chunkFileFloader.listFiles();
        List<File> fileList = Arrays.asList(files);
        fileList.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;
            }
        });

        // 定义合并文件名称
        File file2 = new File("C:\\workdir\\javascript\\video\\hls\\lucene_test001.avi");
        RandomAccessFile writer = new RandomAccessFile(file2,"rw");
        // 缓冲区
        byte[] cache = new byte[1024];
        int len = -1;
        // 读取分块目录下的文件
        for (File file : fileList) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            while ((len = randomAccessFile.read(cache)) != -1) {
                writer.write(cache);
            }
            randomAccessFile.close();
        }
        writer.close();
    }






}
