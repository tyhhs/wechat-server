package com.tyh.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.tyh.util.MessageUtil.XStreamCDATA;

public class Message {
	
	@XStreamAlias("ToUserName")
	@XStreamCDATA
	private String ToUserName;
	
	@XStreamAlias("FromUserName")
	@XStreamCDATA
	private String FromUserName;
	
	@XStreamAlias("CreateTime")
	private String CreateTime;
	
	@XStreamAlias("MsgType")
	@XStreamCDATA
	private String MsgType;
	
	@XStreamAlias("Content")
	@XStreamCDATA
	private String Content;
	
	@XStreamAlias("MsgId")
	private String MsgId;
	
	@XStreamAlias("Event")
	@XStreamCDATA
	private String Event;
	
	@XStreamAlias("EventKey")
	@XStreamCDATA
	private String EventKey;
	
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	@Override
	public String toString() {
		return "Message [ToUserName=" + ToUserName + ", FromUserName="
				+ FromUserName + ", CreateTime=" + CreateTime + ", MsgType="
				+ MsgType + ", Content=" + Content + ", MsgId=" + MsgId
				+ ", Event=" + Event + ", EventKey=" + EventKey + "]";
	}
}
