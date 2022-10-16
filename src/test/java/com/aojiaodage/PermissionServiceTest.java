package com.aojiaodage;

import com.aojiaodage.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class PermissionServiceTest {

    @Autowired
    PermissionService permissionService;

    @Test
    public void testGetRoleToCodeMap() {
        Map<String, List<String>> roleToCodeMap = permissionService.getRoleToCodeMapByUserId(1);

        Set<Map.Entry<String, List<String>>> entries = roleToCodeMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            System.out.println("roleId：" + entry.getKey());
            System.out.println("code：" + entry.getValue());
        }
    }
}
