package de.retit.puzzle.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MultiThreadingUtil {

	public static <T> List<List<T>> chunkList(List<T> list, int chunkCount) {
		if(chunkCount <= 1) {
			return Collections.singletonList(list);
		}
		
		int chunkSize = (int) Math.ceil((double) list.size() / chunkCount);
		
		List<List<T>> result = new ArrayList<>();
		for(int i = 0; i < chunkCount; i++) {
			int fromIndex = i * chunkSize;
			int toIndex = (i+1) * chunkSize;
			toIndex = Math.min(toIndex, list.size());
			
			List<T> subList = list.subList(fromIndex, toIndex);
			result.add(subList);
		}
		
		return result;
	}
	
	public static <K,V> Map<K,List<V>> joinMap(Map<K,List<V>> result, Map<K,List<V>> mapToJoin) {
		for(Entry<K, List<V>> entry : mapToJoin.entrySet()) {
			if(!result.containsKey(entry.getKey())) {
				result.put(entry.getKey(), entry.getValue());
			}
			else {
				result.get(entry.getKey()).addAll(entry.getValue());
			}
		}
		return result;
	}
}
