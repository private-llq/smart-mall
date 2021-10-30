package com.jsy.utils;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.domain.FileInfo;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.policy.PolicyType;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * 文件服务访问路径 服务器地址/端口号/存储桶/文件名
 * eg:http://222.178.212.29:9000/2020-12-08/05a4358f-d37d-4ea5-92c3-edee92145095-shop.jpg
 */
public class MinioUtil {
    //ip
    private static final String ENDPOINT = "http://222.178.212.29";
    //端口
    private static final int PROT = 9000;
    //ACCESS_KEY
    private static final String ACCESSKEY = "minio"; //用户名
    //SECRET_KEY
    private static final String SECRETKET = "minimini";//密码
    //(+shop/)存储桶名称
    private static String BUCKETNAME = null;

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
            // 存储桶
            BUCKETNAME = String.valueOf(LocalDate.now());// 当前年月日
            //存入bucket不存在则创建
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
}
