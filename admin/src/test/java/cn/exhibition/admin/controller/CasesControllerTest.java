package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.entity.CasesPo;
import cn.exhibition.admin.mapper.CasesMapper;
import cn.exhibition.admin.service.impl.CasesServiceImpl;
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

@WebMvcTest(CasesController.class)
@Import(CasesServiceImpl.class)
class CasesControllerTest {

    private static final String BODY = "{\"title\":\"出团案例\",\"content\":\"<p>内容</p>\",\"coverUrl\":\"2026/11/c.png\",\"id\":99}";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CasesMapper mapper;

    @Test
    void add_ignoresClientId() throws Exception {
        mvc.perform(post("/cases/add").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());

        ArgumentCaptor<CasesPo> saved = ArgumentCaptor.forClass(CasesPo.class);
        verify(mapper).insert(saved.capture());
        assertThat(saved.getValue().getId()).isNull();
        assertThat(saved.getValue().getTitle()).isEqualTo("出团案例");
    }

    @Test
    void add_rejectsMissingCover() throws Exception {
        mvc.perform(post("/cases/add").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"出团案例\",\"content\":\"内容\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请上传封面图"));
    }

    @Test
    void add_rejectsTooLongTitle() throws Exception {
        mvc.perform(post("/cases/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("出团案例", "长".repeat(256))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("标题不能超过 255 个字"));
    }

    @Test
    void update_okOr404() throws Exception {
        when(mapper.updateById(any(CasesPo.class))).thenReturn(1, 0);

        mvc.perform(put("/cases/update/3").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        mvc.perform(put("/cases/update/4").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("案例不存在"));
    }

    @Test
    void detailAndDelete_return404WhenMissing() throws Exception {
        mvc.perform(get("/cases/detail/5")).andExpect(status().isNotFound());
        mvc.perform(delete("/cases/delete/5")).andExpect(status().isNotFound());
    }

    @Test
    void detail_returnsRecord() throws Exception {
        CasesPo po = new CasesPo();
        po.setId(5);
        po.setTitle("出团案例");
        when(mapper.selectById(5)).thenReturn(po);

        mvc.perform(get("/cases/detail/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("出团案例"));
    }

    @Test
    void page_capsPageSize() throws Exception {
        ArgumentCaptor<Page<CasesPo>> page = Pages.stubSelectPage(mapper);

        mvc.perform(get("/cases/page").param("pageSize", "500").param("title", "案例"))
                .andExpect(status().isOk());

        assertThat(page.getValue().getSize()).isEqualTo(100);
    }

    @Test
    void page_returnsSummaryInsteadOfContent() throws Exception {
        CasesPo po = new CasesPo();
        po.setId(1);
        po.setTitle("出团案例");
        po.setCoverUrl("2026/11/c.png");
        po.setContent("<h2>现场</h2>\n<p>展位&amp;洽谈</p>");
        Pages.stubSelectPage(mapper, po);

        mvc.perform(get("/cases/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows[0].coverUrl").value("2026/11/c.png"))
                .andExpect(jsonPath("$.rows[0].summary").value("现场 展位&洽谈"))
                .andExpect(jsonPath("$.rows[0].content").doesNotExist());
    }
}
