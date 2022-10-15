package com.aojiaodage.controller;

import com.aojiaodage.annotation.Required;
import com.aojiaodage.dto.Query;
import com.aojiaodage.dto.RoleForm;
import com.aojiaodage.entity.Role;
import com.aojiaodage.service.RoleService;
import com.aojiaodage.util.R;
import com.aojiaodage.validator.interfaces.Del;
import com.aojiaodage.validator.interfaces.Save;
import com.aojiaodage.validator.interfaces.Update;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    // 获取角色分页
    @GetMapping("/pagination")
    public R<?> getPagination(Query query) {
        Page<Role> page = roleService.getPagination(query);

        return R.ok(page);
    }

    @GetMapping("/list")
    public R<?> getList() {
        List<Role> roles = roleService.getList();

        return R.ok(roles);
    }

    @GetMapping("/user/{userId}/list")
    public R<?> getListByUserId(@PathVariable Integer userId) {
        List<Role> roles = roleService.getListByUserId(userId);

        return R.ok(roles);
    }

    @Required("role:save")
    @PostMapping("/save")
    public R<?> save(@Validated(value = Save.class) @RequestBody RoleForm form) {
        roleService.save(form);
        return R.ok();
    }

    @Required("role:del")
    @PostMapping("/del")
    public R<?> del(@Validated(value = Del.class) @RequestBody RoleForm form) {
        roleService.del(form);
        return R.ok();
    }

    @Required("role:update")
    @PostMapping("/update")
    public R<?> update(@Validated(value = Update.class) @RequestBody RoleForm form) {
        roleService.update(form);
        return R.ok();
    }
}
