package com.aojiaodage.controller;

import com.aojiaodage.annotation.Required;
import com.aojiaodage.dto.LoginForm;
import com.aojiaodage.dto.Query;
import com.aojiaodage.dto.RefreshForm;
import com.aojiaodage.dto.UserForm;
import com.aojiaodage.entity.User;
import com.aojiaodage.service.UserService;
import com.aojiaodage.util.R;
import com.aojiaodage.validator.interfaces.Del;
import com.aojiaodage.validator.interfaces.Save;
import com.aojiaodage.validator.interfaces.Update;
import com.aojiaodage.vo.LoginInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<?> login(@Validated @RequestBody LoginForm loginForm) {
        LoginInfo<User> loginInfo = userService.login(loginForm);
        return R.ok(loginInfo);
    }

    @PostMapping("/logout")
    public R<?> logout() {
        userService.logout();
        return R.ok();
    }

    @PostMapping("/refresh")
    public R<?> refresh(@Validated @RequestBody RefreshForm refreshForm) {
        LoginInfo<User> loginInfo = userService.refresh(refreshForm);
        return R.ok(loginInfo);
    }

    @GetMapping("/pagination")
    public R<?> getList(Query query) {
        Page<User> pagination = userService.getPagination(query);
        return R.ok(pagination);
    }

    @Required("user:update")
    @PostMapping("/update")
    public R<?> update(@Validated(value = Update.class) @RequestBody UserForm form) {
        userService.update(form);
        return R.ok();
    }

    @Required("user:save")
    @PostMapping("/save")
    public R<?> save(@Validated(value = Save.class) @RequestBody UserForm form) {
        userService.save(form);
        return R.ok();
    }

    @Required("user:del")
    @PostMapping("/del")
    public R<?> del(@Validated(value = Del.class) @RequestBody UserForm form) {
        userService.del(form);
        return R.ok();
    }
}
