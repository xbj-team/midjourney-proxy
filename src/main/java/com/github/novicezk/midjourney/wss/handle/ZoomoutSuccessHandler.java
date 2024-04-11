package com.github.novicezk.midjourney.wss.handle;

import com.github.novicezk.midjourney.enums.MessageType;
import com.github.novicezk.midjourney.enums.TaskAction;
import com.github.novicezk.midjourney.loadbalancer.DiscordInstance;
import com.github.novicezk.midjourney.support.TaskCondition;
import com.github.novicezk.midjourney.util.ContentParseData;
import com.github.novicezk.midjourney.util.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * variation消息处理.
 * 完成(create): **cat** - Variations (Strong\Subtle\Region等) by <@1012983546824114217> (relaxed)
 * 完成(create): **cat** - Variations by <@1012983546824114217> (relaxed)
 */
@Component
@Slf4j
public class ZoomoutSuccessHandler extends MessageHandler {
	private static final String CONTENT_REGEX_1 = "\\*\\*(.*?)\\*\\* - Zoom Out by <@\\d+> \\((.*?)\\)";
	private static final String CONTENT_REGEX_2 = "\\*\\*(.*?)\\*\\* - Zoom Out \\(.*?\\) by <@\\d+> \\((.*?)\\)";

	@Override
	public void handle(DiscordInstance instance, MessageType messageType, DataObject message) {
		log.info("zoomout回调：{}", JSONObject.valueToString(message));
		String content = getMessageContent(message);
		log.info("zoomout-content回调：{}", content);
		ContentParseData parseData = getParseData(content);
		log.info("zoomout-parseData回调：{}", JSONObject.valueToString(parseData));
		if (MessageType.CREATE.equals(messageType) && parseData != null && hasImage(message)) {
			TaskCondition condition = new TaskCondition()
					.setActionSet(Set.of(TaskAction.ZOOMOUT))
					.setFinalPromptEn(parseData.getPrompt());
			findAndFinishImageTask(instance, condition, parseData.getPrompt(), message);
		}
	}

	private ContentParseData getParseData(String content) {
		ContentParseData parseData = ConvertUtils.parseContent(content, CONTENT_REGEX_1);
		if (parseData == null) {
			parseData = ConvertUtils.parseContent(content, CONTENT_REGEX_2);
		}
		return parseData;
	}

}
