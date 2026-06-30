package com.haha.blog.admin.service.impl;

import com.haha.blog.admin.domain.vo.file.FileUploadVO;
import com.haha.blog.admin.service.IFileAdminService;
import com.haha.blog.admin.utils.MinioUtil;
import com.haha.blog.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileAdminServiceImpl implements IFileAdminService {

    private final MinioUtil minioUtil;

    @Override
    public FileUploadVO uploadFile(MultipartFile file) {
        try{
            String url = minioUtil.uploadFile(file);
            return FileUploadVO.builder()
                    .url(url)
                    .build();
        }catch(Exception e){
            log.error("==> 上传文件至 Minio 错误: ", e);
            throw new BizException("文件上传失败");
        }
    }
}
