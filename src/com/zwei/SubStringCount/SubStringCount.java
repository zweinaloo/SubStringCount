/**
 * @package com.zwei.SubStringCount
 * @brief 子串统计
 */
package com.zwei.SubStringCount;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zwei.Util.MapSortDemo;
import com.zwei.Util.Sort;

/**
 * 非实体子串统计类
 * @class SubStringCount
 * @brief 基于统计方法发现热词
 * @author Zwei
 * @date 2015年7月18日 下午3:57:50
 * @version V1.0
 */
public class SubStringCount {
	public static int fileCount=0;                                               //<读入文档数计数
	private static String path;
	private static String charset = "utf-8";
	private StringBuffer data = null;                                            //<输入数据
	private static List<Integer> PointTable = new ArrayList<Integer>();                 //<Point指针表
	private static List<Integer> LeftTable = new ArrayList<Integer>();                  //<左子串表
	private static Map<String, Integer> result = new HashMap<String, Integer>();        //<处理结果
	private static HashSet<String> stopWordList = new HashSet<String>();                //停用词词表
	private static HashSet<String> stopHeadWordList = new HashSet<String>();                //停用词词表
	private static HashSet<String> stopTailWordList = new HashSet<String>();                //停用词词表
	private static String pathStop = "D:\\Zwei1\\测试&数据\\停用词表.txt";
	private static String pathStop1 = "D:\\Zwei1\\测试&数据\\停用词首表.txt";
	private static String pathStop2 = "D:\\Zwei1\\测试&数据\\停用词尾表.txt"; //待补

	
	
	
	/**
	 * 构造器
	 * @brief 构造器
	 * @param path          文件地址
	 * @param charset       文件编码
	 * @param single        单文件或者多文件读取数据
	 */
	public SubStringCount(String path,String charset,Boolean single) throws IOException{
		this.path=path;
		this.charset=charset;
		preparing(single);
	}
	public SubStringCount(){
		
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public Map<String, Integer> getResult() {
		return result;
	}
	public void setResult(Map<String, Integer> result) {
		SubStringCount.result = result;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public StringBuffer getData() {
		return data;
	}
	public void setData(StringBuffer data) {
		this.data = data;
	}

	
	
	/**
	 * 从一个txt文件获取数据
	 * @brief 从一个txt文件获取数据
	 * @param path      txt文件路径
	 * @param charset   文件编码格式
	 * @throws IOException  txt文件未找到
	 * @return StringBuffer 待处理语料data
	 * @since V1.0
	 * @date 2015年7月18日 下午4:55:17
	 */
	private  void getData(String path,String charset) throws IOException{
		File file = new File(path);
		FileInputStream inputStream = new FileInputStream(file);
		DataInputStream read = new DataInputStream(inputStream);

		BufferedReader br = new BufferedReader(new InputStreamReader(read,charset));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line=br.readLine())!=null){
			line=filterAll(line);
			//line=filterStopWord(line,stopWordList);
			sb.append(line);
		}
		br.close();
		read.close();
		inputStream.close();
		//System.out.println(sb);
		this.data = sb;
	}
	
