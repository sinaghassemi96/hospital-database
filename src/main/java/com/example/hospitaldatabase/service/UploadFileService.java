package com.example.hospitaldatabase.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

    public String uploadFile(MultipartFile multipartFile,String description,String grouping,String hospitalName);

}
