package com.example.bysj3.Util;

import com.example.bysj3.Plus.PlusActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

//PlusActivity 上传数据库功能
public class HttpData {

    public static String AddData(String title,String content,String user,String user_id,String user_image,String image,String image2,String image3,String image4,String image5,String image6,String image7,String image8,String image9){

        String address = "http://39.97.251.81:8080/test5/AddDataServlet";
        String result = "";

        try{
            URL url = new URL(address);//初始化URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//请求方式

            //超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);

            //我们请求的数据
            String data = "title="+ URLEncoder.encode(title,"UTF-8")
                    +"&content="+ URLEncoder.encode(content,"UTF-8")
                    +"&user="+ URLEncoder.encode(user,"UTF-8")
                    +"&user_id="+ URLEncoder.encode(user_id,"UTF-8")
                    +"&user_image="+ URLEncoder.encode(user_image,"UTF-8")
                    +"&image="+ URLEncoder.encode(image,"UTF-8")
                    +"&image2="+ URLEncoder.encode(image2,"UTF-8")
                    +"&image3="+ URLEncoder.encode(image3,"UTF-8")
                    +"&image4="+ URLEncoder.encode(image4,"UTF-8")
                    +"&image5="+ URLEncoder.encode(image5,"UTF-8")
                    +"&image6="+ URLEncoder.encode(image6,"UTF-8")
                    +"&image7="+ URLEncoder.encode(image7,"UTF-8")
                    +"&image8="+ URLEncoder.encode(image8,"UTF-8")
                    +"&image9="+ URLEncoder.encode(image9,"UTF-8")
                    ;
            //获取输出流
            OutputStream out = conn.getOutputStream();

            out.write(data.getBytes("UTF-8"));
            out.flush();
            out.close();
            conn.connect();

            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                result = new String(message.toByteArray());
                //return result;
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return result;

    }
}

