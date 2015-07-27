package com.tyh.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thoughtworks.xstream.XStream;
import com.tyh.pojo.Message;
import com.tyh.util.Constant;
import com.tyh.util.MessageUtil;

@Controller
public class WeChatMessageController {
	
	@RequestMapping("/message")
	public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		//设置接口的编码方式
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		
		//parse request xml to message entity
		Map<String, String > map = MessageUtil.saveMessageToMap(request);
		Message message = MessageUtil.parseXMLToMessage1(request);		
		System.out.println(message.toString());
		
		//text message
		if(message.getMsgType().equals(Constant.TEXT_MESSAGE_TYPE)){
			
			message.setContent("this message is replied by serveer");
			//parse the message object to xml
			
			String xml = MessageUtil.messageToXml(message);
			
			OutputStream os = response.getOutputStream();
			os.write(xml.getBytes());
			os.flush();
			os.close();
		}//event message
		else if(message.getMsgType().equals(Constant.EVENT_MESSAGE_TYPE)){
			System.out.println("this is an Event message");
		}
		
	}
}
