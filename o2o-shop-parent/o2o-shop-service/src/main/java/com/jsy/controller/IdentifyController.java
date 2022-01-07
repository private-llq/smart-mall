package com.jsy.controller;

import com.jsy.basic.util.utils.Base64Util;
import com.jsy.basic.util.utils.PicUtil;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.FileClient;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/identify")
public class IdentifyController {
    @Autowired
    private PicContentUtil picContentUtil;
    @Autowired
    private FileClient fileClient;
    @ApiOperation("身份证照片识别")
    @PostMapping("idCard/distinguish")
    // @Permit("community:proprietor:user:idCard:distinguish")
    public CommonResult distinguishIdCard(MultipartFile file, @RequestParam String type){
        PicUtil.imageQualified(file);
        if(PicContentUtil.ID_CARD_PIC_SIDE_FACE.equals(type) || PicContentUtil.ID_CARD_PIC_SIDE_BACK.equals(type)){
            Map<String, Object> returnMap = picContentUtil.getIdCardPicContent(Base64Util.fileToBase64Str(file), type);
            if(returnMap != null){
                //上传图片
                String data = fileClient.uploadFile2(file).getData();
                returnMap.put("file",data);
                System.out.println("识别结果"+returnMap);
                return CommonResult.ok(returnMap);
            }else{
                return CommonResult.error(-1,"识别失败");
            }
        }else{
            return CommonResult.error(-1,"缺少身份证正反面参数");
        }
    }
    @ApiOperation("营业执照识别")
    @PostMapping("idCard/business")
    @Deprecated
    // @Permit("community:proprietor:user:idCard:distinguish")
    public CommonResult getBusiness(MultipartFile file){
        PicUtil.imageQualified(file);
        Map<String, Object> returnMap = picContentUtil.getBusinessContent(Base64Util.fileToBase64Str(file));
        if(returnMap != null){
            //上传图片
            String data = fileClient.uploadFile2(file).getData();
            returnMap.put("file",data);
            System.out.println("识别结果"+returnMap);
            return CommonResult.ok(returnMap);
        }else{
            return CommonResult.error(-1,"识别失败");
        }
    }
}
