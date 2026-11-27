package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.entity.CarouselPo;
import cn.exhibition.admin.mapper.CarouselMapper;
import cn.exhibition.admin.service.impl.CarouselServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarouselController.class)
@Import(CarouselServiceImpl.class)
class CarouselControllerTest {

    private static final String BODY = """
            {"title":"首页大图","imageUrl":"2026/11/a.png","link":"/exhibitions","sort":1,"status":1,
             "id":99,"createTime":"2020-01-01 00:00:00"}""";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CarouselMapper mapper;

    @Test
    void add_savesFormFieldsAndFillsTime() throws Exception {
        mvc.perform(post("/carousel/add").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        ArgumentCaptor<CarouselPo> saved = ArgumentCaptor.forClass(CarouselPo.class);
        verify(mapper).insert(saved.capture());
        assertThat(saved.getValue().getTitle()).isEqualTo("首页大图");
        // 前端传的 id、createTime 不生效，时间由后端填写
        assertThat(saved.getValue().getId()).isNull();
        assertThat(saved.getValue().getCreateTime().getYear()).isNotEqualTo(2020);
    }

    @Test
    void add_rejectsBlankTitle() throws Exception {
        mvc.perform(post("/carousel/add").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\" \",\"imageUrl\":\"a.png\",\"link\":\"/\",\"sort\":1,\"status\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("标题不能为空"));
        verify(mapper, never()).insert(any(CarouselPo.class));
    }

    @Test
    void add_rejectsInvalidStatus() throws Exception {
        mvc.perform(post("/carousel/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("\"status\":1", "\"status\":5")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("状态只能是 0 或 1"));
    }

    @Test
    void add_rejectsMalformedJson() throws Exception {
        mvc.perform(post("/carousel/add").contentType(MediaType.APPLICATION_JSON).content("{bad"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("参数格式错误"));
    }

    @Test
    void update_usesPathId() throws Exception {
        when(mapper.updateById(any(CarouselPo.class))).thenReturn(1);

        mvc.perform(put("/carousel/update/7").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());

        ArgumentCaptor<CarouselPo> saved = ArgumentCaptor.forClass(CarouselPo.class);
        verify(mapper).updateById(saved.capture());
        assertThat(saved.getValue().getId()).isEqualTo(7);
        assertThat(saved.getValue().getCreateTime()).isNull();
        assertThat(saved.getValue().getUpdateTime()).isNotNull();
    }

    @Test
    void update_returns404WhenMissing() throws Exception {
        mvc.perform(put("/carousel/update/7").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.msg").value("轮播图不存在"));
    }

    @Test
    void detail_returnsRecordOr404() throws Exception {
        CarouselPo po = new CarouselPo();
        po.setId(1);
        po.setTitle("首页大图");
        when(mapper.selectById(1)).thenReturn(po);

        mvc.perform(get("/carousel/detail/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("首页大图"));
        mvc.perform(get("/carousel/detail/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("轮播图不存在"));
    }

    @Test
    void delete_returnsOkOr404() throws Exception {
        when(mapper.deleteById(1)).thenReturn(1);

        mvc.perform(delete("/carousel/delete/1")).andExpect(status().isOk());
        mvc.perform(delete("/carousel/delete/2")).andExpect(status().isNotFound());
    }

    @Test
    void detail_rejectsNonNumericId() throws Exception {
        mvc.perform(get("/carousel/detail/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("参数格式错误"));
    }

    @Test
    void page_capsPageSizeAt100AndDefaultsTo10() throws Exception {
        ArgumentCaptor<Page<CarouselPo>> page = Pages.stubSelectPage(mapper);

        mvc.perform(get("/carousel/page").param("pageNum", "1").param("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        mvc.perform(get("/carousel/page"))
                .andExpect(status().isOk());

        assertThat(page.getAllValues()).extracting(Page::getSize).containsExactly(100L, 10L);
    }

    @Test
    void unexpectedError_returns500WithoutDetails() throws Exception {
        when(mapper.selectById(1)).thenThrow(new IllegalStateException("db down"));

        mvc.perform(get("/carousel/detail/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("系统出错，请稍后重试"));
    }

    @Test
    void wrongMethod_keepsSpringStatus() throws Exception {
        mvc.perform(get("/carousel/add"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value(405));
    }
}
