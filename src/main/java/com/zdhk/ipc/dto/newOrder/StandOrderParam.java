package com.zdhk.ipc.dto.newOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 描述:
 * 标准订单提交参数
 *
 * @auther WangMin
 * @create 2021-07-14 18:02
 */
@Data
public class StandOrderParam {

    @ApiModelProperty(value = "产品信息")
    private List<PuPackageVxVOExt> puPackageVxVOExtList;

    @ApiModelProperty(value = "优惠卷金额")
    private BigDecimal couponPrice;

    @NotBlank
    @ApiModelProperty(value = "门店唯一标识",required = true)
    private String storeKey;


    @NotNull
    @ApiModelProperty(value = "用户服务地址id")
    private Integer addressId;


    @NotNull
    @ApiModelProperty(value = "服务方式0到店1上门")
    private Integer serviceMode;


    @ApiModelProperty(value = "订单编号",hidden = true)
    private String orderNo;

    @ApiModelProperty(value = "用户唯一标识",hidden = true)
    private String userKey;



}
