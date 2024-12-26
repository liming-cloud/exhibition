package cn.exhibition.admin.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author slm
 * @description
 */

public interface FileService {

    String uploadFile(MultipartFile file);
}
