package com.aojiaodage.vo;

import java.util.List;

public class RoleWithCode {
    private Integer roleId;

    private List<String> codes;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
