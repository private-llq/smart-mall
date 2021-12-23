package com.jsy.utils;
import com.alibaba.fastjson.JSONObject;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.SnowFlake;
import com.jsy.domain.FileInfo;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.policy.PolicyType;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 文件服务访问路径 服务器地址/端口号/存储桶/文件名
 * eg:http://222.178.212.29:9000/2020-12-08/05a4358f-d37d-4ea5-92c3-edee92145095-shop.jpg
 */

public class MinioUtil {

    private static final String ENDPOINT = "http://222.178.212.29";//ip

    private static final int PROT = 9000;//端口

    private static final String ACCESSKEY = "minio";  //ACCESS_KEY 用户名

    private static final String SECRETKET = "minimini"; //SECRET_KEY 密码

    private static final String BUCKETNAME = "mall";//存储桶名称


    /**
     *  删除数据同步删除文件
     * @param path 文件地址
     */
    public static void delFile(String path){
        if (Objects.nonNull(path)){
            String[] split = path.split("/");
            String objectName=split[split.length-1];
            String bucketName="mall";
            removeFile(bucketName,objectName);
        }
    }


    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的文件名称
     * @return
     * @throws Exception
     */
    public static void removeFile(String bucketName, String objectName){
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, PROT, ACCESSKEY, SECRETKET);
            ObjectStat objectStat = minioClient.statObject(bucketName, objectName);
            if (objectStat != null) {
                minioClient.removeObject(bucketName, objectName);
            } else {
                throw new JSYException(-1,"删除文件不存在!");
            }
        }catch (Exception e){
            throw new JSYException(-1,"文件删除失败!");
        }

    }




    /**
     * 文件上传
     * @param file
     * @return
     * @throws Exception
     */
    public static FileInfo upload(MultipartFile file){
        FileInfo fileInfo=null;
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, PROT, ACCESSKEY, SECRETKET);
            if (!minioClient.bucketExists(BUCKETNAME)) {
                minioClient.makeBucket(BUCKETNAME);

                //给一个存储桶+文件对象前缀 设置策略。
                minioClient.setBucketPolicy(BUCKETNAME,"*", PolicyType.READ_WRITE);//存储桶名称  文件对象前缀 存储策略：读写
            }
            String fileName = file.getOriginalFilename();
            String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
            // 文件存储的目录结构
            String uuid = UUID.randomUUID().toString();
            String objectName = uuid +"-"+ fileName;
            // 存储文件
            minioClient.putObject(BUCKETNAME, objectName, file.getInputStream(), file.getContentType());
            String filePath =BUCKETNAME + "/" + objectName;//文件路径就是 桶名/文件名
            fileInfo=new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setDownloadUrl(filePath);
            fileInfo.setSize(file.getBytes().length);
            fileInfo.setMd5Hash(digest("MD5",file.getBytes()));
            fileInfo.setFileType(FilePathGenerator.FileType.getFileTypeByExtension(prefix).getFtype());//
            fileInfo.setTags(prefix);
            fileInfo.setValidStatus(0);
        }catch (Exception e){
            throw new JSYException(-1,"上传失败");
        }
        return fileInfo;
    }

    /**
     * 文件上传2.0
     * @param file
     * @return
     * @throws Exception
     */
    public static String uploadGetUrl(MultipartFile file){
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, PROT, ACCESSKEY, SECRETKET);
            if (!minioClient.bucketExists(BUCKETNAME)) {
                minioClient.makeBucket(BUCKETNAME);

                //给一个存储桶+文件对象前缀 设置策略。
                minioClient.setBucketPolicy(BUCKETNAME,"*", PolicyType.READ_WRITE);//存储桶名称  文件对象前缀 存储策略：读写
            }
            String fileName = file.getOriginalFilename();
            String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
            // 文件存储的目录结构
            String uuid = String.valueOf(SnowFlake.nextId());
            String replaceAll = prefix.replaceAll(",", "");
            String objectName=uuid+"."+replaceAll;
            // 存储文件
            minioClient.putObject(BUCKETNAME, objectName, file.getInputStream(), file.getContentType());
            String filePath =BUCKETNAME + "/" + objectName;//文件路径就是 桶名/文件名
            String download_url=ENDPOINT+":"+PROT+"/"+filePath;//下载地址
            return download_url;
        }catch (Exception e){
            throw new JSYException(-1,"上传失败");
        }
    }


    /**
     * 下载文件
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的文件名称
     * @return
     * @throws Exception
     */
    public static InputStream getFile(String bucketName, String objectName) {
        InputStream inputStream=null;
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, PROT, ACCESSKEY, SECRETKET);
            ObjectStat objectStat = minioClient.statObject(bucketName, objectName);
            if (objectStat != null) {
               inputStream = minioClient.getObject(bucketName, objectName);
            } else {
                throw new JSYException(-1,"文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSYException(-1,"下载失败");
        }
        return inputStream;
    }
    /****************************************批量上传*********************************************************************/
    public static Map<Object, String> uploadGetUrls(MultipartFile[] file) {
        Map<Object,String> map = new HashMap<>();
        String s=null;
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, PROT, ACCESSKEY, SECRETKET);
            //存入bucket不存在则创建，并设置为只读
            if (!minioClient.bucketExists(BUCKETNAME)) {
                minioClient.makeBucket(BUCKETNAME);
                //给一个存储桶+文件对象前缀 设置策略。
                minioClient.setBucketPolicy(BUCKETNAME,"*", PolicyType.READ_WRITE);//存储桶名称  文件对象前缀 存储策略：读写
            }
            for(int i=0;i<file.length;i++){
                String fileName = file[i].getOriginalFilename();
                String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
                // 文件存储的目录结构
                String uuid = UUID.randomUUID().toString();
                String objectNameTemp = uuid +"."+ prefix.replaceAll(",","");
                String objectName=objectNameTemp;

                // 存储文件
                minioClient.putObject(BUCKETNAME, objectName, file[i].getInputStream(), file[i].getContentType());
                String filePath =BUCKETNAME + "/" + objectName;//文件路径就是 桶名/文件名
                String download_url=ENDPOINT+":"+PROT+"/"+filePath;//下载地址
                map.put("url"+i,download_url);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /*文件加密*/
    private static String digest(String algorithm, byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance(algorithm);
            digest.update(bytes);
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","张三");
        jsonObject.put("age",25);
        jsonObject.put("hobby",new String[]{"蓝球","排球","网球"});
        jsonObject.put("list", Arrays.asList("java","springboot",2));
        System.out.println(jsonObject.toJSONString());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name","张三");
        hashMap.put("age",25);
        hashMap.put("hobby",new String[]{"蓝球","排球","网球"});
        System.out.println(new JSONObject(hashMap).toJSONString());

    }
}
