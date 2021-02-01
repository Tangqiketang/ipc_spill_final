package com.zdhk.ipc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author Wang Min
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysPermission对象", description="菜单权限表")
public class SysPermission extends Model<SysPermission> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "菜单标题")
    private String name;

    @ApiModelProperty(value = "路径")
    private String url;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "组件名字")
    private String componentName;

    @ApiModelProperty(value = "菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)")
    private Integer menuType;

    @ApiModelProperty(value = "菜单排序")
    private Integer sortNo;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "是否叶子节点:    1:是   0:不是")
    private Boolean isLeaf;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除状态 0正常 1已删除")
    private Integer delFlag;

    @ApiModelProperty(value = "按钮权限状态(0无效1有效)")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
