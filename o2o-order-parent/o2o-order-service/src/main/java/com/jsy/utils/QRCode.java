package com.jsy.utils;



import com.esotericsoftware.minlog.Log;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @className（类名称）: QRCode
 * @description（类描述）: this is the QRCode class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2022/1/4
 * @version（版本）: v1.0
 */
@Data
public class QRCode {

    public static void main(String[] args) {
        try {
            BufferedImage image = QrCodeUtils.createImage("8888888", null, true);

            //创建一个ByteArrayOutputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //把BufferedImage写入ByteArrayOutputStream
            ImageIO.write(image, "jpg", os);
            //ByteArrayOutputStream转成InputStream
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            //InputStream转成MultipartFile
            MultipartFile multipartFile =new MockMultipartFile("file", "file.jpg", "text/plain", input);


            System.out.println(image.toString()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
