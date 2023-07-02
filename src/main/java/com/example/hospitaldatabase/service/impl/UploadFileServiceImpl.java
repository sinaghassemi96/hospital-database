package com.example.hospitaldatabase.service.impl;


import com.example.hospitaldatabase.component.MessageComponent;
import com.example.hospitaldatabase.entity.Log;
import com.example.hospitaldatabase.repository.LogRepository;
import com.example.hospitaldatabase.service.UploadFileService;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.hierynomus.msfscc.FileAttributes.FILE_ATTRIBUTE_NORMAL;
import static com.hierynomus.mssmb2.SMB2CreateDisposition.FILE_OVERWRITE_IF;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private final LogRepository logRepository;

    private final MessageComponent messageComponent;

    @Autowired
    public UploadFileServiceImpl(LogRepository logRepository, MessageComponent messageComponent) {
        this.logRepository = logRepository;
        this.messageComponent = messageComponent;
    }

    private DiskShare share() throws IOException {
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
    public String uploadFile(MultipartFile multipartFile, String description, String grouping, String hospitalName) {
        try {
            DiskShare diskShare = share();
            Set<FileAttributes> fileAttributes = new HashSet<>();
            fileAttributes.add(FILE_ATTRIBUTE_NORMAL);
            Set<SMB2CreateOptions> createOptions = new HashSet<>();
            createOptions.add(SMB2CreateOptions.FILE_RANDOM_ACCESS);
            com.hierynomus.smbj.share.File f = diskShare.openFile(hospitalName + "/" + grouping + "/" + multipartFile.getOriginalFilename(), new HashSet<>(List.of(AccessMask.GENERIC_ALL)), fileAttributes, SMB2ShareAccess.ALL, FILE_OVERWRITE_IF, createOptions);

            OutputStream oStream = f.getOutputStream();
            oStream.write(multipartFile.getBytes());
            oStream.flush();
            oStream.close();
            diskShare.close();
            Log log = new Log();
            log.setFileName(multipartFile.getOriginalFilename());
            log.setDescription(description);
            log.setData(LocalDate.now());
            log.setGrouping(grouping);
            logRepository.save(log);
            return messageComponent.getMessage("file.upload.message", new Object[]{multipartFile.getOriginalFilename()});
        } catch (Exception e) {
            return messageComponent.getMessage("file.upload.error", new Object[]{multipartFile.getOriginalFilename()});
        }
    }
}
