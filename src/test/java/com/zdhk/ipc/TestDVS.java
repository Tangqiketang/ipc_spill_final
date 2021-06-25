package com.zdhk.ipc;

import com.alibaba.fastjson.JSON;
import com.zdhk.ipc.entity.IpcEvent;
import com.zdhk.ipc.mapper.IpcEventMapper;
import com.zdhk.ipc.utils.MyDateUtils;
import org.junit.Test;

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

//   @Test
//    public void createAdcode(){
//        String url = "https://webapi.amap.com/ui/1.1/ui/geo/DistrictExplorer/assets/d_v1/country_tree.json";
//        String result = restTemplate.getForEntity(url,String.class).getBody();
//        JSONObject data = JSONObject.parseObject(result);
//
//        SysAdcode adcode = new SysAdcode();
//        adcode.setParent("");
//        adcode.setAdcode(data.getString("adcode"));
//        adcode.setLevel(data.getString("level"));
//        adcode.setName(data.getString("name"));
//        adcode.insert();
//
//        JSONArray proviceArr = data.getJSONArray("children");
//        for(int i=0;i<proviceArr.size();i++){
//            JSONObject provice = proviceArr.getJSONObject(i);
//            SysAdcode adcode1 = new SysAdcode();
//            adcode1.setParent(adcode.getAdcode());
//            adcode1.setAdcode(provice.getString("adcode"));
//            adcode1.setLevel(provice.getString("level"));
//            adcode1.setName(provice.getString("name"));
//            adcode1.insert();
//
//            if(provice.containsKey("children")){
//                JSONArray cityArr = provice.getJSONArray("children");
//                for(int j=0;j<cityArr.size();j++){
//                    JSONObject city = cityArr.getJSONObject(j);
//                    SysAdcode adcode2 = new SysAdcode();
//                    adcode2.setParent(adcode1.getAdcode());
//                    adcode2.setAdcode(city.getString("adcode"));
//                    adcode2.setLevel(city.getString("level"));
//                    adcode2.setName(city.getString("name"));
//                    adcode2.insert();
//
//                    if(city.containsKey("children")){
//                        JSONArray districtArr = city.getJSONArray("children");
//                        for(int k=0;k<districtArr.size();k++){
//                            JSONObject district = districtArr.getJSONObject(k);
//                            SysAdcode adcode3 = new SysAdcode();
//                            adcode3.setParent(adcode2.getAdcode());
//                            adcode3.setAdcode(district.getString("adcode"));
//                            adcode3.setLevel(district.getString("level"));
//                            adcode3.setName(district.getString("name"));
//                            adcode3.insert();
//                        }
//                    }
//                }
//            }
//        }


}
