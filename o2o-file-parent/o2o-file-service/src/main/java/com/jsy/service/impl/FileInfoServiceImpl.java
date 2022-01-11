package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.enums.contentTypeEnum;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.domain.FileInfo;
import com.jsy.mapper.FileInfoMapper;
import com.jsy.service.IFileInfoService;
import com.jsy.utils.MinioUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * <p>
 * 附件资源日志 服务实现类
 * </p>
 *
 * @author lijin
 * @since 2020-11-27
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {
    /**
     * 图片文件最大kb
     */
    public static final Integer IMAGE_MAX_SIZE = 2000;
    public static final String TYPE = "shop_logo";
    public static final Integer SHOP_IMAGE_MAX_SIZE = 1024;

    @Override
    public String uploadFile(MultipartFile file) {
        FileInfo fileInfo= MinioUtil.upload(file);
        fileInfo.setDescription("电商文件");
        fileInfo.setUuid(UUIDUtils.getUUID());
        baseMapper.insert(fileInfo);
        return fileInfo.getUuid(); //返回主键
    }

    @Override
    public void deleteFile(String uuid) {
        FileInfo fileInfo=selectByUuid(uuid);
        if(Objects.nonNull(fileInfo)){
            Integer index=fileInfo.getDownloadUrl().lastIndexOf("/");
            MinioUtil.removeFile(fileInfo.getDownloadUrl().substring(0,index),fileInfo.getDownloadUrl().substring(index+1));
            baseMapper.delete(new QueryWrapper<FileInfo>().eq("uuid",uuid));
        }else {
            throw new JSYException(-1,"文件不存在");
        }
    }

    private FileInfo selectByUuid(String uuid) {
        return baseMapper.selectOne(new QueryWrapper<FileInfo>().eq("uuid",uuid));
    }

    @Override
    public void downLoadFile(String uuid, HttpServletResponse response) throws UnsupportedEncodingException {
        FileInfo fileInfo=selectByUuid(uuid);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            if(Objects.isNull(fileInfo)){
                outputStream.write("文件不存在".getBytes());
            }
            if("img".equals(fileInfo.getFileType())){
                response.setContentType(contentTypeEnum.getType(fileInfo.getTags())+"; charset=ISO8859-1");
            }else {
                response.setContentType("application/octet-stream; charset=ISO8859-1");
            }
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileInfo.getFileName()).getBytes("utf-8"),"ISO8859-1"));
            //这个路径为待下载文件的路径
            Integer index=fileInfo.getDownloadUrl().lastIndexOf("/");
            outputStream.write(MinioUtil.getFile(fileInfo.getDownloadUrl().substring(0,index),fileInfo.getDownloadUrl().substring(index+1)).readAllBytes());
            outputStream.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String printPicBase64(String uuid) {
        FileInfo fileInfo=selectByUuid(uuid);
        if(Objects.isNull(fileInfo)){
            throw new JSYException(-1,"图片不存在");
        }
        if (!"img".equals(fileInfo.getFileType())){
            throw new JSYException(-1,"该文件非图片");
        }
        byte[] data = null;

        // 读取图片字节数组
        try { Integer index=fileInfo.getDownloadUrl().lastIndexOf("/");
            InputStream in = MinioUtil.getFile(fileInfo.getDownloadUrl().substring(0,index),fileInfo.getDownloadUrl().substring(index+1));
            data = in.readAllBytes();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64 base64 = new Base64();
        // 对字节数组Base64编码
        return "data:image/"+fileInfo.getTags()+";base64,"+base64.encodeToString(data);// 返回Base64编码过的字节数组字符串
    }

    @Override
    public Map<String, String> getUrlByList(List<String> picList) {
        if (picList==null ||picList.size()==0){
            return null;
        }

        List<FileInfo> fileInfos=super.list(new QueryWrapper<FileInfo>().in("uuid",picList));

        System.out.println(fileInfos);


        Map<String,String> map=new HashMap<>();

        fileInfos.forEach(x->{
            map.put(x.getUuid(),x.getDownloadUrl());
        });

        return map;
    }

    /**********************************************************2.0************************************************/

    /**
     * 上传并获取路径
     * @param file
     * @return
     */
    @Override
    public String uploadFile2(MultipartFile file) {
        //判断图片大小
        long size = file.getSize() / 1024;
        System.out.println(size);
        if (size > IMAGE_MAX_SIZE) {
            throw new JSYException(-1, "文件太大了,最大：" + IMAGE_MAX_SIZE + "KB");
        }
       return MinioUtil.uploadGetUrl(file);
    }

    @Override
    public Map<Object, String> uploadGetUrls(MultipartFile[] file) {
        return MinioUtil.uploadGetUrls(file);
    }
//限制大小
    @Override
    public String uploadFile3(MultipartFile file, String type) {
        long size = file.getSize() / 1024;
        if (type.equals(TYPE)){
            //判断图片大小
            if (size > SHOP_IMAGE_MAX_SIZE) {
                throw new JSYException(-1, "文件太大了,最大：" + SHOP_IMAGE_MAX_SIZE + "KB");
            }
        }
        System.out.println(size);
        return null;
    }


}
