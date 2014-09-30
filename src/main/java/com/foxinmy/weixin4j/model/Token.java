package com.foxinmy.weixin4j.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * access_token是公众号的全局唯一票据,公众号调用各接口时都需使用access_token,正常情况下access_token有效期为7200秒,
 * 重复获取将导致上次获取的access_token失效
 * 
 * @className Token
 * @author jy.hu
 * @date 2014年4月5日
 * @since JDK 1.7
 * @see <a
 *      href="http://mp.weixin.qq.com/wiki/index.php?title=%E8%8E%B7%E5%8F%96access_token">获取token</a>
 */
@XStreamAlias("app-token")
public class Token implements Serializable {

	private static final long serialVersionUID = 1L;
	@JSONField(name = "access_token")
	private String accessToken;
	@JSONField(name = "expires_in")
	private int expiresIn;
	private String openid;
	private long time;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Token) {
			return accessToken.equals(((Token) obj).getAccessToken());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Token [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", openid=" + openid + ", time=" + time + "]";
	}
}
