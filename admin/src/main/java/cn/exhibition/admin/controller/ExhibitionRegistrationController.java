package cn.exhibition.admin.controller;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.database.TableDataInfo;
import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.entity.ExhibitionRegistrationPo;
import cn.exhibition.admin.domain.enums.ContactProgress;
import cn.exhibition.admin.service.ExhibitionRegistrationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 展会报名控制器
 */
@RestController
@RequestMapping("/exhibition/registration")
@RequiredArgsConstructor
public class ExhibitionRegistrationController {

    private final ExhibitionRegistrationService registrationService;

    /**
     * 提交报名
     */
    @PostMapping("/submit")
    public R<Void> submitRegistration(@RequestBody ExhibitionRegistrationPo registration) {
        registrationService.save(registration);
        return R.ok("报名成功");
    }

    /**
     * 分页查询报名列表
     */
    @GetMapping("/page")
    public TableDataInfo<ExhibitionRegistrationPo> pageList(PageQuery pageQuery) {
        IPage<ExhibitionRegistrationPo> page = registrationService.pageWithExhibitionInfo(pageQuery);
        return TableDataInfo.build(page);
    }

    /**
     * 更新联系进度
     */
    @PutMapping("/update-progress/{id}")
    public R<Void> updateProgress(@PathVariable Integer id, @RequestParam String progress) {
        ContactProgress contactProgress = ContactProgress.fromCode(progress);
        registrationService.updateProgress(id, contactProgress);
        return R.ok("更新成功");
    }

    /**
     * 获取报名详情
     */
    @GetMapping("/detail/{id}")
    public R<ExhibitionRegistrationPo> getDetail(@PathVariable Integer id) {
        return R.ok(registrationService.getById(id));
    }

    /**
     * 删除报名记录
     */
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        registrationService.removeById(id);
        return R.ok("删除成功");
    }
}