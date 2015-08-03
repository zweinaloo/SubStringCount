package com.zwei.utils;

import java.util.*;



public class MapSortDemo {
	/**
	 * @deprecated
	 * @test
	 * @brief {TODO}
	 * @param args
	 * @return void
	 * @since V1.0
	 * @date 2015年7月18日 下午3:52:51
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new TreeMap<String, Integer>();
		map.put("KFC", 123);
		map.put("WNBA", 321);
		map.put("NBA", 421);
		map.put("CBA", 23);
		/*Map<String, Integer> resultMap = sortMapByValue(map); //按Value进行排序
		for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}*/
	}
	
	/**
	 * 使用 Map按value进行排序
	 * @param map
	 * @return
	 */
	public Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(entryList, new MapValueComparator());
		Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
		Map.Entry<String, Integer> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
	
	public Map<String, Integer> sortMapByValue1(Map<String, Integer> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(entryList, new MapValueComparator());
		Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
		Map.Entry<String, Integer> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
	
}

//比较器类
