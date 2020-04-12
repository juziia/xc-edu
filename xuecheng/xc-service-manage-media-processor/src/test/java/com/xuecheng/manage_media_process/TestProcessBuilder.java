package com.xuecheng.manage_media_process;

import com.xuecheng.framework.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {

    @Test
    public void testProcessBuilder() throws IOException {

        //创建ProcessBuilder对象
        ProcessBuilder processBuilder =new ProcessBuilder();
        //设置执行的第三方程序(命令)
//        processBuilder.command("ping","127.0.0.1");
        processBuilder.command("ipconfig");
//        processBuilder.command("java","-jar","f:/xc-service-manage-course.jar");
        //将标准输入流和错误输入流合并，通过标准输入流读取信息就可以拿到第三方程序输出的错误信息、正常信息
        processBuilder.redirectErrorStream(true);

        //启动一个进程
        Process process = processBuilder.start();
        //由于前边将错误和正常信息合并在输入流，只读取输入流
        InputStream inputStream = process.getInputStream();
        //将字节流转成字符流
        InputStreamReader reader = new InputStreamReader(inputStream,"gbk");
       //字符缓冲区
        char[] chars = new char[1024];
        int len = -1;
        while((len = reader.read(chars))!=-1){
            String string = new String(chars,0,len);
            System.out.println(string);
        }

        inputStream.close();
        reader.close();

    }

    //测试使用工具类将avi转成mp4
    @Test
    public void testProcessMp4(){
        //String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path
        //ffmpeg的路径
        String ffmpeg_path = "D:\\Program Files\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe";
        //video_path视频地址
        String video_path = "E:\\ffmpeg_test\\1.avi";
        //mp4_name mp4文件名称
        String mp4_name  ="1.mp4";
        //mp4folder_path mp4文件目录路径
        String mp4folder_path="E:/ffmpeg_test/";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        //开始编码,如果成功返回success，否则返回输出的日志
        String result = mp4VideoUtil.generateMp4();
        System.out.println(result);
    }


    @Test
    public void testProcessBuilder02(){
        // 定义一个进程构造器对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 定义第三方程序的命令
        processBuilder.command("ping","127.0.0.1");
        // 将标出输入流.错误流合并
        processBuilder.redirectErrorStream(true);
        try {
            // 开启进程,执行命令
            Process process = processBuilder.start();
            // 获取输入流
            InputStream inputStream = process.getInputStream();
            // 将输入流转换为字符流
            InputStreamReader reader = new InputStreamReader(inputStream,"gbk");
            // 缓冲区
            char[] cache = new char[1024];
            int len = -1;
            while(-1 != (reader.read(cache))){
                String str = new String(cache);
                System.out.println(str);
            }
            reader.close();
            inputStream.close();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    // 使用ProcessBuilder调用ffmpeg程序
    @Test
    public void testFfmpeg(){
        // 定义进程构建器对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 定义第三方应用程序的命令,使用集合添加命令
        List<String> command = new ArrayList<>();
        command.add("C:\\software\\Ffmpeg-win64\\bin\\ffmpeg.exe");
        command.add("-version");
        // 将命令集合添加至进程构建器对象中
        processBuilder.command(command);
        // 合并标准流,错误流
        processBuilder.redirectErrorStream(true);
        // 开启进程,执行命令
        try {
            Process process = processBuilder.start();
            // 获取输入流
            InputStream inputStream = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream,"gbk");
            // 定义缓冲区
            char[] cache = new char[1024];
            int len = -1;
            while(-1 != (reader.read(cache))){
                String str = new String(cache);
                System.out.println(str);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testRuntime(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("notepad.exe");
        // 开启进程,执行命令
        try {
            Process process = processBuilder.start();
            Thread.sleep(2000);
            process.destroy();  // 关闭进程
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



















}
