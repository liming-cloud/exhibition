package cn.exhibition.admin.controller;

import cn.exhibition.admin.domain.R;
import cn.exhibition.admin.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author slm
 * @description
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Value("${upload}")
    private String uploadDir;

    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile file) {
        return R.ok("操作成功",fileService.uploadFile(file));
    }

    @GetMapping("/preview")
    public void preview(@RequestParam String path, HttpServletResponse response) throws IOException {
        // 新数据是相对上传目录的路径，旧数据是绝对路径；两种都只允许读取上传目录下的文件
        Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path file = base.resolve(path).normalize();
        if (!file.startsWith(base) || !Files.isRegularFile(file)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(MediaTypeFactory.getMediaType(file.getFileName().toString())
            .orElse(MediaType.APPLICATION_OCTET_STREAM).toString());
        // 上传后文件不再被覆盖，可以长期缓存
        response.setHeader(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000");
        response.setContentLengthLong(Files.size(file));
        Files.copy(file, response.getOutputStream());
    }
}
