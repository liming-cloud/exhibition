package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author slm
 * @description
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${upload}")
    private String path;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // 按「年/月/随机名.扩展名」保存，避免同名文件（如粘贴截图统一叫 image.png）互相覆盖
            String relative = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy/MM"))
                + "/" + UUID.randomUUID().toString().replace("-", "") + extension(file.getOriginalFilename());
            Path filePath = Paths.get(path).toAbsolutePath().resolve(relative);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath.toFile());
            // 返回相对上传目录的路径，不暴露服务器目录
            return relative;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    /**
     * 取原始文件名的扩展名（含点，小写，只保留字母数字），没有则返回空串
     */
    private String extension(String originalFileName) {
        if (originalFileName == null) {
            return "";
        }
        int dot = originalFileName.lastIndexOf('.');
        String ext = dot < 0 ? "" : originalFileName.substring(dot + 1).toLowerCase().replaceAll("[^a-z0-9]", "");
        return ext.isEmpty() ? "" : "." + ext;
    }
}
