package com.jsy.controller;

import com.jsy.basic.util.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RequestMapping("/upload")
@Api(tags = "文件上传")
@RestController
public class UploadController {

    @Value("${filePath}")
    private String filePath;

    @PostMapping("signFile")
    @ApiOperation("单文件上传")
    public CommonResult signFile(@RequestParam("file") MultipartFile file) {
        //获取文件名称
        String filename = file.getOriginalFilename();
        //截取后缀
        String suffixName = filename.substring((filename.lastIndexOf("."))+1);
        //获取uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        //根据每天创建一个文件夹
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dir = format.format(new Date());

        //文件路径
        String path = filePath + dir + "\\" ;
        String outfielName = uuid + "." + suffixName;

        HashMap<String, String> map = new HashMap<>();
        map.put("url",path+outfielName);

        File file1 = new File(path,outfielName);
        if (!file1.getParentFile().exists()) {
            file1.getParentFile().mkdir();
        }
        try {
            file.transferTo(file1);
            return CommonResult.ok(map);
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.error(-1,"上传失败");
        }

    }

}
