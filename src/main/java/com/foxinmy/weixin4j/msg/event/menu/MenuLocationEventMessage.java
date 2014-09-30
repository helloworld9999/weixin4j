package com.foxinmy.weixin4j.msg.event.menu;

import com.foxinmy.weixin4j.type.EventType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 弹出地理位置选择器的事件推送
 * 
 * @className MenuLocationEventMessage
 * @author jy
 * @date 2014年9月30日
 * @since JDK 1.7
 * @see <a
 *      href="http://mp.weixin.qq.com/wiki/index.php?title=%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8F%9C%E5%8D%95%E4%BA%8B%E4%BB%B6%E6%8E%A8%E9%80%81#location_select.EF.BC.9A.E5.BC.B9.E5.87.BA.E5.9C.B0.E7.90.86.E4.BD.8D.E7.BD.AE.E9.80.89.E6.8B.A9.E5.99.A8.E7.9A.84.E4.BA.8B.E4.BB.B6.E6.8E.A8.E9.80.81">弹出地理位置选择事件推送</a>
 */
public class MenuLocationEventMessage extends MenuEventMessage {

	private static final long serialVersionUID = 145223888272819563L;

	public MenuLocationEventMessage() {
		super.setEventType(EventType.location_select);
	}

	@XStreamAlias("SendLocationInfo")
	private LocationInfo locationInfo;

	public LocationInfo getLocationInfo() {
		return locationInfo;
	}

	public static class LocationInfo {
		@XStreamAlias("Location_X")
		private double x; // 地理位置维度
		@XStreamAlias("Location_Y")
		private double y; // 地理位置经度
		@XStreamAlias("Scale")
		private double scale; // 地图缩放大小
		@XStreamAlias("Label")
		private String label; // 地理位置信息
		@XStreamAlias("Poiname")
		private String poiname;

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getScale() {
			return scale;
		}

		public String getLabel() {
			return label;
		}

		public String getPoiname() {
			return poiname;
		}

		@Override
		public String toString() {
			return "LocationInfo [x=" + x + ", y=" + y + ", scale=" + scale
					+ ", label=" + label + ", poiname=" + poiname + "]";
		}
	}

	@Override
	public String toString() {
		return "MenuLocationEventMessage [locationInfo=" + locationInfo + "]";
	}
}
