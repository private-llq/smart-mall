package com.jsy.client.impl;

import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.FileClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class FileClientImpl implements FileClient {
    @Override
    public CommonResult uploadFile(MultipartFile file) {
        return null;
    }

    @Override
    public CommonResult removeFile(Long id) {
        return null;
    }

    @Override
    public CommonResult downLoadFile(Long id, HttpServletResponse response) {
        return null;
    }

    @Override
    public CommonResult showPic(Long id) {
        return null;
    }

    @Override
    public Map<String, String> getPicUrl(List<String> picList) {
        return null;
    }

    @Override
    public CommonResult<String> uploadFile2(MultipartFile file) {
        return null;
    }

}
