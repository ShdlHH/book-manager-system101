package com.todo.todomanagementsystem.controller;

import com.todo.todomanagementsystem.dto.FileUploadVO;
import com.todo.todomanagementsystem.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Tag(name = "文件管理", description = "文件上传相关接口")
@Slf4j
public class FileController {

    private static final String UPLOAD_DIR = "uploads";
    private static final String BASE_URL = "https://xxx.com/uploads/";

    @Operation(summary = "上传文件", description = "上传图书封面或其他文件")
    @PostMapping("/upload")
    public Result<FileUploadVO> uploadFile(
            @Parameter(description = "文件对象") @RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isValidFileType(originalFilename)) {
            return Result.error("不支持的文件类型");
        }

        try {
            String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = uuid + extension;
            
            Path uploadPath = Paths.get(UPLOAD_DIR, dateDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(newFilename);
            file.transferTo(filePath.toFile());

            String fileUrl = BASE_URL + dateDir + "/" + newFilename;
            log.info("文件上传成功: {}", fileUrl);

            return Result.success(new FileUploadVO(fileUrl));

        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }
    }

    private boolean isValidFileType(String filename) {
        String lowerFilename = filename.toLowerCase();
        return lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")
                || lowerFilename.endsWith(".png") || lowerFilename.endsWith(".gif")
                || lowerFilename.endsWith(".pdf") || lowerFilename.endsWith(".doc")
                || lowerFilename.endsWith(".docx");
    }
}