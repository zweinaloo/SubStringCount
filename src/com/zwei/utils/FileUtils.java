package com.zwei.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

	 public  void WriteStringToFile5(String filePath,String s) {
	        try {
	            FileOutputStream fos = new FileOutputStream(filePath);
	            OutputStreamWriter writer = new OutputStreamWriter(fos, "utf-8");
	            writer.append(s);
	            writer.close();
	            fos.close();
	            System.out.println("Ok"+s);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	 
	 public  void getData(String path,String charset) throws IOException{
			File file = new File(path);
			FileInputStream inputStream = new FileInputStream(file);
			DataInputStream read = new DataInputStream(inputStream);

			BufferedReader br = new BufferedReader(new InputStreamReader(read,charset));
			StringBuffer sb = new StringBuffer();
			String line;
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			br.close();
			read.close();
			inputStream.close();
		}
	 
	 public Map<String, Double> loadWord(String path,String charset) throws FileNotFoundException, IOException{
		 File file = new File(path);
			FileInputStream inputStream = new FileInputStream(file);
			DataInputStream read = new DataInputStream(inputStream);

			BufferedReader br = new BufferedReader(new InputStreamReader(read,charset));
			StringBuffer sb = new StringBuffer();
			String line;
			List<Word> words = new ArrayList<Word>();
		    Map<String, Double> wordMap = new HashMap<String, Double>();
			while((line=br.readLine())!=null){
				int index = line.indexOf(":");
				String cout = line.substring(0,index);
				Double value = Double.valueOf(line.substring(index+1));
//				Word word = new Word(cout,value);
//				words.add(word);
				wordMap.put(cout, value);
				
			}
			br.close();
			read.close();
			inputStream.close();
			
		    return wordMap;
	 }
}
