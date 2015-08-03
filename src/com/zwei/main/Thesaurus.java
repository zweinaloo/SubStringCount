package com.zwei.main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.imageio.stream.IIOByteBuffer;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.zwei.utils.FileUtils;
import com.zwei.utils.Filter;
import com.zwei.utils.MapSortDemo;
import com.zwei.utils.Word;



public class Thesaurus {

	private Hashtable<String, Integer> compoundHeader = new Hashtable<String, Integer>();   //复合词 首表
	private Hashtable<String, Integer> compoundTail = new Hashtable<String, Integer>();     //复合词 词尾表
	private Hashtable<String, Integer> Independent = new Hashtable<String, Integer>();      //独立词表
	private Hashtable<String, Integer> singleWord = new Hashtable<String, Integer>();       //单字表
	
	public static List<Word> impWordList = new ArrayList<Word>(); //独立概率词库


	//获取数据
	public void getDataFloder(String path,String charset) throws IOException{
		Filter filter = new Filter();
		File parentPath = new File(path);
		File[] fileList = parentPath.listFiles();
		String sentenceList = new String();
		try{
			for(File file:fileList){
				//System.out.println(file.getAbsolutePath());
				File testSetFile = new File(file.getAbsolutePath());
				FileInputStream fileInputStream = new FileInputStream(testSetFile);		
				DataInputStream read = new DataInputStream(fileInputStream);
				BufferedReader br = new BufferedReader(new InputStreamReader(read, charset));
				String line;
				while((line=br.readLine())!=null){
					line=filter.filterAll(line);
					//line=filterStopWord(line,stopWordList);
					if(line!=null&&line.length()>0){
						sentenceList+=(line);
					}
				}
				br.close();
				read.close();
				fileInputStream.close();
				if(!sentenceList.equals(null)){
				IKSegmenter(sentenceList);}
				sentenceList = new String();
			}			
		}catch(Exception e){
			e.printStackTrace();
		}


	}

	//保存数据进Hashtable 词库
	private void saveData(String word,Hashtable<String, Integer> wordList){
		if(wordList.containsKey(word)){
			int count = wordList.get(word)+1;
			wordList.put(word, count);
		}else {				
			wordList.put(word, 1);
		}
	}


	//统计 字频
	private void count(String word){
		if(word!=null&&word.length()>0){
			//统计独立单字 出现频率
			char[] word1 = word.toCharArray();
			for(char c:word1){
				String tmp=String.valueOf(c);
				saveData(tmp, singleWord);
			}

			if(word.length()==1){
				//System.out.println(word);
				saveData(word, Independent);
			}
			//统计单字 词首 和词尾 频率
			else{
				String head = word.substring(0,1);
				String tail = word.substring(word.length()-1);
				saveData(head, compoundHeader);
				saveData(tail, compoundTail);
			}
		}
	}

	//IK分词方法
	public void IKSegmenter(String text){
		// 创建分词对象  
		StringReader reader = new StringReader(text);

		IKSegmenter ik = new IKSegmenter(reader,true);// 当为true时，分词器进行最大词长切分
		Lexeme lexeme = null;
		try {
			while((lexeme = ik.next())!=null){
				count(lexeme.getLexemeText());}	
		} catch (IOException e) {
			e.printStackTrace();
		} finally{

			reader.close();
		}
	}

	private void filter(){
		//1.如果某单字出现在 复合词首表或者复合词尾表中，则
		Set<String> WordSet = Independent.keySet();
		int count=0;
//		for(String word : WordSet){
//			if(compoundHeader.contains(word)||compoundTail.contains(word)){
//				Independent.remove(word);
//				count++;
//			}
//		}
		IWPcalculate(singleWord);
		System.out.println(impWordList.size());
		Comparator<Word> comparator = new Comparator<Word>() {
	        public int compare(Word s1, Word s2) {
	                return s2.getIWP().compareTo(s1.getIWP());
	        }
	    };
		Collections.sort(impWordList,comparator);
		/*System.out.println(count);
		System.out.println("-------------------独立词---------------------");
		Mapsort(Independent);
		System.out.println("-------------------词首---------------------");
		Mapsort(compoundHeader);
		System.out.println("-------------------词尾---------------------");
		Mapsort(compoundTail);
		System.out.println("-------------------单字---------------------");
		Mapsort(singleWord);*/
		//Mapsort(Independent);
		

	}

	
    
	

	
	private void IWPcalculate(Map<String, Integer> map){
		for(String word:map.keySet()){	
			double Nword;
			if(Independent.containsKey(word)){
				Nword= Independent.get(word);				
			}else {
				Nword= 0;
			}
			double Nc=singleWord.get(word);
			double IWP= Nword/(Nc*1.0);
			Word w = new Word(word,IWP);
			impWordList.add(w);
		}
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args)  {
		Thesaurus thesaurus = new Thesaurus();
		//String pathFloder = "D:\\Zwei1\\测试&数据\\热词发现测试数据\\4";
		String pathFloder = "D:\\Zwei1\\测试&数据\\微博数据\\#王林#";
		try {
			thesaurus.getDataFloder(pathFloder, "utf-8");
			thesaurus.filter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(thesaurus.compoundHeader.size()+"|"+thesaurus.compoundTail.size()+"|"+thesaurus.Independent.size()+"|"+thesaurus.singleWord.size());
		
		String s= new String();
		for(Word w: thesaurus.impWordList){
			//System.out.println(w.getCout()+":"+w.getIWP());
			s+=w.getCout()+":"+w.getIWP()+"\r\n";
		}
		
		System.out.println(s.length());
		
		
		
		String filePath = "C://Users//JiaHui//workspace//SubStringCount//src//iwp.txt";
		FileUtils fileUtils = new FileUtils();
		fileUtils.WriteStringToFile5(filePath, s);
		System.out.println("写入完成");
		
		


	}


}
