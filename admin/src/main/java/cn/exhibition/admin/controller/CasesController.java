package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.dto.CasesDto;
import cn.exhibition.admin.domain.entity.CasesPo;
import cn.exhibition.admin.service.CasesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author slm
 * @description 案例
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/cases")
public class CasesController {

    private final CasesService casesService;

    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody CasesDto dto) {
        casesService.add(dto);
        return R.ok();
    }

    @PutMapping("/update/{id}")
    public R<Void> update(@PathVariable Integer id, @Valid @RequestBody CasesDto dto) {
        casesService.update(id, dto);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        casesService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    public R<CasesPo> detail(@PathVariable Integer id) {
        return R.ok(casesService.findById(id));
    }

    @GetMapping("/page")
    public TableDataInfo<CasesPo> dataGrid(PageQuery pageQuery,String title) {

        IPage<CasesPo> page = casesService.findByPage(pageQuery,title);
        return TableDataInfo.build(page);
    }

}
