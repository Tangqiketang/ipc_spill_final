package com.zdhk.ipc;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdhk.ipc.entity.IpcEvent;
import com.zdhk.ipc.mapper.IpcEventMapper;
import com.zdhk.ipc.utils.MyDateUtils;
import com.zdhk.ipc.utils.MyFileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 描述: 事件检测相机数据插入
 *
 * @auther WangMin
 * @create 2021-01-15 16:57
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDect {

    @Autowired
    private IpcEventMapper ipcEventMapper;

    @Test
    public void getlist() throws IOException {
        //读取文件夹下所有文件
        List<String> fileNames = getFiles("D:\\工作文档\\抛洒物\\20201231-最终版-备份\\track\\track");
        System.out.println(JSON.toJSONString(fileNames));

        for (String fileName:fileNames){
            if(!fileName.endsWith(".avi")){
                continue;
            }
            System.out.println(fileName);  //D:\工作文档\抛洒物\20201231-最终版-备份\track\track\0658.avi

            BasicFileAttributes att = Files.readAttributes(Paths.get(fileName), BasicFileAttributes.class);//获取文件的属性
            long lastmdtime = att.lastModifiedTime().toMillis();
            Date date = new Date(lastmdtime);
            System.out.println(date);

            String singName = fileName.split("track\\\\track\\\\")[1].split("\\.")[0]+".mp4";

            String videoUrl = "http://10.0.40.91:12333/ipc_spill/ipc/mp4?path=/opt/upgrade/video/tmp/track/"+"f"+singName;
            System.out.println(videoUrl);


            IpcEvent event = new IpcEvent();

            event.setEventType(1);
            event.setEventDesc("DVS事件抛洒物上报");
            event.setCommonVideoUrl(videoUrl);
            event.setIsHide(0);
            event.setEventTime(date);
            //System.out.println(JSON.toJSONString(event));
            ipcEventMapper.insert(event);


        }


    }


    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
    }

/*
    @Test
    public void getlist() throws IOException {
        //读取文件夹下所有文件
        List<String> fileNames = getFiles("D:\\工作文档\\抛洒物\\20201231-最终版-备份\\track\\track");
        System.out.println(JSON.toJSONString(fileNames));

        File exeFile = new File("D:\\工作文档\\抛洒物\\20201231-最终版-备份\\track\\track\\Myexe.txt");
        FileWriter fw =new FileWriter(exeFile,true) ;
        for (String fileName:fileNames){
            if(!fileName.endsWith(".avi")){
                continue;
            }
            System.out.println(fileName);  //D:\工作文档\抛洒物\20201231-最终版-备份\track\track\0658.avi

            BasicFileAttributes att = Files.readAttributes(Paths.get(fileName), BasicFileAttributes.class);//获取文件的属性
            long lastmdtime = att.lastModifiedTime().toMillis();

            String source = fileName.split("track\\\\track\\\\")[1];
            String singName = fileName.split("track\\\\track\\\\")[1].split("\\.")[0]+".mp4";


            String exeStr = "ffmpeg -i "+ source + " -c:v libx264 -strict -2 " + "f"+singName+";";
            fw.append(exeStr);
            fw.write("\r\n");//换行
            fw.write("sleep 5;");
            fw.write("\r\n");//换行


        }
        fw.close();

    }*/


}
