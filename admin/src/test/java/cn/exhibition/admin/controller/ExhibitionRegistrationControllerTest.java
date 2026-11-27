package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.ExhibitionRegistrationPo;
import cn.exhibition.admin.domain.enums.ContactProgress;
import cn.exhibition.admin.mapper.ExhibitionDetailMapper;
import cn.exhibition.admin.mapper.ExhibitionRegistrationMapper;
import cn.exhibition.admin.service.impl.ExhibitionRegistrationServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

@WebMvcTest(ExhibitionRegistrationController.class)
@Import(ExhibitionRegistrationServiceImpl.class)
class ExhibitionRegistrationControllerTest {

    private static final String BODY = """
            {"exhibitionId":1,"companyOrPersonalName":"某某科技","contactName":"张三","contactPhone":"13812345678",
             "contactProgress":"CLOSED","exhibitionName":"前端乱填"}""";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExhibitionRegistrationMapper mapper;

    @MockitoBean
    private ExhibitionDetailMapper exhibitionDetailMapper;

    private void exhibitionExists() {
        ExhibitionDetailPo exhibition = new ExhibitionDetailPo();
        exhibition.setId(1);
        exhibition.setName("迪拜能源展");
        when(exhibitionDetailMapper.selectById(1)).thenReturn(exhibition);
    }

    @Test
    void submit_savesWithServerSideFields() throws Exception {
        exhibitionExists();

        mvc.perform(post("/exhibition/registration/submit").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("报名成功"));

        ArgumentCaptor<ExhibitionRegistrationPo> saved = ArgumentCaptor.forClass(ExhibitionRegistrationPo.class);
        verify(mapper).insert(saved.capture());
        assertThat(saved.getValue().getExhibitionName()).isEqualTo("迪拜能源展");
        assertThat(saved.getValue().getContactProgress()).isEqualTo(ContactProgress.UNCONTACTED);
        assertThat(saved.getValue().getCreateTime()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"12812345678", "1381234567", "138123456789", "abc"})
    void submit_rejectsInvalidPhone(String phone) throws Exception {
        exhibitionExists();

        mvc.perform(post("/exhibition/registration/submit").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("13812345678", phone)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请输入 11 位手机号"));
        verify(mapper, never()).insert(any(ExhibitionRegistrationPo.class));
    }

    @Test
    void submit_rejectsBlankName() throws Exception {
        mvc.perform(post("/exhibition/registration/submit").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("张三", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请输入联系人姓名"));
    }

    @Test
    void submit_rejectsUnknownExhibition() throws Exception {
        mvc.perform(post("/exhibition/registration/submit").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("\"exhibitionId\":1", "\"exhibitionId\":2")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("报名的展会不存在"));
        verify(mapper, never()).insert(any(ExhibitionRegistrationPo.class));
    }

    @Test
    void page_worksWithoutParamsAndCapsSize() throws Exception {
        ArgumentCaptor<Page<ExhibitionRegistrationPo>> page = Pages.stubSelectPage(mapper);

        mvc.perform(get("/exhibition/registration/page")).andExpect(status().isOk());
        mvc.perform(get("/exhibition/registration/page").param("pageSize", "1000")).andExpect(status().isOk());

        assertThat(page.getAllValues()).extracting(Page::getSize).containsExactly(10L, 100L);
    }

    @Test
    void updateProgress_okOr404() throws Exception {
        when(mapper.updateById(any(ExhibitionRegistrationPo.class))).thenReturn(1, 0);

        mvc.perform(put("/exhibition/registration/update-progress/1").param("progress", "CONTACTED"))
                .andExpect(status().isOk());
        mvc.perform(put("/exhibition/registration/update-progress/2").param("progress", "CONTACTED"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("报名记录不存在"));

        ArgumentCaptor<ExhibitionRegistrationPo> saved = ArgumentCaptor.forClass(ExhibitionRegistrationPo.class);
        verify(mapper, org.mockito.Mockito.times(2)).updateById(saved.capture());
        assertThat(saved.getAllValues().get(0).getContactProgress()).isEqualTo(ContactProgress.CONTACTED);
    }

    @Test
    void updateProgress_rejectsUnknownOrMissingProgress() throws Exception {
        mvc.perform(put("/exhibition/registration/update-progress/1").param("progress", "DONE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("参数格式错误"));
        mvc.perform(put("/exhibition/registration/update-progress/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("缺少参数 progress"));
    }

    @Test
    void detailAndDelete_okOr404() throws Exception {
        ExhibitionRegistrationPo po = new ExhibitionRegistrationPo();
        po.setId(1);
        when(mapper.selectById(1)).thenReturn(po);
        when(mapper.deleteById(1)).thenReturn(1);

        mvc.perform(get("/exhibition/registration/detail/1")).andExpect(status().isOk());
        mvc.perform(get("/exhibition/registration/detail/2")).andExpect(status().isNotFound());
        mvc.perform(delete("/exhibition/registration/delete/1")).andExpect(status().isOk());
        mvc.perform(delete("/exhibition/registration/delete/2")).andExpect(status().isNotFound());
    }
}
