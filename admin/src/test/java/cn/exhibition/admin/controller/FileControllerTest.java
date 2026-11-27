package cn.exhibition.admin.controller;

import cn.exhibition.admin.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FileService fileService;

    @Test
    void upload_returnsRelativePath() throws Exception {
        when(fileService.uploadFile(any())).thenReturn("2026/11/abc.png");

        mvc.perform(multipart("/file/upload").file(new MockMultipartFile("file", "a.png", "image/png", new byte[]{1})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("2026/11/abc.png"));
    }

    @Test
    void upload_rejectsMissingFile() throws Exception {
        mvc.perform(multipart("/file/upload"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void preview_returns404OutsideUploadDir() throws Exception {
        mvc.perform(get("/file/preview").param("path", "../../etc/passwd")).andExpect(status().isNotFound());
        mvc.perform(get("/file/preview").param("path", "2026/11/missing.png")).andExpect(status().isNotFound());
    }

    @Test
    void preview_rejectsMissingPath() throws Exception {
        mvc.perform(get("/file/preview"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("缺少参数 path"));
    }
}
