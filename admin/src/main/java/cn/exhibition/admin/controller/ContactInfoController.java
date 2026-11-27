package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.domain.dto.ContactInfoDto;
import cn.exhibition.admin.domain.entity.ContactInfoPo;
import cn.exhibition.admin.service.ContactInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 联系信息控制器
 */
@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactInfoController {

    private final ContactInfoService contactInfoService;

    /**
     * 获取联系信息
     */
    @GetMapping("/info")
    public R<ContactInfoPo> getContactInfo() {
        return R.ok(contactInfoService.getContactInfo());
    }

    /**
     * 更新联系信息
     */
    @PutMapping("/update")
    public R<Void> updateContactInfo(@Valid @RequestBody ContactInfoDto dto) {
        contactInfoService.updateContactInfo(dto);
        return R.ok("更新成功");
    }
}