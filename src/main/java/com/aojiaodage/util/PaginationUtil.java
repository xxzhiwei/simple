package com.aojiaodage.util;

import com.aojiaodage.dto.Query;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class PaginationUtil {
    // 获取分页偏移量
    public static Long getPaginationOffset(Long current, Long size) {
        return (current - 1) * size;
    }

    // 计算总页码
    public static Long calculatePages(Long total, Long size) {
        long pages = total / size;

        if (total % size != 0) {
            pages += 1;
        }
        return pages;
    }
    public static void setPaginationIfNecessary(Query query) {
        long current = query.getCurrent();
        long size = query.getSize();

        if (current <= 0) {
            query.setCurrent(1);
        }

        if (size <= 0) {
            query.setSize(10);
        }
    }

    public static <T> Page<T> getPage(Query query) {
        setPaginationIfNecessary(query);
        return new Page<>(query.getCurrent(), query.getSize());
    }
}
