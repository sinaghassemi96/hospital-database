package com.example.hospitaldatabase.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ContentFolderService {
   List<Map<String,Object>> getContentFolder(String path) throws IOException;

   String createFolder (String hospitalName) throws IOException;
}
