package com.aojiaodage.util;

import com.aojiaodage.interfaces.ChildPredicate;
import com.aojiaodage.interfaces.Children;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TreeMaker {
    // 生成树结构
    public static <T extends Children<T>> List<T> make(List<T> list, Predicate<T> rootPredicate, ChildPredicate<T> childPredicate) {
        List<T> roots = list
                .stream()
                .filter(rootPredicate)
                .collect(Collectors.toList());
        list.removeAll(roots);
        for (T root : roots) {
            List<T> children = makeChildren(root, list, childPredicate);
            root.setChildren(children);
            list.removeAll(children);
        }
        return roots;
    }

    /**
     * 递归设置子节点
     * @param parent 父节点
     * @param list 所有节点
     * @param childPredicate 子节点规则判断
     * @return 权限树
     */
    private static <T extends Children<T>> List<T> makeChildren(T parent, List<T> list, ChildPredicate<T> childPredicate) {
        return list
                .stream()
                .filter(item -> childPredicate.test(item, parent))
                .peek(item -> {
                    List<T> children = makeChildren(item, list, childPredicate);
                    if (children.size() > 0) {
                        item.setChildren(children);
                    }
                })
                .collect(Collectors.toList());
    }
}
