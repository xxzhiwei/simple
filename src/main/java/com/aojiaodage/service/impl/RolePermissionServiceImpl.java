package com.aojiaodage.service.impl;

import com.aojiaodage.dao.RolePermissionDao;
import com.aojiaodage.entity.RolePermission;
import com.aojiaodage.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDao, RolePermission> implements RolePermissionService {
}
