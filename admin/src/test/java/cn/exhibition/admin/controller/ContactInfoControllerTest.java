package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.entity.ContactInfoPo;
import cn.exhibition.admin.mapper.ContactInfoMapper;
import cn.exhibition.admin.service.impl.ContactInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactInfoController.class)
@Import(ContactInfoServiceImpl.class)
class ContactInfoControllerTest {

    private static final String BODY = """
            {"address":"北京市海淀区","phone":["010-12345678"],"contactPerson":"李四","workingHours":"周一至周五",
             "holidayArrangement":"节假日休息","email":"a@example.com","companyProfile":"简介","id":99}""";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ContactInfoMapper mapper;

    @Test
    void info_returnsEmptyObjectWithoutWriting() throws Exception {
        mvc.perform(get("/contact/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.address").doesNotExist());
        verify(mapper, never()).insert(any(ContactInfoPo.class));
    }

    @Test
    void info_returnsLatestRecord() throws Exception {
        ContactInfoPo po = new ContactInfoPo();
        po.setId(1);
        po.setPhone(List.of("010-12345678"));
        when(mapper.selectOne(any())).thenReturn(po);

        mvc.perform(get("/contact/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phone[0]").value("010-12345678"));
    }

    @Test
    void update_overwritesExistingRecord() throws Exception {
        ContactInfoPo existing = new ContactInfoPo();
        existing.setId(1);
        when(mapper.selectOne(any())).thenReturn(existing);

        mvc.perform(put("/contact/update").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("更新成功"));

        ArgumentCaptor<ContactInfoPo> saved = ArgumentCaptor.forClass(ContactInfoPo.class);
        verify(mapper).updateById(saved.capture());
        assertThat(saved.getValue().getId()).isEqualTo(1);
        assertThat(saved.getValue().getContactPerson()).isEqualTo("李四");
    }

    @Test
    void update_insertsWhenEmpty() throws Exception {
        mvc.perform(put("/contact/update").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());

        ArgumentCaptor<ContactInfoPo> saved = ArgumentCaptor.forClass(ContactInfoPo.class);
        verify(mapper).insert(saved.capture());
        assertThat(saved.getValue().getId()).isNull();
        assertThat(saved.getValue().getCreateTime()).isNotNull();
    }

    @Test
    void update_rejectsInvalidEmail() throws Exception {
        mvc.perform(put("/contact/update").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("a@example.com", "not-an-email")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("邮箱格式不正确"));
    }

    @Test
    void update_rejectsEmptyPhoneList() throws Exception {
        mvc.perform(put("/contact/update").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("[\"010-12345678\"]", "[]")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请输入联系电话"));
        mvc.perform(put("/contact/update").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("[\"010-12345678\"]", "[\" \"]")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("联系电话不能为空"));
    }
}
