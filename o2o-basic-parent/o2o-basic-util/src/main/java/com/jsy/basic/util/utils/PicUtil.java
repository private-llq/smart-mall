package com.jsy.basic.util.utils;

import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Description: 判断图片格式
 * @Author: chq459799974
 * @Date: 2020/12/2
 **/
@Slf4j
public class PicUtil {

    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_JPEG = "jpeg";
    public static final String TYPE_JFIF = "jfif";
    public static final String JPG_BYTE = "FFD8FF";
    public static final String TYPE_GIF = "gif";
    public static final String GIF_BYTE = "47494638";
    public static final String TYPE_PNG = "png";
    public static final String PNG_BYTE = "89504E47";
    public static final String TYPE_BMP = "bmp";
    public static final String BMP_TYPE = "424D";
    public static final String TYPE_UNKNOWN = "unknown";

    /**
     * 可用图片格式数组
     */
    public static final String[] AVAILABLE_FORMAT = {TYPE_JPG, TYPE_JPEG, TYPE_BMP, TYPE_GIF, TYPE_PNG,TYPE_JFIF};

    /**
     * 图片文件最大kb
     */
    public static final Integer IMAGE_MAX_SIZE = 5000;


    /**
     * @author YuLF
     * @since 2021/2/22 11:21
     * 图片文件是在指定格式 和 指定大小之内的
     */
    public static void imageQualified(MultipartFile file){
        if( Objects.isNull(file) ){
            throw new JSYException(JSYError.BAD_REQUEST.getCode(),"文件为空");
        }
        String originalFilename = file.getOriginalFilename();
        if( !FilenameUtils.isExtension(originalFilename, AVAILABLE_FORMAT) ){
            throw new JSYException(JSYError.BAD_REQUEST.getCode(), "图片格式没有在可用范围之内!" + Arrays.asList(AVAILABLE_FORMAT));
        }
        long size = file.getSize() / 1024;
        if (size > IMAGE_MAX_SIZE) {
            throw new JSYException(JSYError.REQUEST_PARAM.getCode(), "文件太大了,最大：" + IMAGE_MAX_SIZE + "KB");
        }
    }



    /**
     * byte数组转换成16进制字符串
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件流判断图片类型
     */
    private static String getPicType(FileInputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            int read = fis.read(b, 0, b.length);
            String type = Objects.requireNonNull(bytesToHexString(b)).toUpperCase();
            if (type.contains(JPG_BYTE)) {
                return TYPE_JPG;
            } else if (type.contains(PNG_BYTE)) {
                return TYPE_PNG;
            } else if (type.contains(GIF_BYTE)) {
                return TYPE_GIF;
            } else if (type.contains(BMP_TYPE)) {
                return TYPE_BMP;
            }else{
                return TYPE_UNKNOWN;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    /**
     * 获取图片类型
     */
    public static String getPicType(MultipartFile file){
        if(file == null){
            throw new JSYException(JSYError.BAD_REQUEST.getCode(),"文件为空");
        }
        String fileType;
        File tempFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            fileType = getPicType(new FileInputStream(tempFile));
            if (tempFile.exists()) {
                boolean delete = tempFile.delete();
                if(!delete){
                    log.error("临时文件资源未被清理...");
                    throw new JSYException(JSYError.INTERNAL);
                }
            }
        }catch (IOException e){
            throw new JSYException(JSYError.INTERNAL);
        }
        return fileType;
    }
    
    /**
     * 判断是否是图片(批量)
     */
    public static boolean isPic(MultipartFile[] files){
        if(files == null || files.length == 0){
            throw new JSYException(JSYError.BAD_REQUEST.getCode(),"请至少上传一个文件");
        }
        for(MultipartFile file : files){
            if(!isPic(file)){
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断是否是图片
     */
    public static boolean isPic(MultipartFile file){
        if(file == null){
            throw new JSYException(JSYError.BAD_REQUEST.getCode(),"文件为空");
        }
        String fileType;
        File tempFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            fileType = getPicType(new FileInputStream(tempFile));
        }catch (IOException e){
            throw new JSYException(JSYError.INTERNAL);
        }
        if (tempFile.exists()) {
                boolean delete = tempFile.delete();
            }
            if(TYPE_UNKNOWN.equals(fileType)){
                return false;
            }
        return true;
    }
    
    /**
     * 验证图片大小和格式
     */
    public static boolean checkSizeAndType(MultipartFile file,long kb){
        long size = file.getSize() / 1024;
        if (size > kb) {
            throw new JSYException(JSYError.BAD_REQUEST.getCode(), "文件太大了,最大：" + kb + "KB");
        }
        return isPic(file);
    }
    
    /**
     * 验证图片大小和格式(批量)
     */
    public static boolean checkSizeAndTypeBatch(MultipartFile[] files,long kb){
        if(files == null || files.length == 0){
            throw new JSYException(JSYError.BAD_REQUEST.getCode(),"请至少上传一个文件");
        }
        for(MultipartFile file : files){
           if(!checkSizeAndType(file, kb)){
               return false;
           }
        }
        return true;
    }
    
}
