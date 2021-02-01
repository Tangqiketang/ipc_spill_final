package com.zdhk.ipc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.entity.TOrder;
import com.zdhk.ipc.mapper.TOrderMapper;
import com.zdhk.ipc.utils.MyDateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.Date;
import com.zdhk.ipc.annotation.DateValue;

/**
 * 描述:
 *订单分表插入查询测试类
 * @auther WangMin
 * @create 2021-02-01 15:17
 */
@RestController
@RequestMapping("/orderTest")
@Api(tags={"订单分表模块测试"})
@Validated
public class ShardingOrderController {

    @Resource
    private TOrderMapper tOrderMapper;

    @ApiOperation("插入订单")
    @RequestMapping(value = "/insertOrder", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp insertOrder2(){
        BaseResp rsp = new BaseResp();
        TOrder order = new TOrder();

        order.setCreateTime(new Date());
        order.setName("tt");
        tOrderMapper.insert(order);

        return rsp;
    }


    /**
     * 按范围查找
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("查询订单")
    @RequestMapping(value = "/queryOrderList", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间 yyyy-MM-dd HH:mm:ss", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 yyyy-MM-dd HH:mm:ss", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页,最小值为1", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数,pageSize必须在1到100之间", dataType = "int", paramType = "query")
    })
    public BaseResp queryOrderList2(@DateValue(format = "yyyy-MM-dd HH:mm:ss") @RequestParam String startTime,
                                    @DateValue(format = "yyyy-MM-dd HH:mm:ss") @RequestParam String endTime,
                                    @Min(value = 1, message = "最小值为1") @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @Range(min = 1, max = 100, message = "pageSize必须在1到100之间")@RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        BaseResp rsp = new BaseResp();

        Page<TOrder> pageParam = new Page<TOrder>();
        pageParam.setCurrent(pageNo);
        pageParam.setSize(pageSize);

        LambdaQueryWrapper<TOrder> wrapper = new LambdaQueryWrapper<TOrder>();
        wrapper.between(TOrder::getCreateTime, MyDateUtils.getDateByyyyyMMddhhmmss(startTime),MyDateUtils.getDateByyyyyMMddhhmmss(endTime));
        wrapper.orderByDesc(TOrder::getCreateTime);

        Page<TOrder> list = (Page<TOrder>) tOrderMapper.selectPage(pageParam,wrapper);
        rsp.setResult(list);
        return rsp;
    }

}
