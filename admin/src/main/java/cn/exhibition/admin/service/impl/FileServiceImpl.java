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
            // 获取存储路径
            Path uploadPath = Paths.get(path);
            // 确保目录存在
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // 获取原始文件名并处理（去除空格和特殊字符）
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IOException("File name is invalid.");
            }
            // 只替换空格为下划线，保留中文字符和其他合法字符
            String sanitizedFileName = originalFileName.trim()
                .replaceAll("\\s+", "_")
                .replaceAll("[\\\\/:*?\"<>|]", ""); // 只移除 Windows/Unix 文件系统中的非法字符

            // 保存文件
            Path filePath = uploadPath.resolve(sanitizedFileName);
            file.transferTo(filePath.toFile());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
