package com.zdhk.ipc;

import com.alibaba.fastjson.JSON;
import com.zdhk.ipc.entity.IpcEvent;
import com.zdhk.ipc.mapper.IpcEventMapper;
import com.zdhk.ipc.utils.MyDateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 事件检测相机数据插入
 *
 * @auther WangMin
 * @create 2021-01-15 16:57
 */

public class TestDVS extends BaseTest{

    @Resource
    private IpcEventMapper ipcEventMapper;

    @Test
    public void getlist(){
        //读取文件夹下所有文件
        List<String> fileNames = getFiles("D:\\工作文档\\抛洒物\\20201231-最终版-备份\\抛洒物测试");
        System.out.println(JSON.toJSONString(fileNames));

        for (String fileName:fileNames){
            String time = fileName.split("测试")[1].split("\\\\")[1].split("\\.")[0];
            Date date = MyDateUtils.getDateByyyyMMddHHmmssSSS(time);
            System.out.println(time);
            System.out.println(date);

            String picUrl = "/opt/upgrade/video/tmp/"+time+".jpg";
            System.out.println(picUrl);

            IpcEvent event = new IpcEvent();

            event.setEventType(0);
            event.setEventDesc("事件检测相机抛洒物碎片事件上报");
            event.setDetectPicUrl(picUrl);
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




}
