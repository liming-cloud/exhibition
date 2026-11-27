package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.dto.CarouselDto;
import cn.exhibition.admin.domain.bo.CarouselBo;
import cn.exhibition.admin.domain.entity.CarouselPo;
import cn.exhibition.admin.service.CarouselService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author slm
 * @description 轮播图
 */
@RestController
@RequestMapping("/carousel")
@RequiredArgsConstructor
public class CarouselController {

    private final CarouselService carouselService;

    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody CarouselDto dto) {
        carouselService.add(dto);
        return R.ok();
    }

    @PutMapping("/update/{id}")
    public R<Void> update(@PathVariable Integer id, @Valid @RequestBody CarouselDto dto) {
        carouselService.update(id, dto);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> deleteById(@PathVariable Integer id) {
        carouselService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    public R<CarouselPo> detail(@PathVariable Integer id) {
        return R.ok(carouselService.findById(id));
    }

    @GetMapping("/page")
    public TableDataInfo<CarouselPo> pageList(PageQuery pageQuery, String title,Integer status) {

        IPage<CarouselPo> page = carouselService.findByPage(pageQuery,new CarouselBo(title,status));
        return TableDataInfo.build(page);
    }
}
