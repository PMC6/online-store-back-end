/**
 * @author： fanzhonghao
 * @date: 18-12-16 15 27
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.service;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class ImgService {

    /**
     * 将base64数据转为图片，并把图片存储，将存储路径返回
     * @param image 传输过来的json数据
     * @return
     * @throws Exception
     */
    public String storeImg(String image) throws Exception{
        //base64 img 数据
        String baseUrl = "/home/fan/Pictures/"; //需修改
        String imgUrl = null;
        long time = new Date().getTime();
        String imgType = ".png";

        //去掉 'data:image/xxx;base64,'
        String[] type = image.split(";");
        imgType = "." + type[0].split("/")[1];
        image = image.substring(image.split(",")[0].length()+1);
        System.out.println("img type " + imgType);

        imgUrl = baseUrl + time + imgType;

        BASE64Decoder decoder = new BASE64Decoder();

        byte[] b = decoder.decodeBuffer(image);

        for (int i = 0;i < b.length;i++){
            if (b[i] < 0){
                b[i] += 256;
            }
        }

        OutputStream out = new FileOutputStream(imgUrl);
        out.write(b);
        out.flush();
        out.close();
        return imgUrl;
    }
}

