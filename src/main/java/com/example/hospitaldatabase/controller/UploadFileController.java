package com.example.hospitaldatabase.controller;

import com.example.hospitaldatabase.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadFileController {

    private final UploadFileService uploadFileService;

    @Autowired
    public UploadFileController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    @PostMapping("/uploadGeneralInformationFile")
    public String uploadGeneralInformationFile(@RequestBody MultipartFile multipartFile, @RequestParam("hospitalName") String hospitalName, @RequestParam("description") String description) {
        return uploadFileService.uploadFile(multipartFile, description, "GeneralInformation", hospitalName);

    }
}
