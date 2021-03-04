package com.zdhk.ipc;

import com.alibaba.fastjson.JSON;
import com.zdhk.ipc.entity.IpcEvent;
import com.zdhk.ipc.mapper.IpcEventMapper;
import com.zdhk.ipc.protocal.elasticsearch.dao.HouseRepository;
import com.zdhk.ipc.protocal.elasticsearch.entity.HouseIndexTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 事件检测相机数据插入
 *
 * @auther WangMin
 * @create 2021-01-15 16:57
 */


public class TestElastic extends BaseTest{
    @Autowired
    private HouseRepository houseRepository;

    @Test
    public void saveUser(){
        HouseIndexTemplate template = new HouseIndexTemplate();
        template.setHouseId(1L);
        template.setName("Tom");
        houseRepository.save(template);
    }


}
