package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.dto.ExhibitionDto;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.GlobalAreaPo;
import cn.exhibition.admin.domain.vo.CountryStatVo;
import cn.exhibition.admin.domain.vo.ExhibitionListVo;
import cn.exhibition.admin.mapper.GlobalAreaMapper;
import cn.exhibition.admin.service.ExhibitionDetailService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author slm
 * @description
 */
@RestController
@RequestMapping("/exhibition")
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionDetailService exhibitionService;
    private final GlobalAreaMapper globalAreaMapper;

    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody ExhibitionDto dto) {
        exhibitionService.add(dto);
        return R.ok();
    }

    @PutMapping("/update/{id}")
    public R<Void> update(@PathVariable Integer id, @Valid @RequestBody ExhibitionDto dto) {
        exhibitionService.update(id, dto);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> deleteById(@PathVariable Integer id) {
        exhibitionService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    public R<ExhibitionDetailPo> detail(@PathVariable Integer id) {
        return R.ok(exhibitionService.findById(id));
    }

    /**
     * 列表不含正文。筛选参数见 {@link ExhibitionBo}
     */
    @GetMapping("/page")
    public TableDataInfo<ExhibitionListVo> pageList(PageQuery pageQuery, ExhibitionBo query) {
        IPage<ExhibitionListVo> page = exhibitionService.page(pageQuery, query);
        return TableDataInfo.build(page);
    }

    @GetMapping("/area")
    public R<List<GlobalAreaPo>> findGlobalArea() {
        return R.ok(globalAreaMapper.selectList());
    }

    /**
     * 有展会的国家及展会数量
     */
    @GetMapping("/countries")
    public R<List<CountryStatVo>> countries() {
        return R.ok(exhibitionService.countryStats());
    }

    /**
     * 有展会的年份
     */
    @GetMapping("/years")
    public R<List<Integer>> years() {
        return R.ok(exhibitionService.years());
    }


}
