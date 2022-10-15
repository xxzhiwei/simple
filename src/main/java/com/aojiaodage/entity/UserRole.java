package com.aojiaodage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * (UserRole)实体类
 *
 * @author makejava
 * @since 2022-10-12 17:06:33
 */
@TableName("tb_user_role")
@Getter
@Setter
@Builder
public class UserRole implements Serializable {
    private static final long serialVersionUID = -35354760799069299L;

    @Id
    private Integer id;
    
    private Integer userId;
    
    private Integer roleId;


}

