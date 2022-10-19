package com.aojiaodage.entity;

import com.aojiaodage.interfaces.Children;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (Permission)实体类
 *
 * @author makejava
 * @since 2022-10-12 11:49:29
 */
@TableName("tb_permission")
@Getter
@Setter
public class Permission implements Serializable, Children<Permission> {
    private static final long serialVersionUID = -59106826863538986L;

    @TableId
    private Integer id;
    /**
     * 权限类型；1为菜单，2为操作，3...
     */
    private Integer type;
    /**
     * 权限标识码
     */
    @TableField(value = "`value`")
    private String value;
    /**
     * 权限名称
     */
    @TableField(value = "`name`")
    private String name;
    
    private Date createdTime;
    
    private Date updatedTime;

    @TableField(value = "`path`")
    private String path;
    /**
     * 排序
     */
    @TableField(value = "`order`")
    private Integer order;
    /**
     * 可以用于保存json字符串，供前端使用
     */
    private String extra;
    
    private Integer parentId;

    @TableField(exist = false)
    private List<Permission> children;
}

