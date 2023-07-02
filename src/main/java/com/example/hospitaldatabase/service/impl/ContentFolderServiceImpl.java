package com.example.hospitaldatabase.service.impl;

import com.example.hospitaldatabase.service.ContentFolderService;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.protocol.commons.EnumWithValue;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import static com.hierynomus.msfscc.FileAttributes.FILE_ATTRIBUTE_NORMAL;

@Service
public class ContentFolderServiceImpl implements ContentFolderService {


    public DiskShare share() throws IOException {
        String sambaDomain = null;
        String sambaUsername = "root";
        String sambaPass = "Admin43";
        String sambaIP = "5.161.143.32";
        String sambaSharedPath = "share";
        @SuppressWarnings("duplicate") SmbConfig cfg = SmbConfig.builder().build();
        SMBClient client = new SMBClient(cfg);
        Connection connection = client.connect(sambaIP);
        Session session = connection.authenticate(new AuthenticationContext(sambaUsername, sambaPass.toCharArray(), sambaDomain));
        return (DiskShare) session.connectShare(sambaSharedPath);
    }

    @Override
    public List<Map<String, Object>> getContentFolder(String path) throws IOException {
        DiskShare diskShare = share();
        List<String> contains = new ArrayList<>();
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> data;
        List<FileIdBothDirectoryInformation> directoryPath = diskShare.list(path);
        Set<FileAttributes> fileAttributes = new HashSet<>();
        fileAttributes.add(FILE_ATTRIBUTE_NORMAL);
        Set<SMB2CreateOptions> createOptions = new HashSet<>();
        createOptions.add(SMB2CreateOptions.FILE_RANDOM_ACCESS);
        for (FileIdBothDirectoryInformation file : directoryPath) {
            if (!file.getFileName().equals(".") && !file.getFileName().equals("..")) {
                if (!EnumWithValue.EnumUtils.isSet(file.getFileAttributes(), FileAttributes.FILE_ATTRIBUTE_DIRECTORY)) {
                    data = new HashMap<>();
                    data.put("type", "file");
                    data.put("name", file.getFileName());
                    data.put("items", null);
                    long nBytes = file.getEndOfFile();
                    double nBytesCalculate;
                    if (nBytes < 1024) {
                        data.put("size", nBytes + "B");
                    } else if (nBytes > 1024 && nBytes < 1048576) {
                        nBytesCalculate = ((double) nBytes) / 1024;
                        DecimalFormat df = new DecimalFormat("#.##");
                        nBytesCalculate = Double.valueOf(df.format(nBytesCalculate));
                        data.put("size", nBytesCalculate + "KB");
                    } else {
                        nBytesCalculate = ((double) nBytes) / 1024 / 1024;
                        DecimalFormat df = new DecimalFormat("#.##");
                        nBytesCalculate = Double.valueOf(df.format(nBytesCalculate));
                        data.put("size", nBytesCalculate + "MB");
                    }
                    if (!contains.contains(file.getFileName())) {
                        contains.add(file.getFileName());
                        ret.add(data);
                    }
                } else {
                    List<FileIdBothDirectoryInformation> dir = diskShare.list(  path + "/" + file.getFileName());
                    data = new HashMap<>();
                    data.put("type", "folder");
                    data.put("size", null);
                    data.put("name", file.getFileName());
                    int count = 0;
                    for (FileIdBothDirectoryInformation item : dir) {
                        if (!item.getFileName().equals(".") && !item.getFileName().equals("..")) {
                            count++;
                        }
                    }
                    data.put("items", count);
                    if (!contains.contains(file.getFileName())) {
                        contains.add(file.getFileName());
                        ret.add(data);
                    }
                }
            }
        }
        diskShare.close();
        return ret;
    }

    @Override
    public String createFolder(String hospitalName) throws IOException {
        try {
            DiskShare diskShare = share();
            diskShare.mkdir( hospitalName);
            List<String> folders = new ArrayList<>(List.of("GeneralInformation", "Architecture", "Structure", "Material", "Geotechnical", "PeriodicInspection"));
            for (String folderName : folders) {
                diskShare.mkdir( hospitalName + "/" + folderName);
            }
            diskShare.close();
            return "Directory created successful";
        }catch (Exception e){
            return "Directory created Unsuccessful";
        }

    }
}
