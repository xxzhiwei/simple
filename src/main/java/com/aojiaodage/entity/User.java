package com.aojiaodage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * tulingshop-admin用户(User)实体类
 *
 * @author makejava
 * @since 2022-10-12 11:50:07
 */
@TableName("tb_user")
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = -67544180507251291L;

    @TableId
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    @JsonIgnore
    private String password;
    /**
     * 用户头像，为一串url地址，默认为空。
     */
    private String avatar;
    
    private Date createdTime;
    
    private Date updatedTime;
    
    private String nickname;
    
    private String email;
    /**
     * 1正常，2禁用
     */
    private Integer state;
}

