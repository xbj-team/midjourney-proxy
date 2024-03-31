package com.github.novicezk.midjourney.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "任务回调测试")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
public class TaskCallbackController {

    @ApiOperation(value = "任务回调")
    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody Object body) {
        log.info("callback-source,{}", body.toString());
        String jsonString = JSONObject.toJSONString(body);
        log.info("callback-source-json,{}",jsonString);
//        MjDescriptionCallback mjDescriptionCallback = JSONObject.parseObject(jsonString, MjDescriptionCallback.class);
//        log.info("callback-transfer,{}", JSONObject.toJSONString(mjDescriptionCallback));
        ResponseEntity.BodyBuilder status = ResponseEntity.status(2);
        return status.build();
    }

}
