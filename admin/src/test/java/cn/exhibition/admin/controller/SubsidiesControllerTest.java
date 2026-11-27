package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.entity.SubsidiesPo;
import cn.exhibition.admin.mapper.SubsidiesMapper;
import cn.exhibition.admin.service.impl.SubsidiesServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubsidiesController.class)
@Import(SubsidiesServiceImpl.class)
class SubsidiesControllerTest {

    private static final String BODY = """
            {"title":"展位补贴","content":"<p>内容</p>","price":5000,
             "startTime":"2026-01-01 00:00:00","endTime":"2026-12-31 00:00:00"}""";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SubsidiesMapper mapper;

    @Test
    void add_saves() throws Exception {
        mvc.perform(post("/subsidies/add").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());

        ArgumentCaptor<SubsidiesPo> saved = ArgumentCaptor.forClass(SubsidiesPo.class);
        verify(mapper).insert(saved.capture());
        assertThat(saved.getValue().getPrice()).isEqualByComparingTo(new BigDecimal("5000"));
        assertThat(saved.getValue().getEndTime().getMonthValue()).isEqualTo(12);
    }

    @Test
    void add_rejectsEndBeforeStart() throws Exception {
        mvc.perform(post("/subsidies/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("2026-12-31", "2025-12-31")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("结束日期不能早于开始日期"));
    }

    @Test
    void add_rejectsNegativePrice() throws Exception {
        mvc.perform(post("/subsidies/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("5000", "-1")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("补贴金额不能为负数"));
    }

    @Test
    void add_rejectsMissingStartTime() throws Exception {
        mvc.perform(post("/subsidies/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("\"startTime\":\"2026-01-01 00:00:00\",", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请选择开始日期"));
    }

    @Test
    void update_okOr404() throws Exception {
        when(mapper.updateById(any(SubsidiesPo.class))).thenReturn(1, 0);

        mvc.perform(put("/subsidies/update/3").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        mvc.perform(put("/subsidies/update/4").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("补贴政策不存在"));
    }

    @Test
    void detailAndDelete_okOr404() throws Exception {
        SubsidiesPo po = new SubsidiesPo();
        po.setId(1);
        when(mapper.selectById(1)).thenReturn(po);
        when(mapper.deleteById(1)).thenReturn(1);

        mvc.perform(get("/subsidies/detail/1")).andExpect(status().isOk());
        mvc.perform(get("/subsidies/detail/2")).andExpect(status().isNotFound());
        mvc.perform(delete("/subsidies/delete/1")).andExpect(status().isOk());
        mvc.perform(delete("/subsidies/delete/2")).andExpect(status().isNotFound());
    }

    @Test
    void page_capsPageSize() throws Exception {
        ArgumentCaptor<Page<SubsidiesPo>> page = Pages.stubSelectPage(mapper);

        mvc.perform(get("/subsidies/page").param("pageSize", "999")).andExpect(status().isOk());

        assertThat(page.getValue().getSize()).isEqualTo(100);
    }
}
