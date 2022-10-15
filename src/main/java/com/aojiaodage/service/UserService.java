package com.aojiaodage.service;

import com.aojiaodage.dto.LoginForm;
import com.aojiaodage.dto.Query;
import com.aojiaodage.dto.RefreshForm;
import com.aojiaodage.dto.UserForm;
import com.aojiaodage.entity.User;
import com.aojiaodage.vo.LoginInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface UserService {
    LoginInfo<User> login(LoginForm loginForm);
    void logout();
    // 刷新token
    LoginInfo<User> refresh(RefreshForm refreshForm);

    Page<User> getPagination(Query query);

    void update(UserForm form);

    void save(UserForm form);

    void del(UserForm form);
}
