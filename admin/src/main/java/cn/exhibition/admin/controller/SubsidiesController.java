package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.dto.SubsidiesDto;
import cn.exhibition.admin.domain.entity.SubsidiesPo;
import cn.exhibition.admin.domain.vo.SubsidiesListVo;
import cn.exhibition.admin.service.SubsidiesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author slm
 * @description 补贴
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/subsidies")
public class SubsidiesController {

    private final SubsidiesService subsidiesService;

    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody SubsidiesDto dto) {
        subsidiesService.add(dto);
        return R.ok();
    }

    @PutMapping("/update/{id}")
    public R<Void> update(@PathVariable Integer id, @Valid @RequestBody SubsidiesDto dto) {
        subsidiesService.update(id, dto);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> deleteById(@PathVariable Integer id) {
        subsidiesService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    public R<SubsidiesPo> detail(@PathVariable Integer id) {
        SubsidiesPo subsidiesPo = subsidiesService.findById(id);
        return R.ok(subsidiesPo);
    }

    @GetMapping("/page")
    public TableDataInfo<SubsidiesListVo> page(PageQuery pageQuery,String title) {
        IPage<SubsidiesListVo> page = subsidiesService.page(pageQuery,title);
        return TableDataInfo.build(page);
    }
}
