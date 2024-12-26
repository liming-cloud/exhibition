package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.entity.SubsidiesPo;
import cn.exhibition.admin.service.SubsidiesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @PostMapping("/save")
    public R<Void> save(@RequestBody SubsidiesPo subsidiesPo) {
        subsidiesService.save(subsidiesPo);
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
    public TableDataInfo<SubsidiesPo> page(PageQuery pageQuery,String title) {
        IPage<SubsidiesPo> page = subsidiesService.page(pageQuery,title);
        return TableDataInfo.build(page);
    }
}
