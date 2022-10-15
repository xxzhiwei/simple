package com.aojiaodage.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RolePermissionForm {
    @NotNull(message = "id不能为空")
    private Integer id;
    private List<Integer> removing;
    private List<Integer> assigning;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getRemoving() {
        return removing;
    }

    public void setRemoving(List<Integer> removing) {
        this.removing = removing;
    }

    public List<Integer> getAssigning() {
        return assigning;
    }

    public void setAssigning(List<Integer> assigning) {
        this.assigning = assigning;
    }
}
