package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author slm
 * @description
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile file) {
        return R.ok(fileService.uploadFile(file));
    }
}
