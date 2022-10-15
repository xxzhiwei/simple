package com.aojiaodage.service.impl;

import com.aojiaodage.dao.PermissionDao;
import com.aojiaodage.dto.PermissionForm;
import com.aojiaodage.entity.Permission;
import com.aojiaodage.entity.RolePermission;
import com.aojiaodage.enums.PermissionType;
import com.aojiaodage.exception.CustomException;
import com.aojiaodage.service.PermissionService;
import com.aojiaodage.service.RolePermissionService;
import com.aojiaodage.vo.RoleWithCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

    @Autowired
    RolePermissionService rolePermissionService;

    @Cacheable(cacheNames = "roleToCode", key = "#id")
    @Override
    public Map<String, List<String>> getRoleToCodeMapByUserId(Integer id) {
        List<RoleWithCode> roleWithCodeList = baseMapper.selectRoleListByUserId(id);

        Map<String, List<String>> map = new HashMap<>();

        for (RoleWithCode roleWithCode : roleWithCodeList) {
            map.put(roleWithCode.getRoleId().toString(), roleWithCode.getCodes());
        }
        return map;
    }

    @Override
    public List<Permission> getList() {
        List<Permission> permissions = list();
        return setChildrenForRoots(permissions);
    }

    @Override
    public void save(PermissionForm form) {
        boolean included = PermissionType.in(form.getType());
        if (!included) {
            throw new CustomException("字段type输入错误");
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(form, permission);

        // 根节点
        if (permission.getParentId() == null) {
            permission.setParentId(-1);
        }

        if (permission.getParentId() == -1) {
            if (PermissionType.Menu.getValue() != permission.getType()) {
                throw new CustomException("根权限必须为「菜单」类型");
            }
        }

        //「操作」权限只允许在根节点下添加
        save(permission);
    }

    @Transactional
    @Override
    public void del(PermissionForm form) {
        Integer id = form.getId();
        Permission old = getById(id);

        if (old == null) {
            throw new RuntimeException("指定的记录不存在");
        }

        // 若存在子权限时，不允许删除
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Permission::getParentId, id);
        Integer count = baseMapper.selectCount(lambdaQueryWrapper);

        if (count > 0) {
            throw new CustomException("操作失败，当前权限存在子权限");
        }

        // 删除角色&权限关系、权限
        LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionLambdaQueryWrapper.eq(RolePermission::getPermissionId, id);
        rolePermissionService.remove(rolePermissionLambdaQueryWrapper);
        removeById(id);
    }

    @Override
    public void update(PermissionForm form) {
        Permission old = getById(form.getId());

        if (old == null) {
            throw new RuntimeException("指定的记录不存在");
        }

        Permission permission = new Permission();
        permission.setId(form.getId());
        permission.setExtra(form.getExtra());
        permission.setOrder(form.getOrder());
        permission.setPath(form.getPath());
        permission.setName(form.getName());
        permission.setValue(form.getValue());
        updateById(permission);
    }

    @Cacheable(cacheNames = "user:menu", key = "#id")
    @Override
    public List<Permission> getMenuListByUserId(Integer id) {
        return baseMapper.selectMenuListByUserId(id);
    }

    // 这种管理员使用的接口，其实不需要缓存，使用频率不高
    @Override
    public List<Permission> getPermissionListByRoleId(Integer id) {
        return baseMapper.selectPermissionListByRoleId(id);
    }

    private <T extends Permission> List<T> setChildrenForRoots(List<T> permissions) {
        List<T> roots = permissions
                .stream()
                .filter(p -> p.getParentId() != null && p.getParentId().equals(-1))
                .collect(Collectors.toList());
        permissions.removeAll(roots);
        for (T root : roots) {
            List<Permission> children = makeChildren(root, permissions);
            root.setChildren(children);
            permissions.removeAll((List<T>) children);
        }
        return roots;
    }

    /**
     * 递归设置子节点
     * @param parent 父节点
     * @param permissions 权限列表
     * @return 权限树
     */
    private <T extends Permission> List<Permission> makeChildren(T parent, List<T> permissions) {
        return permissions
                .stream()
                .filter(p -> p.getParentId().equals(parent.getId()))
                .peek(p -> {
                    List<Permission> children = makeChildren(p, permissions);
                    if (children.size() > 0) {
                        p.setChildren(children);
                    }
                })
                .collect(Collectors.toList());
    }
}
