package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.dto.InformationDto;
import cn.exhibition.admin.domain.entity.InformationPo;
import cn.exhibition.admin.service.InformationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author slm
 * @description 新闻咨询
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/information")
public class InformationController {

    private final InformationService informationService;

    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody InformationDto dto) {
        informationService.add(dto);
        return R.ok();
    }

    @PutMapping("/update/{id}")
    public R<Void> update(@PathVariable Integer id, @Valid @RequestBody InformationDto dto) {
        informationService.update(id, dto);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        informationService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    public R<InformationPo> detail(@PathVariable Integer id) {
        return R.ok(informationService.findById(id));
    }

    @GetMapping("/page")
    public TableDataInfo<InformationPo> page(PageQuery pageQuery,String title) {
        IPage<InformationPo> page = informationService.findByPage(pageQuery,title);
        return TableDataInfo.build(page);
    }
 }
