package cn.exhibition.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * @author slm
 * @description
 */

public interface FileService {

    String uploadFile(MultipartFile file);
}
