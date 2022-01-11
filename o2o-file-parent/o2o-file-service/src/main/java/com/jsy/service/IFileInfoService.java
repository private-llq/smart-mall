package com.jsy.service;

import com.jsy.domain.FileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件资源日志 服务类
 * </p>
 *
 * @author lijin
 * @since 2020-11-27
 */
public interface IFileInfoService extends IService<FileInfo> {

    String uploadFile(MultipartFile file);

    void deleteFile(String uuid);

    void downLoadFile(String uuid, HttpServletResponse response) throws UnsupportedEncodingException;

    String printPicBase64(String uuid);

    Map<String, String> getUrlByList(List<String> picList);

    String uploadFile2(MultipartFile file);
//批量上传
    Map<Object, String> uploadGetUrls(MultipartFile[] file);
//限制大小
    String uploadFile3(MultipartFile file, String type);
}
