package com.aojiaodage.service.impl;

import com.aojiaodage.dao.PermissionDao;
import com.aojiaodage.dto.PermissionForm;
import com.aojiaodage.entity.Permission;
import com.aojiaodage.entity.RolePermission;
import com.aojiaodage.enums.PermissionType;
import com.aojiaodage.exception.CustomException;
import com.aojiaodage.interfaces.ChildPredicate;
import com.aojiaodage.service.PermissionService;
import com.aojiaodage.service.RolePermissionService;
import com.aojiaodage.util.TreeMaker;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

    @Autowired
    RolePermissionService rolePermissionService;

    Predicate<Permission> rootPredicate = item -> item.getParentId() != null && item.getParentId().equals(-1);

    ChildPredicate<Permission> childPredicate = (Permission o1, Permission o2) -> o1.getParentId().equals(o2.getId());

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
        List<Permission> list = list();
        return TreeMaker.make(list, rootPredicate, childPredicate);
    }

    @Override
    public void save(PermissionForm form) {
        boolean included = PermissionType.in(form.getType());
        if (!included) {
            throw new CustomException("??????type????????????");
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(form, permission);

        // ?????????
        if (permission.getParentId() == null) {
            permission.setParentId(-1);
        }

        if (permission.getParentId() == -1) {
            if (PermissionType.Menu.getValue() != permission.getType()) {
                throw new CustomException("????????????????????????????????????");
            }
        }

        //????????????????????????????????????????????????
        save(permission);
    }

    @Transactional
    @Override
    public void del(PermissionForm form) {
        Integer id = form.getId();
        Permission old = getById(id);

        if (old == null) {
            throw new RuntimeException("????????????????????????");
        }

        // ???????????????????????????????????????
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Permission::getParentId, id);
        Integer count = baseMapper.selectCount(lambdaQueryWrapper);

        if (count > 0) {
            throw new CustomException("??????????????????????????????????????????");
        }

        // ????????????&?????????????????????
        LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionLambdaQueryWrapper.eq(RolePermission::getPermissionId, id);
        rolePermissionService.remove(rolePermissionLambdaQueryWrapper);
        removeById(id);
    }

    @Override
    public void update(PermissionForm form) {
        Permission old = getById(form.getId());

        if (old == null) {
            throw new RuntimeException("????????????????????????");
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

    // ???????????????????????????????????????????????????????????????????????????
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
     * ?????????????????????
     * @param parent ?????????
     * @param permissions ????????????
     * @return ?????????
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
