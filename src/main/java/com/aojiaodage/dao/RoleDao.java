package com.aojiaodage.dao;

import com.aojiaodage.entity.Role;
import com.aojiaodage.vo.RoleWithAssigned;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleDao extends BaseMapper<Role> {

    // 查询所有角色，若指定的用户已分配某个角色，则assigned为true，否则为false
    // List<RoleWithAssigned> selectRolesWithAssignedByUserId(Integer id);
    List<Role> selectListByUserId(Integer id);
}
