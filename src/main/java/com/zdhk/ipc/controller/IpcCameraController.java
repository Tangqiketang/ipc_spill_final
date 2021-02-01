package com.zdhk.ipc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.entity.IpcCamera;
import com.zdhk.ipc.service.IIpcCameraService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wang Min
 * @since 2020-11-12
 */
@Controller
@RequestMapping("/camera")
@Api(value = "相机",tags={"相机相关"})
public class IpcCameraController {

    @Autowired
    private IIpcCameraService ipcCameraService;

    @ApiOperation(value = "新增相机", notes = "相机")
    @PostMapping("/insertCamera")
    @ResponseBody
    public BaseResp insertCamera(@Validated @RequestBody IpcCamera camera ){
        BaseResp rsp = new BaseResp();
        ipcCameraService.save(camera);
        rsp.setCode(0);
        return rsp;
    }


    @ApiOperation(value = "编辑更新相机", notes = "相机")
    @PostMapping("/updateCamera")
    @ResponseBody
    public BaseResp updateCamera(@Validated @RequestBody IpcCamera camera ){
        BaseResp rsp = new BaseResp();
       // ipcCameraService.update(camera,new LambdaUpdateWrapper<>());
        ipcCameraService.updateById(camera);
        rsp.setCode(0);
        return rsp;
    }


    /**
     * 分页列表查询相机
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value="相机分页列表查询", notes="分页列表查询")
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResp<IPage<IpcCamera>> queryPageList(
            @RequestParam(name="cameraName",defaultValue = "")String cameraName,
            @Min(value = 1, message = "最小值为1") @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @Range(min = 1, max = 100, message = "pageSize必须在1到100之间")@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        BaseResp rsp= new BaseResp<>();
        Page<IpcCamera> page =new Page<>(pageNo,pageSize);
        LambdaQueryWrapper<IpcCamera> queryWrapper =new LambdaQueryWrapper<IpcCamera>();
        if(StringUtils.isNotBlank(cameraName)){
            queryWrapper.like(IpcCamera::getCameraName,cameraName);
        }

        IPage<IpcCamera> pageList = ipcCameraService.page(page,queryWrapper);
        rsp.setCode(0);
        rsp.setResult(pageList);
        return rsp;
    }


    @ApiOperation(value="删除相机",notes = "删除相机")
    @GetMapping(value = "/deleteById")
    @ResponseBody
    public BaseResp deleteCameraById(
            @RequestParam(name="id",required = true)Integer id
    ){
        BaseResp rsp = new BaseResp();
        ipcCameraService.removeById(id);
        rsp.setCode(0);
        return rsp;
    }

    @RequestMapping("/testVideoUrl")
    public String testVideoUrl(){
        return "video";
    }
}

