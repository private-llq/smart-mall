package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.FileClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@FeignClient(value = "shop-service-file",fallback = FileClientImpl.class,configuration = FeignConfiguration.class)
public interface FileClient {

    @PostMapping("/uploadFile")
    CommonResult uploadFile(MultipartFile file);

    @GetMapping("/removeFile/{id}")
    CommonResult removeFile(@PathVariable("id") Long id);

    @GetMapping("/downLoadFile/{id}")
    CommonResult downLoadFile(@PathVariable("id") Long id, HttpServletResponse response);

    @GetMapping("/picture/{id}")
    CommonResult showPic(@PathVariable("id") Long id);

    @PostMapping("/fileInfo/pub/pictureUrl")
    Map<String,String> getPicUrl(@RequestBody List<String> picList);

    @ApiOperation("上传文件")
    @PostMapping(value = "/fileInfo/uploadFile2",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CommonResult<String> uploadFile2(MultipartFile file);
}
