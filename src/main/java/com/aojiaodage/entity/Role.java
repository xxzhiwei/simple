package com.aojiaodage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * (Role)实体类
 *
 * @author makejava
 * @since 2022-10-12 11:50:31
 */
@TableName("tb_role")
@Getter
@Setter
public class Role implements Serializable {
    private static final long serialVersionUID = -85348133295380209L;
    
    private Integer id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
    
    private Date createdTime;
    
    private Date updatedTime;


}

