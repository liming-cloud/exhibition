package cn.exhibition.admin.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 测试工具：让 mock 的 selectPage 原样返回传入的分页对象，并记下每次查询用的分页参数
 */
final class Pages {

    private Pages() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T> ArgumentCaptor<Page<T>> stubSelectPage(BaseMapper<T> mapper) {
        ArgumentCaptor<Page<T>> captor = (ArgumentCaptor) ArgumentCaptor.forClass(Page.class);
        when(mapper.selectPage(captor.capture(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        return captor;
    }
}
