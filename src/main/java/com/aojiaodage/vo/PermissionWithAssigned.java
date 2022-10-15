package com.aojiaodage.vo;

import com.aojiaodage.entity.Permission;

public class PermissionWithAssigned extends Permission {

    private Integer assigned;

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }
}
