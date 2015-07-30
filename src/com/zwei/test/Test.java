package com.zwei.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.zwei.SubStringCount.SubStringCount;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path = "D:\\Zwei1\\测试&数据\\微博数据\\2.txt";
		String pathFloder = "D:\\Zwei1\\测试&数据\\微博数据\\#王林#";
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		
		SubStringCount a = new SubStringCount();
		a.setPath(pathFloder);       //配置读取内容位置
		//a.setPath(path);
		a.setCharset("utf-8");       //(可选)配置读取内容编码规范
		a.preparing(false);          //读取的是文件还是文件夹 true 为文件夹 。false 为文件夹
		a.stringCount(2,4);   // 得到N到M元词
		a.filterAndSort(2);         //过滤词频小于N的
		
		
		result=a.getResult();              //得到处理结果           
		
		//a.showMap(100);             //测试     输出TOP项
		System.out.println(result);
		//System.out.println(a.getResult());
	}	
}
