package com.aojiaodage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * (RolePermission)实体类
 *
 * @author makejava
 * @since 2022-10-12 16:08:52
 */
@TableName(value = "tb_role_permission")
@Getter
@Setter
@Builder
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 146355917721471840L;

    @Id
    private Integer id;
    
    private Integer roleId;
    
    private Integer permissionId;
}

