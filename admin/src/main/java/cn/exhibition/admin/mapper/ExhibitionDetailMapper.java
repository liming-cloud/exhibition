package cn.exhibition.admin.mapper;

import cn.exhibition.admin.database.BaseMapperPlus;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.vo.CountryStatVo;
import cn.exhibition.admin.domain.vo.ExhibitionListVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ExhibitionDetailMapper extends BaseMapperPlus<ExhibitionDetailPo,ExhibitionDetailPo> {

    /**
     * 列表分页：未结束的按开始日期升序在前，已结束的按开始日期倒序在后；同一天按 sort、id 升序
     *
     * @param today 判断是否结束的日期，结束日期早于它的算已结束
     */
    IPage<ExhibitionListVo> selectListPage(IPage<ExhibitionListVo> page, @Param("q") ExhibitionBo query, @Param("today") LocalDate today);

    /**
     * 有展会的国家，带展会总数和未结束数，按国家 id 排序
     */
    List<CountryStatVo> selectCountryStats(@Param("today") LocalDate today);

    /**
     * 展会开始日期出现过的年份，升序
     */
    List<Integer> selectYears();
}
