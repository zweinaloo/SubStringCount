package com.zwei.Util;

import java.util.ArrayList;
import java.util.List;


public class Sort{
		private String[] strArray = null;
		private List<Integer> pointTable;
		private StringBuffer data;

		public StringBuffer getData() {
			return data;
		}

		public void setData(StringBuffer data) {
			this.data = data;
		}

		public Sort(String[] strArray){
			this.strArray = strArray;
		}
		
		public Sort(List<String> strList){
			this.strArray = new String[strList.size()];
			this.strArray = strList.toArray(this.strArray);
		}
		
		public Sort(List<Integer> pointTabl,StringBuffer data){
			setPointTable(pointTabl);
			setData(data);
		}
		
		public void setPointTable(List<Integer> pointTable) {
			this.pointTable = pointTable;
		}

		public List<Integer> getPointTable() {
			try{		
				QuickSort(0, pointTable.size()-1);
			}catch(NullPointerException e){
				System.out.print("null array!");
			}
			return this.pointTable;
		}	

		private int Partition1(int start, int end){
		        Integer startPoint = pointTable.get(start);
		        String baseValue = data.substring(startPoint);
		        int basePos = start;
		        for(int i = start + 1;i <= end;i++){
		            //if(CompareTwoStrings(baseValue, data.substring(i))){
		            if(baseValue.compareTo(data.substring(pointTable.get(i)))>0){
		                basePos++;
		                Swap1(basePos, i);
		            }
		        }
		        Swap1(start, basePos);
		        return basePos;
		    }

		
		private void QuickSort(int start, int end){		
			if(start < end){
				int basePos = Partition1(start, end);
				QuickSort(start, basePos-1);
				QuickSort(basePos+1, end);
			}
		}
		
		private void Swap1(int pos1,int pos2){
			Integer tmp=pointTable.get(pos1);
			pointTable.add(pos1, pointTable.get(pos2));
			pointTable.remove(pos1+1);
			pointTable.add(pos2, tmp);
			pointTable.remove(pos2+1);
			
		}
		
		public String[] getSortedArray(){
			try{
				QuickSort(0, strArray.length - 1);
			}catch(NullPointerException e){
				System.out.print("null array!");
			}
			return strArray;
		}
		
		public List<String> getSortedList(){
			try{
				QuickSort(0, strArray.length - 1);
			}catch(NullPointerException e){
				System.out.print("null array!");
			}
			return java.util.Arrays.asList(this.strArray);
		}
		
		public void show(){
			List<Integer> p = this.pointTable;
			for(Integer b :  p){
				System.out.print(data.charAt(b));
			}
			System.out.println();
		}
		
		//test

		public static void main(String[] args){
			List<Integer> pointTable1 = new ArrayList<Integer>();
			StringBuffer data = new StringBuffer();
			data.append("ajldsfjlkadsfjkladsjflkdasjfkldsajf;jewjrqiweh fjdas;kncx,mnvkdjf;asdjfk;sjdk;f");
			for(int i=0;i<data.length();i++){
				pointTable1.add(i);
			}
			Sort a = new Sort(pointTable1, data);
			pointTable1=a.getPointTable();
			System.out.println("[结果]");
			for(Integer b :  pointTable1){
				System.out.println((int)b+"|"+data.charAt(b));
			}
		
		}
	}