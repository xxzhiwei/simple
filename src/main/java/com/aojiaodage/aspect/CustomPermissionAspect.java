package com.aojiaodage.aspect;

import com.aojiaodage.annotation.Required;
import com.aojiaodage.entity.User;
import com.aojiaodage.exception.NoPermissionException;
import com.aojiaodage.service.PermissionService;
import com.aojiaodage.service.RoleService;
import com.aojiaodage.util.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
public class CustomPermissionAspect {

    @Autowired
    PermissionService permissionService;

    @Autowired
    RoleService roleService;

    /**
     * 校验用户访问权限
     * 1、查询用户的权限码；
     * 2、根据用户的角色进行对比；
     * 3、不匹配时将会抛异常
     */
    @Before(value = "@annotation(com.aojiaodage.annotation.Required)")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Required annotation = methodSignature.getMethod().getAnnotation(Required.class);
        String code = annotation.value();

        if (!"".equals(code)) {
            User user = UserUtil.get();
            Map<String, List<String>> codesMap = permissionService.getRoleToCodeMapByUserId(user.getId());
            List<Integer> roleIds = roleService.getRoleIdsByUserId(user.getId());

            boolean matched = false;

            for (Integer roleId : roleIds) {
                List<String> codes = codesMap.get(roleId.toString());
                if (codes != null && codes.contains(code)) {
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                throw new NoPermissionException();
            }
        }
    }
}
