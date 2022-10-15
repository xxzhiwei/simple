package com.aojiaodage.service;

import com.aojiaodage.dto.PermissionForm;
import com.aojiaodage.entity.Permission;
import com.aojiaodage.vo.PermissionWithAssigned;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {

    /**
     * 查询用户的权限，并以角色为分组
     * @param userId 用户id
     * @return 角色id&权限码映射
     */
    Map<String, List<String>> getRoleToCodeMapByUserId(Integer userId);

    // 查询所有
    List<Permission> getList();
    void save(PermissionForm form);
    void del(PermissionForm form);
    void update(PermissionForm form);

    /**
     * 查询角色权限列表
     * @param id 角色id
     * @return 权限表（平级）
     */
    List<Permission> getPermissionListByRoleId(Integer id); // 只能说，使用前端框架，有别于原生js（框架方便阿
    // 查询（已登录）用户的菜单（平级）
    List<Permission> getMenuListByUserId(Integer id);
}
