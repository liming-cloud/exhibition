package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.GlobalAreaPo;
import cn.exhibition.admin.domain.enums.ExhibitionType;
import cn.exhibition.admin.domain.vo.CountryStatVo;
import cn.exhibition.admin.domain.vo.ExhibitionListVo;
import cn.exhibition.admin.mapper.ExhibitionDetailMapper;
import cn.exhibition.admin.mapper.GlobalAreaMapper;
import cn.exhibition.admin.service.impl.ExhibitionDetailServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

@WebMvcTest(ExhibitionController.class)
@Import(ExhibitionDetailServiceImpl.class)
class ExhibitionControllerTest {

    private static final String BODY = """
            {"name":"迪拜能源展","location":"迪拜世贸中心","startDate":"2026-03-01","endDate":"2026-03-03",
             "sort":1,"content":"<p>介绍</p>","country":8,"countryName":"前端乱填","exhibitionType":"ENERGY"}""";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExhibitionDetailMapper mapper;

    @MockitoBean
    private GlobalAreaMapper globalAreaMapper;

    @BeforeEach
    void country() {
        GlobalAreaPo uae = new GlobalAreaPo();
        uae.setId(8);
        uae.setCountryName("阿联酋");
        when(globalAreaMapper.selectById(8)).thenReturn(uae);
    }

    @Test
    void add_fillsCountryNameFromArea() throws Exception {
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());

