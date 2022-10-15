package com.aojiaodage.controller;

import com.aojiaodage.annotation.Required;
import com.aojiaodage.dto.PermissionForm;
import com.aojiaodage.entity.Permission;
import com.aojiaodage.service.PermissionService;
import com.aojiaodage.util.R;
import com.aojiaodage.util.UserUtil;
import com.aojiaodage.validator.interfaces.Del;
import com.aojiaodage.validator.interfaces.Save;
import com.aojiaodage.validator.interfaces.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    // 这些获取数据的权限到底需不需要？获取数据相比按钮，还是后者比较直观一些
    // 是不是可以这样考虑：在能进入某个页面，获取数据的权限都是拥有的？以后如有需要再添加
    @GetMapping("/role/{roleId}/list")
    public R<List<Permission>> getPermissionListByRoleId(@PathVariable Integer roleId) {
        List<Permission> permissions = permissionService.getPermissionListByRoleId(roleId);
        return R.ok(permissions);
    }

    // 这个可以返回用户所有的权限给前端，让前端自己拆分【菜单、操作权限】
    @GetMapping("/user/menuList")
    public R<List<Permission>> getMenuListByUserId() {
        Integer id = UserUtil.get().getId();
        List<Permission> permissions = permissionService.getMenuListByUserId(id);
        return R.ok(permissions);
    }

    // 获取权限列表
    @GetMapping("/list")
    public R<List<Permission>> getList() {
        List<Permission> permissions = permissionService.getList();
        return R.ok(permissions);
    }

    @Required("permission:update")
    @PostMapping("/update")
    public R<?> update(@Validated(value = Update.class) @RequestBody PermissionForm form) {
        permissionService.update(form);
        return R.ok();
    }

    @Required("permission:del")
    @PostMapping("/del")
    public R<?> del(@Validated(value = Del.class) @RequestBody PermissionForm form) {
        permissionService.del(form);
        return R.ok();
    }

    @Required("permission:save")
    @PostMapping("/save")
    public R<?> save(@Validated(value = Save.class) @RequestBody PermissionForm form) {
        permissionService.save(form);
        return R.ok();
    }
}
