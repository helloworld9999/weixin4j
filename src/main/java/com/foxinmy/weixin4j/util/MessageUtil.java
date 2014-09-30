package com.foxinmy.weixin4j.util;

import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foxinmy.weixin4j.msg.BaseMessage;
import com.foxinmy.weixin4j.type.EventType;
import com.foxinmy.weixin4j.type.MessageType;
import com.foxinmy.weixin4j.xml.XStream;

public class MessageUtil {
	
	private final static Logger log = LoggerFactory.getLogger(MessageUtil.class);

	/**
	 * 验证微信签名
	 * 
	 * @param echostr
	 *            随机字符串
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @param signature
	 *            微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
	 * @return 开发者通过检验signature对请求进行相关校验。若确认此次GET请求来自微信服务器
	 *         请原样返回echostr参数内容，则接入生效 成为开发者成功，否则接入失败
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97">接入指南</a>
	 */
	public static String signature(String echostr, String timestamp, String nonce, String signature) {
		String app_token = ConfigUtil.getValue("app_token");
		if (StringUtil.isBlank(app_token)) {
			log.error("signature fail : token is null!");
			return null;
		}
		if (StringUtil.isBlank(echostr) || StringUtil.isBlank(timestamp) || StringUtil.isBlank(nonce)) {
			log.error("signature fail : invalid parameter!");
			return null;
		}
		String _signature = null;
		try {
			String[] a = { app_token, timestamp, nonce };
			Arrays.sort(a);
			StringBuilder sb = new StringBuilder(3);
			for (String str : a) {
				sb.append(str);
			}
			_signature = DigestUtils.sha1Hex(sb.toString());
		} catch (Exception e) {
			log.error("signature error", e);
		}
		if (signature.equals(_signature)) {
			return echostr;
		} else {
			log.error("signature fail : invalid signature!");
			return null;
		}
	}

	/**
	 * xml消息转换为消息对象
	 * <p>
	 * 微信服务器在五秒内收不到响应会断掉连接,并且重新发起请求,总共重试三次
	 * </p>
	 * 
	 * @param xml
	 *            消息字符串
	 * @return 消息对象
	 * @throws DocumentException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/index.php?title=%E9%AA%8C%E8%AF%81%E6%B6%88%E6%81%AF%E7%9C%9F%E5%AE%9E%E6%80%A7">验证消息的合法性</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF">普通消息</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6%E6%8E%A8%E9%80%81">事件触发</a>
	 * @see com.foxinmy.weixin4j.type.MessageType
	 * @see com.feican.weixin.msg.BaeMessage
	 * @see com.foxinmy.weixin4j.msg.TextMessage
	 * @see com.foxinmy.weixin4j.msg.in.ImageMessage
	 * @see com.foxinmy.weixin4j.msg.in.VoiceMessage
	 * @see com.foxinmy.weixin4j.msg.in.VideoMessage
	 * @see com.foxinmy.weixin4j.msg.in.LocationMessage
	 * @see com.foxinmy.weixin4j.msg.in.LinkMessage
	 * @see com.foxinmy.weixin4j.msg.event.ScribeEventMessage
	 * @see com.foxinmy.weixin4j.msg.event.ScanEventMessage
	 * @see com.foxinmy.weixin4j.msg.event.LocationEventMessage
	 * @see com.foxinmy.weixin4j.msg.event.menu.MenuEventMessage
	 */
	public static BaseMessage xml2msg(String xml) throws DocumentException {
		if (StringUtil.isBlank(xml))
			return null;
		Document doc = DocumentHelper.parseText(xml);
		String type = doc.selectSingleNode("/xml/MsgType").getStringValue();
		if (StringUtil.isBlank(type)) {
			return null;
		}
		XStream xstream = new XStream();
		MessageType messageType = MessageType.valueOf(type.toLowerCase());
		Class<? extends BaseMessage> messageClass = messageType.getMessageClass();
		if (messageType == MessageType.event) {
			type = doc.selectSingleNode("/xml/Event").getStringValue();
			messageClass = EventType.valueOf(type.toLowerCase()).getEventClass();
		}
		xstream.alias("xml", messageClass);
		xstream.ignoreUnknownElements();
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(messageClass);
		return xstream.fromXML(doc.asXML(), messageClass);
	}

	/**
	 * xml消息转换为消息对象
	 * 
	 * @param inputStream
	 *            带消息字符串的输入流
	 * @return 消息对象
	 * @throws DocumentException
	 * @see {@link com.foxinmy.weixin4j.WeixinProxy#xml2msg(String)}
	 */
	public static BaseMessage xml2msg(InputStream inputStream) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(inputStream);
		return xml2msg(doc.asXML());
	}
}
