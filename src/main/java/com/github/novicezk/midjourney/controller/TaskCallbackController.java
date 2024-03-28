package com.github.novicezk.midjourney.controller;

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
        log.info("callback,{}", body.toString());
        ResponseEntity.BodyBuilder status = ResponseEntity.status(2);
        return status.build();
    }

}
