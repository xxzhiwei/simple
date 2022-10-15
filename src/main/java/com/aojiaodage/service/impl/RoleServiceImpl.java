package com.aojiaodage.service.impl;

import com.aojiaodage.dao.RoleDao;
import com.aojiaodage.dto.Query;
import com.aojiaodage.dto.RoleForm;
import com.aojiaodage.entity.Role;
import com.aojiaodage.entity.RolePermission;
import com.aojiaodage.entity.UserRole;
import com.aojiaodage.exception.CustomException;
import com.aojiaodage.service.RolePermissionService;
import com.aojiaodage.service.RoleService;
import com.aojiaodage.service.UserRoleService;
import com.aojiaodage.util.PaginationUtil;
import com.aojiaodage.vo.RoleWithAssigned;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Cacheable(cacheNames = "user:roleId", key = "#id")
    @Override
    public List<Integer> getRoleIdsByUserId(Integer id) {
        List<Role> roles = baseMapper.selectListByUserId(id);

        return roles.stream().map(Role::getId).collect(Collectors.toList());
    }

    @Override
    public Page<Role> getPagination(Query query) {
        Page<Role> page = PaginationUtil.getPage(query);
        page(page);
        return page;
    }

    @Transactional
    @Override
    public void save(RoleForm form) {
        Role role = new Role();
        BeanUtils.copyProperties(form, role);

        save(role);

        List<Integer> assigning = form.getAssigning();
        if (assigning != null && assigning.size() > 0) {
            List<RolePermission> rolePermissions = assigning
                    .stream()
                    .map(pId -> RolePermission.builder().roleId(role.getId()).permissionId(pId).build())
                    .collect(Collectors.toList());
            rolePermissionService.saveBatch(rolePermissions);
        }
    }

    @Transactional
    @Override
    public void del(RoleForm form) {
        Integer id = form.getId();
        Role role = getById(id);
        if (role == null) {
            throw new CustomException("数据不存在");
        }
        removeById(id);

        // 移除user&role关系
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getRoleId, id);
        userRoleService.remove(userRoleLambdaQueryWrapper);
        // 移除角色权限
        LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionLambdaQueryWrapper.eq(RolePermission::getRoleId, id);
        rolePermissionService.remove(rolePermissionLambdaQueryWrapper);
    }

    // 更新角色权限
    @CacheEvict(value = {"roleToCode"}, allEntries = true)
    @Transactional
    @Override
    public void update(RoleForm form) {
        Integer id = form.getId();
        Role role = getById(id);
        if (role == null) {
            throw new CustomException("数据不存在");
        }

        role.setName(form.getName());
        role.setDescription(form.getDescription());

        List<Integer> assigning = form.getAssigning();
        List<Integer> removing = form.getRemoving();

        // 新增角色权限
        if (assigning != null && assigning.size() > 0) {
            List<RolePermission> rolePermissions = assigning
                    .stream()
                    .map(permissionId -> RolePermission.builder().roleId(id).permissionId(permissionId).build())
                    .collect(Collectors.toList());
            rolePermissionService.saveBatch(rolePermissions);
        }

        // 移除角色权限
        if (removing != null && removing.size() > 0) {
            rolePermissionService.removeByIds(removing);
        }

        updateById(role);
    }

    @Override
    public List<Role> getList() {
        return list();
    }

    @Cacheable(cacheNames = "user:role", key = "#id")
    @Override
    public List<Role> getListByUserId(Integer id) {
        return baseMapper.selectListByUserId(id);
    }
}
