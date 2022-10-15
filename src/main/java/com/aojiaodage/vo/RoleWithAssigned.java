package com.aojiaodage.vo;

public class RoleWithAssigned {
    private Integer id;
    private String roleName;
    private boolean assigned; // false=未分配，true=已经分配

    public boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
