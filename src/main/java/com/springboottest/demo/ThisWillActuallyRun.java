package com.springboottest.demo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;

@RestController
class ThisWillActuallyRun {

    @RequestMapping("/test/")
    String home11() {
        return "hello";
    }

    @RequestMapping("/")
    String home(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request) {

        System.out.println("时间=" + System.currentTimeMillis());
        String question = "";
        String userIdStr = "";
        ServletInputStream is = null;
        try {
            is = request.getInputStream();
            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            userIdStr = sb.substring(sb.indexOf("<FromUserName><![CDATA[") + "<FromUserName><![CDATA[".length());
            userIdStr = userIdStr.substring(0, userIdStr.indexOf("]]></FromUserName>"));


            String substring = sb.substring(sb.lastIndexOf("<![CDATA[") + 9);
            substring = substring.substring(0, substring.indexOf("]]></Content>"));
            question = substring;
            System.out.println("sb = " + substring);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
//        timestamp	时间戳
//        nonce	随机数
//        echostr	随机字符串

        String[] strs = {"abc", timestamp, nonce};
        String jmmmmm = "";
        Arrays.sort(strs);
        String bbb = strs[0] + strs[1] + strs[2];
        try {
            MessageDigest md = MessageDigest.getInstance("sha1");
            byte[] digest = md.digest(bbb.getBytes());
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();

            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            jmmmmm = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("jmmmmm = " + jmmmmm);
        System.out.println("signature = " + signature);

        String app_id = "2130205298";
        String nonce_str = "fa577ce340859f9fe";
//          question = substring;
        String session = "10000";
        String time_stamp = System.currentTimeMillis() / 1000 + "";
        String app_key = "2HhPcV769LyKg8lf";

        String msgurlbig = null;
        try {
            msgurlbig = URLEncoder.encode(question, "UTF-8").toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String paramStr = "app_id=" + app_id + "&nonce_str=" + nonce_str + "&question=" + msgurlbig + "&session=" + session + "&time_stamp=" + time_stamp;
        String s = Md5Util.md5(paramStr + "&app_key=" + app_key);
        s = s.toUpperCase();


        String url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";

        String sr = HttpRequest.sendGet(url, paramStr + "&sign=" + s);
        System.out.println(url + "?" + paramStr + "&sign=" + s);
        JSONObject json = JSONObject.parseObject(sr);
        String string = json.getJSONObject("data").getString("answer");
        System.out.println("string = " + string);
//        return echostr;

        //oQ070vw4mQcbuPUH1Dm0H2Yqzfj0
        return "<xml><ToUserName><![CDATA[" + userIdStr + "]]></ToUserName>\n" +
                "<FromUserName><![CDATA[gh_e926437d410f]]></FromUserName>\n" +
                "<CreateTime>1584252961</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[" + string + "]]></Content>\n" +
                "<MsgId>22681049739928835</MsgId>\n" +
                "</xml>";
    }

}