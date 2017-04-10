package demo.third.model;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 存储oauth基本信息的类
 * 
 * @author Administrator
 *
 */
public abstract class OAuthInfo {

    private String authUrl;
    private String tokenUrl;
    private String userInfoApi;
    private String portraitUrlTemplate; // 头像图片路径模板

    private Map<String, String> params = new HashMap<String, String>();

    /**
     * 构造时从配置文件读取信息
     * 
     * @param fileName
     */
    @SuppressWarnings("unchecked")
    public OAuthInfo(String configFile) {

        InputStream is = null;
        SAXReader reader = new SAXReader();

        try {
            is = new FileInputStream(configFile);
            Document doc = reader.read(is);
            Element rootElement = doc.getRootElement();

            // 获取所有param，存入params map
            Element paramsElement = rootElement.element("params");
            List<Element> paramElements = paramsElement.elements();
            for (Element paramElement : paramElements) {
                params.put(paramElement.attributeValue("name"), paramElement.attributeValue("value"));
            }

            // 获取authUrl模板后直接用params完成模板替换，得到替换完成的authUrl
            authUrl = templateHandle(rootElement.element("authUrl").getText(), params);

            // 获取tokenUrl，替换掉固定模板值
            tokenUrl = templateHandle(rootElement.element("tokenUrl").getText(), params);

            // 获取userInfoApi
            userInfoApi = templateHandle(rootElement.element("userInfoApi").getText(), params);

            // 头像路径模板
            portraitUrlTemplate = templateHandle(rootElement.element("portraitUrlTemplate").getText(), params);
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从返回的json对象中获取user信息
     * 
     * @param userNode
     * @return
     */
    public abstract User getUser(JsonNode userNode) throws IOException;

    /**
     * 验证返回的json对象是否有效，因为有可能返回的是错误信息，此时无效
     * 
     * @param userNode
     * @return
     */
    public abstract boolean userDataValidate(JsonNode userNode);

    /**
     * 引导用户浏览器跳转到授权页的路径
     * 
     * @return
     */
    public String getAuthUrl() {
        return authUrl;
    }

    /**
     * 获取access token的完整url
     * 
     * @param code
     * @return
     */
    public String getTokenUrl(String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("code", code);
        return templateHandle(this.tokenUrl, params);
    }

    /**
     * 获取用户api访问完整路径
     * 
     * @param accessToken
     * @return
     */
    public String getUserInfoApiUrl(String accessToken) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("accessToken", accessToken);
        return templateHandle(this.userInfoApi, params);
    }

    /**
     * 下载头像图片
     * 
     * @param portrait
     * @return
     */
    protected byte[] downloadPortrait(String portrait) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("portrait", portrait);
        String urlstr = templateHandle(portraitUrlTemplate, params);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();

            byte[] buff = new byte[1024];
            int cnt;
            while ((cnt = is.read(buff)) > 0) {
                baos.write(buff, 0, cnt);
            }

            is.close();
            conn.disconnect();

            baos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 模板替换
     * 
     * @param text
     * @param params2
     * @return
     */
    private String templateHandle(String template, Map<String, String> params) {
        // 示例:
        // template =
        // "http://test.com/auth?client_id=${cid}&redirect_uri=${ruri}&auth_type=code";
        // regex = ${(.+?)}，匹配${cid}, ${ruri},匹配后group(0)为${cid}，group(1)为cid

        // 执行匹配操作
        Pattern regex = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = regex.matcher(template);

        StringBuffer sb = new StringBuffer(); // 用来缓存替换后结果
        while (matcher.find()) { // 从字符串开头查找每个匹配项
            String key = matcher.group(1); // 找到每个匹配项后，从匹配值中取出要替换的变量名，如cid
            String replaceVal = params.get(key); // 根据变量名到map中查找要替换的值，如cid的真实值

            if (replaceVal == null) {
                // 未在参数map中找到替换值则不替换此参数，如初次处理tokenUrl时，code变量不需要替换
                continue;
            } else {
                // 用指定值替换匹配部分，如：将${cid}替换成真实cid值xxxx，并将替换后结果缓存入sb，匹配项前面的字符原样存入
                matcher.appendReplacement(sb, replaceVal);
            }
        }
        // 最后一个匹配项并替换完成后，sb中已存入到最后匹配项位置的所有替换结果
        // 最后匹配项以后的字符还没有加入，通过以下操作，将剩余字符加入，完成完整替换过程
        matcher.appendTail(sb);

        return sb.toString();
    }
}
