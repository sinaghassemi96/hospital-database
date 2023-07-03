package com.example.hospitaldatabase.service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ContentFolderService {
   List<Map<String,Object>> getContentFolder(String path) throws IOException;

   String createFolder (String hospitalName) throws IOException;

   Resource download() throws IOException;
}
