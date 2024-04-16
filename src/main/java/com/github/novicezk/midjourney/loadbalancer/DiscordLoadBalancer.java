package com.github.novicezk.midjourney.loadbalancer;


import cn.hutool.core.text.CharSequenceUtil;
import com.github.novicezk.midjourney.loadbalancer.rule.IRule;
import com.github.novicezk.midjourney.support.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DiscordLoadBalancer {
	private final IRule rule;

	private final List<DiscordInstance> instances = Collections.synchronizedList(new ArrayList<>());

	private final HashMap<String,List<DiscordInstance>> instancesByGroup =new HashMap<>();

	public List<DiscordInstance> getAllInstances() {
		return this.instances;
	}

	public List<DiscordInstance> getAliveInstances() {
		return this.instances.stream().filter(DiscordInstance::isAlive).toList();
	}

	public DiscordInstance chooseInstance() {
		return this.rule.choose(getAliveInstances());
	}

	public DiscordInstance getDiscordInstance(String instanceId) {
		if (CharSequenceUtil.isBlank(instanceId)) {
			return null;
		}
		return this.instances.stream()
				.filter(instance -> CharSequenceUtil.equals(instanceId, instance.getInstanceId()))
				.findFirst().orElse(null);
	}

	public Set<String> getQueueTaskIds() {
		Set<String> taskIds = Collections.synchronizedSet(new HashSet<>());
		for (DiscordInstance instance : getAliveInstances()) {
			taskIds.addAll(instance.getRunningFutures().keySet());
		}
		return taskIds;
	}

	public List<Task> getQueueTasks() {
		List<Task> tasks = new ArrayList<>();
		for (DiscordInstance instance : getAliveInstances()) {
			tasks.addAll(instance.getQueueTasks());
		}
		return tasks;
	}

	//按分组的形式来使用discord账号
	public HashMap<String,List<DiscordInstance>> getAllInstancesByGroup() {
		return this.instancesByGroup;
	}

	public DiscordInstance chooseInstance(String groupId) {
		return this.rule.choose(getAliveInstances());
	}


	public DiscordInstance getDiscordInstanceByGroupId(String instanceId) {
		return  this.getDiscordInstanceByGroupId(instanceId,"fast");
	}

	public DiscordInstance getDiscordInstanceByGroupId(String instanceId, String groupId) {
		if (CharSequenceUtil.isBlank(instanceId)||
				CharSequenceUtil.isBlank(groupId)) {
			return null;
		}
		return this.instancesByGroup.get(groupId).stream()
				.filter(instance -> CharSequenceUtil.equals(instanceId, instance.getInstanceId()))
				.findFirst().orElse(null);
	}

}
