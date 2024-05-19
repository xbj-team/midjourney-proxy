package com.github.novicezk.midjourney.support;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.novicezk.midjourney.domain.DiscordAccount;
import com.github.novicezk.midjourney.loadbalancer.DiscordInstance;
import com.github.novicezk.midjourney.loadbalancer.DiscordLoadBalancer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MJCommandInitializer implements ApplicationRunner {

    private final DiscordLoadBalancer discordLoadBalancer;
    private final RestTemplate restTemplate;
    private final static Map<String,HashMap<String,JSONObject>> map = new HashMap();
    private String url = "https://discord.com/api/v9/guilds/${guild-id}/application-command-index";

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<DiscordInstance> aliveInstances = discordLoadBalancer.getAliveInstances();
        for (DiscordInstance discordInstance : aliveInstances) {
            DiscordAccount discordAccount = discordInstance.account();
            getMJCommandInfoFromServer(discordAccount);
        }

    }

    private void getMJCommandInfoFromServer(DiscordAccount discordAccount) {
        String reallyUrl = StringUtils.replace(url,"${guild-id}", discordAccount.getGuildId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", discordAccount.getUserToken());
        headers.set("User-Agent", discordAccount.getUserAgent());
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<JSONObject> jsonObjectResponseEntity = this.restTemplate.exchange(reallyUrl, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject jsonRoot = jsonObjectResponseEntity.getBody();
//        String xxx="{\"application_commands\":[{\"id\":\"938956540159881230\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554623\",\"name\":\"imagine\",\"description\":\"Create images with Midjourney\",\"options\":[{\"type\":3,\"name\":\"prompt\",\"description\":\"The prompt to imagine\",\"required\":true}],\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":1},{\"id\":\"941673664900898876\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554629\",\"name\":\"help\",\"description\":\"Shows help for the bot.\",\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":14},{\"id\":\"972289487818334209\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660565\",\"name\":\"info\",\"description\":\"View information about your profile.\",\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":2},{\"id\":\"972289487818334210\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660570\",\"name\":\"private\",\"description\":\"Toggle stealth mode\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":16},{\"id\":\"972289487818334211\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660571\",\"name\":\"public\",\"description\":\"Switch to public mode\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":22},{\"id\":\"972289487818334212\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660572\",\"name\":\"fast\",\"description\":\"Switch to fast mode\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":10},{\"id\":\"972289487818334213\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660573\",\"name\":\"relax\",\"description\":\"Switch to relax mode\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":8},{\"id\":\"984273800587776053\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415790055476\",\"name\":\"prefer\",\"description\":\"…\",\"options\":[{\"type\":2,\"name\":\"option\",\"description\":\"…\",\"options\":[{\"type\":1,\"name\":\"set\",\"description\":\"Set a custom option.\",\"options\":[{\"type\":3,\"name\":\"option\",\"description\":\"…\",\"required\":true,\"autocomplete\":true},{\"type\":3,\"name\":\"value\",\"description\":\"…\",\"required\":false}]},{\"type\":1,\"name\":\"list\",\"description\":\"View your current custom options.\"}]},{\"type\":1,\"name\":\"auto_dm\",\"description\":\"Whether or not to automatically send job results to your DMs.\"},{\"type\":1,\"name\":\"suffix\",\"description\":\"Suffix to automatically add to the end of every prompt. Leave empty to remove.\",\"options\":[{\"type\":3,\"name\":\"new_value\",\"description\":\"…\",\"required\":false}]},{\"type\":1,\"name\":\"remix\",\"description\":\"Toggle remix mode.\"},{\"type\":1,\"name\":\"variability\",\"description\":\"Toggle variability mode.\"}],\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":7},{\"id\":\"986816068012081172\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660567\",\"name\":\"invite\",\"description\":\"Get an invite link to the Midjourney Discord server\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":20},{\"id\":\"987795925764280351\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660568\",\"name\":\"subscribe\",\"description\":\"Subscribe to Midjourney\",\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":9},{\"id\":\"987795925764280352\",\"type\":3,\"application_id\":\"936929561302675456\",\"version\":\"1237876415790055477\",\"name\":\"Cancel Job\",\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1]},{\"id\":\"987795925764280353\",\"type\":3,\"application_id\":\"936929561302675456\",\"version\":\"1237876415790055478\",\"name\":\"DM Results\",\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1]},{\"id\":\"991449849599885383\",\"type\":3,\"application_id\":\"936929561302675456\",\"version\":\"1237876415790055479\",\"name\":\"Report Job\",\"dm_permission\":false,\"contexts\":[0,2],\"integration_types\":[0,1]},{\"id\":\"994261739745050684\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554622\",\"name\":\"ask\",\"description\":\"Get an answer to a question.\",\"options\":[{\"type\":3,\"name\":\"question\",\"description\":\"What is the question?\",\"required\":true}],\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":11},{\"id\":\"1000850743479255081\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415790055475\",\"name\":\"settings\",\"description\":\"View and adjust your personal settings.\",\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":5},{\"id\":\"1062880104792997970\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554624\",\"name\":\"blend\",\"description\":\"Blend images together seamlessly!\",\"options\":[{\"type\":11,\"name\":\"image1\",\"description\":\"First image to add to the blend\",\"required\":true},{\"type\":11,\"name\":\"image2\",\"description\":\"Second image to add to the blend\",\"required\":true},{\"type\":3,\"name\":\"dimensions\",\"description\":\"The dimensions of the image. If not specified, the image will be square.\",\"required\":false,\"choices\":[{\"name\":\"Portrait\",\"value\":\"--ar 2:3\"},{\"name\":\"Square\",\"value\":\"--ar 1:1\"},{\"name\":\"Landscape\",\"value\":\"--ar 3:2\"}]},{\"type\":11,\"name\":\"image3\",\"description\":\"Third image to add to the blend (optional)\",\"required\":false},{\"type\":11,\"name\":\"image4\",\"description\":\"Fourth image to add to the blend (optional)\",\"required\":false},{\"type\":11,\"name\":\"image5\",\"description\":\"Fifth image to add to the blend (optional)\",\"required\":false}],\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":4},{\"id\":\"1065569343456419860\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660569\",\"name\":\"stealth\",\"description\":\"Toggle stealth mode\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":15},{\"id\":\"1092492867185950852\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554625\",\"name\":\"describe\",\"description\":\"Writes a prompt based on your image.\",\"options\":[{\"type\":11,\"name\":\"image\",\"description\":\"The image to describe\",\"required\":false},{\"type\":3,\"name\":\"link\",\"description\":\"…\",\"required\":false}],\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":3},{\"id\":\"1121575372539039774\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554626\",\"name\":\"shorten\",\"description\":\"Analyzes and shortens a prompt.\",\"options\":[{\"type\":3,\"name\":\"prompt\",\"description\":\"The prompt to shorten\",\"required\":true}],\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":12},{\"id\":\"1124132684143271996\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415790055474\",\"name\":\"turbo\",\"description\":\"Switch to turbo mode\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":17},{\"id\":\"1136041075614683196\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660566\",\"name\":\"userid\",\"description\":\"Get your user ID\",\"dm_permission\":true,\"contexts\":[0,1,2],\"integration_types\":[0,1],\"global_popularity_rank\":19},{\"id\":\"1169435442328911902\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554630\",\"name\":\"show\",\"description\":\"Shows the job view based on job id.\",\"options\":[{\"type\":3,\"name\":\"job_id\",\"description\":\"The job ID of the job you want to show. It should look similar to this:…\",\"required\":true}],\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":6},{\"id\":\"1169440360339091476\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554627\",\"name\":\"tune\",\"description\":\"Create a shareable style tuner based on a prompt.\",\"options\":[{\"type\":3,\"name\":\"prompt\",\"description\":\"The base prompt to use for the tuner\",\"required\":true}],\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":13},{\"id\":\"1181338718158721086\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415471554628\",\"name\":\"list_tuners\",\"description\":\"List your tuners!\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":18},{\"id\":\"1199869998538162256\",\"type\":1,\"application_id\":\"936929561302675456\",\"version\":\"1237876415735660564\",\"name\":\"feedback\",\"description\":\"Submit your feedback!\",\"dm_permission\":true,\"contexts\":[0,1],\"integration_types\":[0],\"global_popularity_rank\":21}],\"version\":\"1237876417597800478\",\"applications\":[{\"id\":\"936929561302675456\",\"name\":\"Midjourney Bot\",\"description\":\"Generate an image based on a text prompt in under 60 seconds using the </imagine:938956540159881230> command!\\n\\nhttps://docs.midjourney.com/docs/terms-of-service\",\"icon\":\"f6ce562a6b4979c4b1cbc5b436d3be76\",\"bot_id\":\"936929561302675456\"}]}";
//        jsonRoot=JSONObject.parseObject(xxx);
        log.info("command info:{}", jsonRoot.toJSONString());
        JSONArray applicationCommands = jsonRoot.getJSONArray("application_commands");
        HashMap<String,JSONObject> hashMap=new HashMap<>();
        for (Object jsonObject :applicationCommands){
            JSONObject temp = JSONObject.parseObject(JSONObject.toJSONString(jsonObject), JSONObject.class);
            hashMap.put(temp.get("name").toString(),temp);
        }
        map.put(discordAccount.getId(),hashMap);
    }


    public static Map<String,HashMap<String,JSONObject>> getMJCommandInfos() {
        return  map;
    }
}
