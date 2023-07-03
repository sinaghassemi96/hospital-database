package com.example.hospitaldatabase.controller;

import com.example.hospitaldatabase.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class UploadFileController {

    private final UploadFileService uploadFileService;

    @Autowired
    public UploadFileController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    @PostMapping("/uploadGeneralInformationFile")
    public String uploadGeneralInformationFile(@RequestBody MultipartFile file, @RequestParam("hospitalName") String hospitalName, @RequestParam("description") String description) {
        return uploadFileService.uploadFile(file, description, "GeneralInformation", hospitalName);

    }
}