	/**
	 * 从一个文件夹获取数据
	 * @brief 从一个txt文件夹获取数据
	 * @param path      文件夹路径
	 * @param charset   文件编码格式
	 * @throws IOException  文件夹未找到，文件夹下不可包含子文件夹
	 * @return StringBuffer 待处理语料data
	 * @since V1.0
	 * @date 2015年7月18日 下午4:59:51
	 */
	public void getDataFloder(String path,String charset) throws IOException{
		File parentPath = new File(path);
		File[] fileList = parentPath.listFiles();
		fileCount=fileList.length-1;
		StringBuffer sentenceList = new StringBuffer();
		try{
			for(File file:fileList){
				//System.out.println(file.getAbsolutePath());
				File testSetFile = new File(file.getAbsolutePath());
				FileInputStream fileInputStream = new FileInputStream(testSetFile);		
				DataInputStream read = new DataInputStream(fileInputStream);
				BufferedReader br = new BufferedReader(new InputStreamReader(read, charset));
				String line;
				while((line=br.readLine())!=null){
					line=filterAll(line);
					//line=filterStopWord(line,stopWordList);
					if(line!=null&&line.length()>0){
						sentenceList.append(line);
					}
			}
				br.close();
				read.close();
				fileInputStream.close();
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("【停用词过滤】");
		System.out.println("【总字数】"+sentenceList.length());
		System.out.println("【总文件数】"+fileCount);
		this.data = sentenceList;
	}

	/**
	 * 加载停用词表
	 * @brief 加载停用词表
	 * @param path 停用词表路径
	 * @param charset 停用词表 编码格式
	 * @return HashSet<String> 停用词集合
	 * @since V1.0
	 * @date 2015年7月18日 下午5:27:31
	 */
	private static void loadStopWord(String pathStop,HashSet<String> stopWordList) throws IOException{
		File file = new File(pathStop);
		FileInputStream inputStream = new FileInputStream(file);
		DataInputStream read = new DataInputStream(inputStream);
		BufferedReader br = new BufferedReader(new InputStreamReader(read,"gbk"));
		String line;
		while((line=br.readLine())!=null){
			stopWordList.add(line);
		}
		br.close();
		read.close();
		inputStream.close();
		System.out.println("【加载停用词表】");
		
		}
	
	//建立P表
	/**
	 * 建立指向和data长度相等的integer数组
	 * @brief P表(PointTable)
	 * @param data            待处理语料
	 * @param PointTable      P表
	 * @return void
	 * @since V1.0
	 * @date 2015年7月18日 下午5:28:53
	 */
	private  void buildPointTable(StringBuffer data,List<Integer> PointTable){
		int dataSize=data.length();
		int i=0;
		while(i<dataSize-1){	
				PointTable.add(i);
				i++;
			
		}
		System.out.println("【PointTable建立完成】");
		//System.out.println(PointTable.size());
	}

	//排序P表
	/**
	 * 排序P表
	 * @brief 使用Sort工具类排序P表
	 * @param data 输入数据
	 * @param PointTable P表
	 * @return void
	 * @since V1.0
	 * @date 2015年7月18日 下午5:30:39
	 */
	private void sortPointTable(StringBuffer data,List<Integer> PointTable){
		Sort sort = new Sort(PointTable,data); 
		PointTable=sort.getPointTable();
		//System.out.println(PointTable);
		System.out.println("【PointTable排序完成】");
	}
	
	//构建左子串表
	/**
	 * 构建L表
	 * @brief 根据P表构建左子串表
	 * @param data 输入数据
	 * @param PointTable P表
	 * @param LeftTable 左子串表
	 * @return void
	 * @since V1.0
	 * @date 2015年7月18日 下午5:37:09
	 */
	private void buildLeftTable(StringBuffer data,List<Integer> PointTable,List<Integer> LeftTable){
		
		for(int i=0;i<PointTable.size();i++){
			//起点必定为0
			if(i==0){
				LeftTable.add(0);
			}
			else {
				String a =data.substring(PointTable.get(i-1),PointTable.size());
				String b =data.substring(PointTable.get(i),PointTable.size());				
				//求最大相同子串
				int length=getMaxSameLength(a, b);
				//最长左子串长度加入L表
				LeftTable.add(length);
			}
		}
		System.out.println("【LeftTable建立完成】");
	}
	
	//求最大子串长度
	/**
	 * 求最大子串长度
	 * @brief 求最大子串长度
	 * @param a 子串1
	 * @param b 子串2
	 * @return
	 * @return int 最大子串的长度
	 * @since V1.0
	 * @date 2015年7月18日 下午5:49:34
	 */
	private int getMaxSameLength(String a,String b){
		int length=0;
		for(int i=1;i<=a.length();i++){
			if(a.length()>i&&b.length()>i){				
				if(a.substring(0, i).equals(b.substring(0, i))){
					length=i;
				}else {
					break;
				}
			}
			else {
				break;
			}
		}	
		//System.out.println(length);
		return length;
	}
	
	/**
	 * 
	 * @brief 得到N元串及词频
	 * @param N 想获取的子串长度
	 * @return
	 * @return Map<String,Integer> N元子串结果
	 * @since V1.0
	 * @date 2015年7月18日 下午5:49:08
	 */
	public Map<String, Integer> stringCount(int N){
		int size=PointTable.size();  //m值
		String X=data.substring(PointTable.get(0), size).substring(0, N);
		int count=1;
		
		
		for(int i=0;i<size;i++){		
			if(LeftTable.get(i)>=N){
			//X频次+1	
				//System.out.println(X);
				count++;
			}else {
				//System.out.println(X);
				result.put(X, count);
				//System.out.println(i);
                if(data.substring(PointTable.get(i), size).length()>N){
                	X=data.substring(PointTable.get(i),size).substring(0,N);
                	count=1;
				}
			}
		}
		System.out.println("【"+N+"元子串完成】");
		return result;
	}
			
	//显示map
	/**
	 * @test
	 * @brief 显示排序后结果的前N项
	 * @param top 希望得到的排序后前N个结果
	 * @since V1.0
	 * @date 2015年7月18日 下午5:45:03
	 */
	public void showMap(int top){
		int i=0;
		if(result!=null&&result.size()>0){			
			System.out.println("【词频前"+top+"】");
			for (Map.Entry<String, Integer> entry : result.entrySet()) {
				i++;
				if(i<=top){

					System.out.print(entry.getKey() + ":" + entry.getValue()+"\n");
				}
					
			}
			System.out.println();
			System.out.println("【剩余总词数】"+result.size());
		}else {
			System.out.println("resutl is null");
		}
		
	}
	
	
	/**
	 * 
	 * @brief 根据词频过滤结果
	 * @param result 处理结果
	 * @param n
	 * @return void
	 * @since V1.0
	 * @date 2015年7月18日 下午5:43:28
	 */
	public void filterByCount(int n){
		Set<String> key = result.keySet();
		List<String> filterList = new ArrayList<String>();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String s = it.next();
			if(result.get(s)<n){
				//System.out.println(s+"|"+result.get(s));
				filterList.add(s);
			}
		}
		for(String s:filterList){
			result.remove(s);
		}
		System.out.println("【过滤频率小于"+n+"次的词"+filterList.size()+"个】");
		//showMap(result);
	}
	
	//得到N元到M元子串
	/**
	 * 
	 * @brief 得到N元到M元子串
	 * @param N 想获得的最小元值，如2
	 * @param M 想获得的最大元值，如4
	 * @return Map<String,Integer> N到M元子串的结果
	 * @since V1.0
	 * @date 2015年7月18日 下午8:49:50
	 */
	public Map<String, Integer> stringCount(int N ,int M){
		if(N<=M){
			Map<String, Integer> result = new HashMap<String, Integer>();    //处理结果
			for(int i=N;i<=M;i++){
				result.putAll(stringCount(i));
			}			
			return result;
		}else {
			System.err.println(M+"<"+N);
			System.exit(0);
			return null;
			
		}
		
	}
	
	/**
	 * 
	 * @brief 近频子串过滤
	 * 将频率接近的子串归并为一个子串，如：江泽民出现"10"次，江泽出现"10"，泽民出现"10"次，则将短子串剔除
	 * @param percent
	 * @return void
	 * @since V1.0
	 * @date 2015年7月18日 下午8:56:04
	 */
	private static void substringReduction(double percent){
		System.out.println("【近频子串过滤】");
		Set<String> listSet = new HashSet<String>();
		int count=0,count1=0;
			listSet.addAll(result.keySet());
			for(String key :listSet){
				//统计》2的串
				if(key.length()>2&&result.containsKey(key)){
					Integer Keyvalue = result.get(key);
					//前缀串及中间串移除
					for(int j=0;j<=key.length()-3;j++){
						for(int i =j+1;i<=key.length()-1;i++){							
								String x = key.substring(j,i);
								if(result.containsKey(x)){
									Integer Subvalue = result.get(x);
									//如果子串与父串频率相近 ，移除子串
									if((Math.abs(Keyvalue-Subvalue))/(Keyvalue*1.0)<percent){//[0.1,0.3]
										count++;
										result.remove(x);
										//break;
									}
									else if(Keyvalue<Subvalue*percent*(x.length()/((double)key.length()*1.0))){
										result.remove(key);
									}
								}
						}
					}
					//后缀串移除
					for(int i =1;i<=key.length()-1;i++){						
						String x = key.substring(i,key.length());
						if(result.containsKey(x)){
							Integer Subvalue = result.get(x);
							//如果子串与父串频率相近 ，移除子串
							if((Math.abs(Keyvalue-Subvalue))/(Keyvalue*1.0)<percent){//[0.1,0.3]
								count1++;
								result.remove(x);
								//break;
							}
							else if(Keyvalue<Subvalue*percent*(x.length()/((double)key.length()*1.0))){
								result.remove(key);
							}
						}
					}
					
				}
			}
		System.out.println("【移除前缀及中间串"+count+"个|移除后缀"+count1+"个】");
	}
	
	/**
	 * 
	 * @brief 将map结果 排序
	 * @return Map<String,Integer> 排序后的子串结果
	 * @since V1.0
	 * @date 2015年7月18日 下午8:55:37
	 */
	private static Map<String, Integer> sortMapShow(){
		MapSortDemo sortDemo = new MapSortDemo();
		return sortDemo.sortMapByValue(result);
	}
	
	//垃圾串过滤
	@SuppressWarnings("unused")
	private void filterByRules(){
		System.out.println("【停用词过滤】");
		int a=0,b=0,c=0,d=0;
		List<String> filterList = new ArrayList<String>();
		//1.移除候选项中的停用词
		for(String key : stopWordList){
			if(result.containsKey(key)){
				result.remove(key);
			}
		}
		for (Map.Entry<String, Integer> entry : result.entrySet()) {
		//2.根据词长度分割词
			String key = entry.getKey();                //词
			String s = key.substring(0,1);				//词首
			String t = key.substring(key.length()-1);	//词尾
			//如果词首或词尾是停用词 则移除
			if(stopHeadWordList.contains(s)||stopHeadWordList.contains(t)&&false){
				filterList.add(key);
			}else {
				if(key.length()==3){
					String m1 = key.substring(1,3);
					String m2 = key.substring(0,2);
					if(stopWordList.contains(m1)||stopWordList.contains(m2)){
						filterList.add(key);
					}
				}
				if(key.length()==4){
					String m3 =key.substring(1,3);
					if(stopWordList.contains(m3)||result.containsKey(m3)){
						filterList.add(key);
					}
				}
			}
		}
		for(String key: filterList){
			result.remove(key);
		}
	}
				
	
	

	/**
	 * 停用词过滤
	 * @deprecated
	 * @brief 停用词过滤
	 * @param msg 处理文本
	 * @param stopWordSet 停用词表
	 * @return String 经过停用词过滤的原始数据
	 * @since V1.0
	 * @date 2015年7月18日 下午8:55:00
	 */
	private String  filterStopWord(String msg,HashSet<String> stopWordSet){
		for(String stopWord:stopWordSet){
			if(msg!=null){
				try {
					msg=msg.replaceAll(stopWord, "");				
				} catch (Exception e) {
					// TODO: handle exception
					System.err.println(stopWord+"|"+msg);
				}
			}
		}
		return msg;
	}
	

	
	//文本符号等过滤器
	/**
	 * 文本符号等过滤器
	 * @brief 文本符号等过滤器
	 * @param s 处理字符串
	 * @return String 经过文本符号过滤的原始数据
	 * @since V1.0
	 * @date 2015年7月18日 下午8:54:40
	 */
	private static String filterAll(String s){
		String regex_url = "(http\\:\\/\\/.+)"; // 匹配Url的正则表达式
		String regex_name = "@((?!=@|\\s|\\[|:).)*"; // 匹配微博名的正则表达式
		String regex_special = "[\\\\ · α.\\:\"'【】#//＂＂<>《》“” ️\\$￥：、……↓（）&()~|！\\[\\]]"; // 匹配特殊字符的正则表达式
		String regex_space = "\\s+";
		//zwei0624	
		String regex_emoji = "[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]";//emjoy表情
		String regex_16 ="/x^[0-9a-fA-F]{2}$/";
		
		s = s.replaceAll(regex_16, "");
		//去掉Emoji表情字符
		s = s.replaceAll(regex_name, "");
		s = s.replaceAll(regex_emoji, "");
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
		s=s.replaceAll("“|”|，|。|　|，|？|", "");
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
	
	/**
	 * 准备数据并构建P表，排序P表，构建L表
	 * @brief 
	 * @param single true表示读取单文件 false表示读取文件夹多个文件
	 * @throws IOException
	 * @since V1.0
	 * @date 2015年7月18日 下午8:40:43
	 */
	public void preparing(Boolean single) throws IOException{
		System.out.println("【准备步骤】");
		loadStopWord(pathStop,stopWordList);    //加载停用词
		loadStopWord(pathStop1,stopHeadWordList);    //加载停用词单字
		loadStopWord(pathStop1,stopTailWordList);    //加载停用词
		//System.out.println(stopWordList.size());
		if(data==null){			
			if(single){
				System.out.println("【读取单文件】");
				getData(path, charset);			
			}else{			
				System.out.println("【读取文件夹】");
				getDataFloder(path, charset);
		}
		}
		buildPointTable(data, PointTable);                                   //建立P表
		sortPointTable(data,PointTable);                                     //排序P表
		buildLeftTable(data,PointTable,LeftTable);                           //建立L表
	}

	/**
	 * 过滤并排序结果
	 * @brief 过滤并排序结果
	 * @param n
	 * @return void
	 * @since V1.0
	 * @date 2015年7月18日 下午8:50:14
	 */
	public void filterAndSort(int n){
		substringReduction(0.5);                                      //过滤近频子串(存部分改善)
		filterByCount(n);                                                //过滤小于N的词
		filterByRules();
		//int a=(fileCount*1)/100;
		//System.out.println("剩余词"+result.size());
		result=sortMapShow();                                          //排序结果Map
		
	}
}
