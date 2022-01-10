package com.jsy.controller;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.service.IFileInfoService;
import com.zhsj.baseweb.annotation.LoginIgnore;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/fileInfo")
public class FileInfoController {
    @Autowired
    public IFileInfoService fileInfoService;

    @ApiOperation("上传文件")
    @PostMapping("/pub/uploadFile")
    public CommonResult uploadFile(MultipartFile file) {
        return CommonResult.ok(fileInfoService.uploadFile(file));
    }

    @ApiOperation("删除文件")
    @GetMapping("/removeFile/{uuid}")
    public CommonResult removeFile(@PathVariable String uuid) {
        fileInfoService.deleteFile(uuid);
        return CommonResult.ok();
    }

    @ApiOperation("下载文件")
    @GetMapping("/pub/downLoadFile/{uuid}")
    public CommonResult downLoadFile(@PathVariable String uuid, HttpServletResponse response) throws UnsupportedEncodingException {
        fileInfoService.downLoadFile(uuid,response);
        return CommonResult.ok();
    }

    @ApiOperation(value = "1.02 图片显示接口，返回Base64编码", httpMethod = "GET", notes = "1.02 图片显示接口，返回Base64编码")
    @GetMapping("/pub/picture/{uuid}")
    public CommonResult showPic(@PathVariable String uuid) {
        return CommonResult.ok(fileInfoService.printPicBase64(uuid));
    }

    @PostMapping("/pub/pictureUrl")
    public Map<String,String> getPicUrl(@RequestBody List<String> picList){
        return fileInfoService.getUrlByList(picList);
    }


    @ApiOperation(value = "上传文件并得到url",httpMethod = "POST")
    @PostMapping("/pub/getPictureUrl/{uuid}")
    public CommonResult<String> getPictureUrl(MultipartFile file) {
        String s = fileInfoService.uploadFile(file);
        List<String> strings=new ArrayList<>();
        strings.add(s);
        Map<String, String> urlByList = fileInfoService.getUrlByList(strings);
        String s1 = urlByList.get(s);
        return new CommonResult<String>(200,"上传成功","http://222.178.212.29:9000/"+s1) ;
    }

    @ApiOperation(value = "上传文件并得到url",httpMethod = "POST")
    @PostMapping("/pub/getPicUrlString")
    public CommonResult<String> getPicUrlString(@RequestParam("ids") String ids) {
        String[] split = ids.split(",");
        List<String> collect =  Arrays.stream(split).collect(Collectors.toList());

        Map<String, String> picUrl = this.getPicUrl(collect);

        ArrayList<String> objects = new ArrayList<>();
        for (String key : picUrl.keySet()) {
           objects.add(picUrl.get(key));
        }
        String join = StringUtils.join(objects.toArray(), ";");
        return CommonResult.ok(join);
    }

    /********************************************2.0*****************************************************************/


    @ApiOperation("上传文件")
    @PostMapping("/uploadFile2")
    @LoginIgnore
    public CommonResult<String> uploadFile2(MultipartFile file) {
       String path= fileInfoService.uploadFile2(file);
       return CommonResult.ok(path);
    }

    @ApiOperation("批量上传文件")
    @PostMapping("/uploadGetUrls")
    public CommonResult<Map<Object, String>> uploadGetUrls(MultipartFile[] file) {
        Map<Object, String> path= fileInfoService.uploadGetUrls(file);
        return CommonResult.ok(path);
    }





}
