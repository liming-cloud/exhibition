package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.entity.InformationPo;
import cn.exhibition.admin.mapper.InformationMapper;
import cn.exhibition.admin.service.impl.InformationServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InformationController.class)
@Import(InformationServiceImpl.class)
class InformationControllerTest {

    private static final String BODY = "{\"title\":\"市场简讯\",\"content\":\"<p>内容</p>\",\"coverUrl\":\"2026/11/n.png\"}";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private InformationMapper mapper;

    @Test
    void add_saves() throws Exception {
        mvc.perform(post("/information/add").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());

        ArgumentCaptor<InformationPo> saved = ArgumentCaptor.forClass(InformationPo.class);
        verify(mapper).insert(saved.capture());
        assertThat(saved.getValue().getCoverUrl()).isEqualTo("2026/11/n.png");
    }

    @Test
    void add_rejectsBlankContent() throws Exception {
        mvc.perform(post("/information/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("<p>内容</p>", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("内容不能为空"));
    }

    @Test
    void update_okOr404() throws Exception {
        when(mapper.updateById(any(InformationPo.class))).thenReturn(1, 0);

        mvc.perform(put("/information/update/3").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        mvc.perform(put("/information/update/4").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("资讯不存在"));
    }

    @Test
    void detailAndDelete_okOr404() throws Exception {
        InformationPo po = new InformationPo();
        po.setId(1);
        when(mapper.selectById(1)).thenReturn(po);
        when(mapper.deleteById(1)).thenReturn(1);

        mvc.perform(get("/information/detail/1")).andExpect(status().isOk());
        mvc.perform(get("/information/detail/2")).andExpect(status().isNotFound());
        mvc.perform(delete("/information/delete/1")).andExpect(status().isOk());
        mvc.perform(delete("/information/delete/2")).andExpect(status().isNotFound());
    }

    @Test
    void page_capsPageSize() throws Exception {
        ArgumentCaptor<Page<InformationPo>> page = Pages.stubSelectPage(mapper);

        mvc.perform(get("/information/page").param("pageNum", "2").param("pageSize", "101"))
                .andExpect(status().isOk());

        assertThat(page.getValue().getSize()).isEqualTo(100);
        assertThat(page.getValue().getCurrent()).isEqualTo(2);
    }

    @Test
    void page_returnsSummaryInsteadOfContent() throws Exception {
        InformationPo po = new InformationPo();
        po.setId(1);
        po.setTitle("市场简讯");
        po.setContent("<p>迪拜&nbsp;<b>能源展</b></p><img src=\"a.png\"><table><tr><td>报名中</td></tr></table>");
        Pages.stubSelectPage(mapper, po);

        mvc.perform(get("/information/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.rows[0].title").value("市场简讯"))
                .andExpect(jsonPath("$.rows[0].summary").value("迪拜 能源展报名中"))
                .andExpect(jsonPath("$.rows[0].content").doesNotExist());
    }
}
