package com.calligraphy.controller;

import com.calligraphy.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final String UPLOAD_DIR = "D:/upload/";

    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String suffix = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf("."));

            String fileName = UUID.randomUUID() + suffix;

            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(dir, fileName);
            file.transferTo(dest);

            return Result.success("http://localhost:8080/upload/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }
    }
}