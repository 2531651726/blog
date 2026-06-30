package com.haha.blog.admin.service;

import com.haha.blog.admin.domain.vo.file.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

public interface IFileAdminService {
    FileUploadVO uploadFile(MultipartFile file);
}
