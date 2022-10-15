package com.aojiaodage.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionRoleDao {
    void deleteByRoleIds(List<Integer> ids);

    void deleteByPermissionIds(List<Integer> ids);

    void deleteByPermissionId(Integer id);
}
