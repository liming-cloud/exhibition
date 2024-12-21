package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        return R.ok("操作成功",fileService.uploadFile(file));
    }

    @GetMapping("/preview")
    public void preview(@RequestParam String path, HttpServletResponse response) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            response.setContentType("image/jpeg");
            Files.copy(file.toPath(), response.getOutputStream());
        }
    }
}
