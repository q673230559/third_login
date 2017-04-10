package demo.third.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.third.model.OAuthInfo;
import demo.third.model.User;

/**
 * 授权与处理的辅助工具
 * 
 * @author Administrator
 *
 */
public class OAuthHelper {

    private OAuthHelper() {
    }

    // 存储信息的数据结构
    private static final Map<String, OAuthInfo> infos = new HashMap<String, OAuthInfo>();

    // 加载信息的过程，本例中直接硬编码生成信息，也可以通过读取配置文件的方式获取
    static {

        String configBasePath = OAuthHelper.class.getClassLoader().getResource("../config").getPath();

        infos.put("baidu", new OAuthInfo(configBasePath + File.separator + "baidu.xml") {
            @Override
            public User getUser(JsonNode userNode) throws IOException {
                User user = new User();
                user.setThirdId("baidu_" + userNode.get("uid").asText());
                user.setName(userNode.get("uname").asText());
                user.setPortraitData(downloadPortrait(userNode.get("portrait").asText()));
                return user;
            }

            @Override
            public boolean userDataValidate(JsonNode userNode) {
                return userNode.get("uid") != null;
            }
        });
        
        infos.put("renren", new OAuthInfo(configBasePath + File.separator + "renren.xml") {
            @Override
            public User getUser(JsonNode userNode) throws IOException {
                System.out.println(userNode);

                userNode = userNode.get("response");
                User user = new User();
                user.setThirdId("renren_" + userNode.get("id").asText());
                user.setName(userNode.get("name").asText());
                
                String portrait = userNode.get("avatar").get(1).get("url").asText();
                user.setPortraitData(downloadPortrait(portrait));
                
                return user;
            }
            
            @Override
            public boolean userDataValidate(JsonNode userNode) {
                return userNode.get("response") != null && userNode.get("response").get("id") != null;
            }
        });
    }

    /**
     * 获取对应平台的信息对象
     * 
     * @param plat
     * @return
     */
    public static OAuthInfo getInfo(String plat) {
        return infos.get(plat);
    }

    /**
     * 获取用户信息
     * 
     * @param plat
     * @param code
     * @return
     */
    public static User getUserInfo(String plat, String code) {

        OAuthInfo info = getInfo(plat);
        final ObjectMapper mapper = new ObjectMapper();

        try {
            String atokenUrl = info.getTokenUrl(code);
            String ret = httpGet(atokenUrl);
            JsonNode retNode = mapper.readTree(ret);
            JsonNode atokenNode = retNode.get("access_token");

            if (atokenNode == null) {
                logError(retNode, plat, code, "access_token");
                return null;
            } else {
                String apiUrl = info.getUserInfoApiUrl(atokenNode.asText());
                ret = httpGet(apiUrl);
                JsonNode userNode = mapper.readTree(ret);

                if (info.userDataValidate(userNode)) {
                    return info.getUser(userNode);
                } else {
                    logError(userNode, plat, atokenNode.asText(), "get_user");
                    return null;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 记录错误信息
     * 
     * @param retNode
     * @param plat
     * @param val
     * @param step
     */
    private static void logError(JsonNode retNode, String plat, String val, String phase) {
        System.err.println("error: " + plat + ", when " + phase + ", value is " + val);
        System.err.println(retNode.toString());
    }

    // 发起http get类型请求获取返回结果
    private static String httpGet(String urlstr) throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.connect();
        InputStream is = conn.getInputStream();

        byte[] buff = new byte[is.available()];
        is.read(buff);
        String ret = new String(buff, "utf-8");

        is.close();
        conn.disconnect();

        return ret;
    }
}
