package com.tyh.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.HttpRequestHandler;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.tyh.pojo.Message;

public class MessageUtil {

	public static Map<String, String> saveMessageToMap(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		InputStream is;
		try {
			is = request.getInputStream();
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(is);
			Element root = document.getRootElement();
            List<Element> elementList = root.elements();

            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}
	public static Message parseXMLToMessage1(HttpServletRequest request) {
		Message message = null;
		InputStream inputStream;
		try {
			// get input stream
			inputStream = request.getInputStream();
			// create reader
			SAXReader reader = new SAXReader();
			// get xml document
			Document document = reader.read(inputStream);
			// get root
			Element root = document.getRootElement();

			Iterator<?> iterator = root.elementIterator();
			// reflect
			Class<?> c = Class.forName("com.tyh.pojo.Message");

			message = (Message) c.newInstance();

			while (iterator.hasNext()) {
				Element ele = (Element) iterator.next();

				Field field = c.getDeclaredField(ele.getName());

				Method method = c.getDeclaredMethod("set" + ele.getName(),
						field.getType());

				method.invoke(message, ele.getText());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return message;

	}

	public static Message parseXMLToMessage2(HttpServletRequest request) {
		Message message = new Message();
		XStream xStream = new XStream();
		try {
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String string = "";
			StringBuffer sb = new StringBuffer();
			while ((string = br.readLine()) != null) {
				sb.append(string);
			}
			String xml = sb.toString();
			// 设置根节点别名
			xStream.alias("xml", message.getClass());
			//
			message = (Message) xStream.fromXML(xml);
			return message;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String messageToXml(Message message) {

		XStream xStream = createXstream();
		// 设置根节点的名称为"xml"
		xStream.alias("xml", message.getClass());
		String xml = xStream.toXML(message);

		return xml;
	}

	public static XStream createXstream() {
		return new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;
					Class<?> targetClass = null;

					@Override
					public void startNode(String name,
							@SuppressWarnings("rawtypes") Class clazz) {
						super.startNode(name, clazz);
						// 业务处理，对于用XStreamCDATA标记的Field，需要加上CDATA标签
						if (!name.equals("xml")) {
							cdata = needCDATA(targetClass, name);
						} else {
							targetClass = clazz;
						}
					}

					@Override
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
	                        writer.write(text);
	                        writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
	}

	private static boolean needCDATA(Class<?> targetClass, String fieldAlias) {
		boolean cdata = false;
		// first, scan self
		cdata = existsCDATA(targetClass, fieldAlias);
		if (cdata)
			return cdata;
		// if cdata is false, scan supperClass until java.lang.Object
		Class<?> superClass = targetClass.getSuperclass();
		while (!superClass.equals(Object.class)) {
			cdata = existsCDATA(superClass, fieldAlias);
			if (cdata)
				return cdata;
			superClass = superClass.getClass().getSuperclass();
		}
		return false;
	}

	private static boolean existsCDATA(Class<?> clazz, String fieldAlias) {
		// scan fields
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// 1. exists XStreamCDATA
			if (field.getAnnotation(XStreamCDATA.class) != null) {
				XStreamAlias xStreamAlias = field
						.getAnnotation(XStreamAlias.class);
				// 2. exists XStreamAlias
				if (null != xStreamAlias) {
					if (fieldAlias.equals(xStreamAlias.value()))// matched
						return true;
				} else {// not exists XStreamAlias
					if (fieldAlias.equals(field.getName()))
						return true;
				}
			}
		}
		return false;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	public @interface XStreamCDATA {

	}
}
