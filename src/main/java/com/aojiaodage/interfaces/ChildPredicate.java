package com.aojiaodage.interfaces;

@FunctionalInterface
public interface ChildPredicate<T> {
    /**
     * 比较俩节点
     * @param o1 节点1
     * @param o2 节点2
     * @return boolean
     */
    boolean test(T o1, T o2);
}
