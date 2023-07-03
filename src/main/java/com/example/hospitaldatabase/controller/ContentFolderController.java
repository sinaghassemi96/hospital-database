package com.example.hospitaldatabase.controller;

import com.example.hospitaldatabase.service.ContentFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("contentFolder")
public class ContentFolderController {
    private final ContentFolderService contentFolderService;

    @Autowired
    public ContentFolderController(ContentFolderService contentFolderService) {
        this.contentFolderService = contentFolderService;
    }

    @GetMapping("/getContentFolder")
    public List<Map<String, Object>> getContentFolder(@RequestParam("path") String path) throws IOException {
        return contentFolderService.getContentFolder(path);
    }

    @GetMapping("/createFolder")
    public String createFolder(@RequestParam("hospitalName") String hospitalName) throws IOException {
        return contentFolderService.createFolder(hospitalName);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> dwonload() throws IOException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(contentFolderService.download());
    }
}
