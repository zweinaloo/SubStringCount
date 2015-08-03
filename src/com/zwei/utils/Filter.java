package com.zwei.utils;

public class Filter {
	
	public String filterAll(String s){
		String regex_url = "(http\\:\\/\\/.+)"; // 匹配Url的正则表达式
		String regex_name = "@((?!=@|\\s|\\[|:).)*"; // 匹配微博名的正则表达式
		String regex_special = "[\\\\ · α.\\:\"'【】#//＂＂<>《》“” ️\\$￥：、……↓（）&()~|！\\[\\]]"; // 匹配特殊字符的正则表达式
		String regex_space = "\\s+";
		//zwei0624	
		//String regex_emoji = "[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]";//emjoy表情
		String regex_16 ="/x^[0-9a-fA-F]{2}$/";
		
		s = s.replaceAll(regex_16, "");
		//去掉Emoji表情字符
		s = s.replaceAll(regex_name, "");
		//s = s.replaceAll(regex_emoji, "");
		// 去掉url
		s = s.replaceAll(regex_url, "");
		// 去掉微博名
		//s = s.replaceAll(regex_name, "");
		// 去掉特殊字符
		s = s.replaceAll(regex_special, "");
		//精简空格
		s = s.replaceAll(regex_space, "");
		//如果只剩最后一个空格
		s = s.trim();//保留空格
		s=s.replaceAll("\\t|\\s+|\\r|\\n|quot;", "");
		s=s.replaceAll("“|”|，|—|。|　|，|？|", "");
		s=s.replaceAll("[a-zA-Z]?[\\d]?", "");
		//如果还有内容，就添加到返回值中
		if (s == null || s.length() <= 3
				|| s.equals("回复")
				|| s.equals("转发微博")
				|| s.equals("轉發微博")
				|| s.equals("热门 转发微博")){
			return null;
		}
		
		return s;
	}
}
