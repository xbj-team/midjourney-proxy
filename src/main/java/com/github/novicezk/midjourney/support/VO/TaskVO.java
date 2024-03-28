package com.github.novicezk.midjourney.support.VO;

import com.github.novicezk.midjourney.support.Task;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
public class TaskVO {
    private String id;
    private int flags;
    private List<String> descriptions;
    private String content;
    private String hash;
    private String progress;
    private String uri;
    private String proxy_url;
    private String options;

    public static TaskVO bulid(Task task) {
        log.info("TaskVO.bulid->task:{}",task);
        TaskVO taskVO=new TaskVO();
        try {
            String messageId = task.getProperty("messageId", String.class,"");
            Integer flags = task.getProperty("flags", Integer.class,-1);
            String hash = task.getProperty("messageHash", String.class,"");
            String proxy_url = task.getProperty("imageProxyUrl", String.class,"");
            String content = task.getProperty("finalPrompt", String.class,"");
            String options = task.getProperty("options", String.class,"");
            String progress = task.getProgress();
            String uri = task.getImageUrl();
            taskVO.setId(messageId);
            taskVO.setFlags(flags);
            taskVO.setProxy_url(proxy_url);
            taskVO.setUri(uri);
            taskVO.setDescriptions(Strings.isNotBlank(content) ? Arrays.asList(content.split("\n\n")) : null);
            taskVO.setContent(content);
            taskVO.setHash(hash);
            taskVO.setProgress(progress);
            taskVO.setOptions(options);
        }catch (Exception exception){
            log.info("TaskVO.bulid:{}",exception);
        }
        log.info("TaskVO.bulid->taskVO:{}",taskVO);
        return taskVO;
    }
}
