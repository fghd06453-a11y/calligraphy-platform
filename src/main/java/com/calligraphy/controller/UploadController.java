package com.calligraphy.controller;

import com.calligraphy.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path:D:/upload/}")
    private String uploadPath;

    @Value("${upload.url-prefix:http://localhost:8080/upload/}")
    private String uploadUrlPrefix;

    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return Result.fail("上传文件不能为空");
            }

            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null || !originalFilename.contains(".")) {
                return Result.fail("文件名不合法");
            }

            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;

            File dir = new File(uploadPath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(dir, fileName);
            file.transferTo(dest);

            String prefix = uploadUrlPrefix.endsWith("/") ? uploadUrlPrefix : uploadUrlPrefix + "/";
            return Result.success(prefix + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("上传失败：" + e.getMessage());
        }
    }
}