package cn.exhibition.admin.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 测试工具：让 mock 的 selectPage 返回传入的分页对象（可放入给定记录），并记下每次查询用的分页参数
 */
final class Pages {

    private Pages() {
    }

    @SafeVarargs
    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T> ArgumentCaptor<Page<T>> stubSelectPage(BaseMapper<T> mapper, T... records) {
        ArgumentCaptor<Page<T>> captor = (ArgumentCaptor) ArgumentCaptor.forClass(Page.class);
        when(mapper.selectPage(captor.capture(), any())).thenAnswer(invocation -> {
            Page<T> page = invocation.getArgument(0);
            return page.setRecords(List.of(records)).setTotal(records.length);
        });
        return captor;
    }
}
