package com.github.novicezk.midjourney.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class DingTalkMessage  {
    public void sendTextMessage(String content) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("msgtype", "markdown");

        JSONObject markdown = new JSONObject();
        markdown.put("title", "mj剩余量通知");
        markdown.put("text", content);

        json.put("markdown", markdown);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toJSONString());

        Request request = new Request.Builder()
                .url("https://oapi.dingtalk.com/robot/send?access_token=d91eb776767181b357cbd07dbca8478389606ffcf1d8294cbcb989584558fbd1")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
