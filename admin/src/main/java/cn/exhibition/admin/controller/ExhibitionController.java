package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.GlobalAreaPo;
import cn.exhibition.admin.mapper.GlobalAreaMapper;
import cn.exhibition.admin.service.ExhibitionDetailService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @PostMapping("/save")
    public R<Void> save(@RequestBody ExhibitionDetailPo exhibitionDetailPo) {
        exhibitionService.save(exhibitionDetailPo);
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

    @GetMapping("/page")
    public TableDataInfo<ExhibitionDetailPo> pageList(PageQuery pageQuery,String name,Integer country) {

        IPage<ExhibitionDetailPo> page = exhibitionService.page(pageQuery,new ExhibitionBo(name,null,null,country));
        return TableDataInfo.build(page);
    }

    @GetMapping("/area")
    public R<List<GlobalAreaPo>> findGlobalArea() {
        return R.ok(globalAreaMapper.selectList());
    }


}
