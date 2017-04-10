package demo.third.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用户类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class User implements Serializable {

    private Long id; // 唯一标识

    private String account; // 本站登录账号
    private String passwd; // 登录密码

    private String thirdId; // 第三方平台账号(openID)

    private String name; // 用户名

    private byte[] portraitData; // 头像图片的二进制数据

    /**
     * 头像标识名，用于页面显示和文件处理<br>
     * 因头像存储文件名与ID相同，此处实际返回的就是ID的字符串值
     * 
     * @return
     */
    public String getPortrait() {
        return id == null ? "default" : String.valueOf(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPortraitData() {
        return portraitData;
    }

    public void setPortraitData(byte[] portraitData) {
        this.portraitData = portraitData;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", passwd=" + passwd + ", thirdId=" + thirdId + ", name="
				+ name + ", portraitData=" + Arrays.toString(portraitData) + "]";
	}
    
}
