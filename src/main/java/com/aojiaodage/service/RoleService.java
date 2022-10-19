package com.aojiaodage.service;

import com.aojiaodage.dto.Query;
import com.aojiaodage.dto.RoleForm;
import com.aojiaodage.dto.RolePermissionForm;
import com.aojiaodage.entity.Role;
import com.aojiaodage.vo.RoleWithAssigned;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService extends IService<Role> {
    /**
     * 获取用户角色id
     * @param id 用户id
     * @return List<Integer> 角色id列表
     */
    List<Integer> getRoleIdsByUserId(Integer id);

    Page<Role> getPagination(Query query);

    void save(RoleForm form);
    void del(RoleForm form);
    void update(RoleForm form);

    List<Role> getList();

    List<Role> getListByUserId(Integer userId);
}