        ArgumentCaptor<ExhibitionDetailPo> saved = ArgumentCaptor.forClass(ExhibitionDetailPo.class);
        verify(mapper).insert(saved.capture());
        assertThat(saved.getValue().getCountryName()).isEqualTo("阿联酋");
        assertThat(saved.getValue().getExhibitionType()).isEqualTo(ExhibitionType.ENERGY);
        assertThat(saved.getValue().getStartDate()).isEqualTo(LocalDate.of(2026, 3, 1));
        assertThat(saved.getValue().getEndDate()).isEqualTo(LocalDate.of(2026, 3, 3));
    }

    @Test
    void add_acceptsOneDayExhibition() throws Exception {
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("2026-03-03", "2026-03-01")))
                .andExpect(status().isOk());
    }

    @Test
    void add_rejectsEndBeforeStart() throws Exception {
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("2026-03-03", "2026-02-28")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("结束日期不能早于开始日期"));
        verify(mapper, never()).insert(any(ExhibitionDetailPo.class));
    }

    @Test
    void add_rejectsMissingDates() throws Exception {
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("\"startDate\":\"2026-03-01\",", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请选择开始日期"));
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("\"endDate\":\"2026-03-03\",", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请选择结束日期"));
    }

    @Test
    void add_rejectsUnknownCountry() throws Exception {
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("\"country\":8", "\"country\":404")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("所选国家不存在"));
        verify(mapper, never()).insert(any(ExhibitionDetailPo.class));
    }

    @Test
    void add_rejectsMissingCountry() throws Exception {
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("\"country\":8,", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("请选择国家"));
    }

    @Test
    void add_rejectsUnknownType() throws Exception {
        mvc.perform(post("/exhibition/add").contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.replace("ENERGY", "FOOD")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("参数格式错误"));
    }

    @Test
    void update_okOr404() throws Exception {
        when(mapper.updateById(any(ExhibitionDetailPo.class))).thenReturn(1, 0);

        mvc.perform(put("/exhibition/update/3").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        mvc.perform(put("/exhibition/update/4").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("展会不存在"));
    }

    @Test
    void detailAndDelete_okOr404() throws Exception {
        ExhibitionDetailPo po = new ExhibitionDetailPo();
        po.setId(1);
        po.setName("迪拜能源展");
        po.setStartDate(LocalDate.of(2026, 3, 1));
        po.setEndDate(LocalDate.of(2026, 3, 3));
        po.setCreateTime(LocalDateTime.of(2026, 1, 2, 9, 30));
        when(mapper.selectById(1)).thenReturn(po);
        when(mapper.deleteById(1)).thenReturn(1);

        mvc.perform(get("/exhibition/detail/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("迪拜能源展"))
                .andExpect(jsonPath("$.data.startDate").value("2026-03-01"))
                .andExpect(jsonPath("$.data.endDate").value("2026-03-03"))
                .andExpect(jsonPath("$.data.createTime").value("2026-01-02 09:30:00"))
                .andExpect(jsonPath("$.data.time").doesNotExist());
        mvc.perform(get("/exhibition/detail/2")).andExpect(status().isNotFound());
        mvc.perform(delete("/exhibition/delete/1")).andExpect(status().isOk());
        mvc.perform(delete("/exhibition/delete/2")).andExpect(status().isNotFound());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void page_passesFiltersAndReturnsRowsWithoutContent() throws Exception {
        ExhibitionListVo row = new ExhibitionListVo();
        row.setId(1);
        row.setName("迪拜能源展");
        row.setStartDate(LocalDate.of(2026, 3, 1));
        row.setExhibitionType(ExhibitionType.ENERGY);
        ArgumentCaptor<Page<ExhibitionListVo>> page = (ArgumentCaptor) ArgumentCaptor.forClass(Page.class);
        ArgumentCaptor<ExhibitionBo> query = ArgumentCaptor.forClass(ExhibitionBo.class);
        ArgumentCaptor<LocalDate> today = ArgumentCaptor.forClass(LocalDate.class);
        when(mapper.selectListPage(page.capture(), query.capture(), today.capture())).thenAnswer(invocation -> {
            Page<ExhibitionListVo> p = invocation.getArgument(0);
            return p.setRecords(List.of(row)).setTotal(1);
        });

        mvc.perform(get("/exhibition/page").param("pageNum", "2").param("pageSize", "999")
                        .param("name", "能源").param("type", "ENERGY").param("year", "2026")
                        .param("continent", "亚洲").param("country", "8").param("upcoming", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.rows[0].name").value("迪拜能源展"))
                .andExpect(jsonPath("$.rows[0].startDate").value("2026-03-01"))
                .andExpect(jsonPath("$.rows[0].exhibitionType").value("ENERGY"))
                .andExpect(jsonPath("$.rows[0].content").doesNotExist());

        assertThat(page.getValue().getSize()).isEqualTo(100);
        assertThat(page.getValue().getCurrent()).isEqualTo(2);
        ExhibitionBo q = query.getValue();
        assertThat(q.getName()).isEqualTo("能源");
        assertThat(q.getType()).isEqualTo(ExhibitionType.ENERGY);
        assertThat(q.getYear()).isEqualTo(2026);
        assertThat(q.getContinent()).isEqualTo("亚洲");
        assertThat(q.getCountry()).isEqualTo(8);
        assertThat(q.getUpcoming()).isTrue();
        assertThat(today.getValue()).isEqualTo(LocalDate.now());
    }

    @Test
    void page_withoutFilters() throws Exception {
        ArgumentCaptor<ExhibitionBo> query = ArgumentCaptor.forClass(ExhibitionBo.class);
        when(mapper.selectListPage(any(), query.capture(), any())).thenAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(get("/exhibition/page")).andExpect(status().isOk());

        assertThat(query.getValue()).isEqualTo(new ExhibitionBo());
    }

    @Test
    void page_rejectsBadFilters() throws Exception {
        mvc.perform(get("/exhibition/page").param("type", "FOOD"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("参数格式错误"));
        mvc.perform(get("/exhibition/page").param("year", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("参数格式错误"));
        mvc.perform(get("/exhibition/page").param("upcoming", "maybe"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("参数格式错误"));
        verify(mapper, never()).selectListPage(any(), any(), any());
    }

    @Test
    void countries_listsCountriesWithCounts() throws Exception {
        CountryStatVo uae = new CountryStatVo();
        uae.setId(8);
        uae.setContinentName("亚洲");
        uae.setCountryName("阿联酋");
        uae.setExhibitionCount(3);
        uae.setUpcomingCount(1);
        when(mapper.selectCountryStats(LocalDate.now())).thenReturn(List.of(uae));

        mvc.perform(get("/exhibition/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(8))
                .andExpect(jsonPath("$.data[0].continentName").value("亚洲"))
                .andExpect(jsonPath("$.data[0].exhibitionCount").value(3))
                .andExpect(jsonPath("$.data[0].upcomingCount").value(1));
    }

    @Test
    void years_listsYears() throws Exception {
        when(mapper.selectYears()).thenReturn(List.of(2025, 2026));

        mvc.perform(get("/exhibition/years"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value(2025))
                .andExpect(jsonPath("$.data[1]").value(2026));
    }

    @Test
    void area_listsCountries() throws Exception {
        GlobalAreaPo uae = new GlobalAreaPo();
        uae.setCountryName("阿联酋");
        when(globalAreaMapper.selectList()).thenReturn(List.of(uae));

        mvc.perform(get("/exhibition/area"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].countryName").value("阿联酋"));
    }
}
