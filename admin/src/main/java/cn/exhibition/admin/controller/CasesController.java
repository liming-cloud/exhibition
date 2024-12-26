package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.entity.CasesPo;
import cn.exhibition.admin.service.CasesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @PostMapping("/save")
    public R<Void> save(@RequestBody CasesPo casesPo) {
        casesService.save(casesPo);
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
