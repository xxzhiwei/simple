package com.aojiaodage.service.impl;

import com.aojiaodage.dao.UserRoleDao;
import com.aojiaodage.entity.UserRole;
import com.aojiaodage.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {
}
