package SearchEngine;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.PriorityQueue;





public class Retriever {
	
// map from URLs that contain the term(s) to relevance score
	private Map<String, Integer> map;
	public Retriever(String term, JedisIndex index){
		Map<String, Integer> map = index.getCounts(term);
		this.map = map;
	}


	public Retriever(Map<String, Integer> map) {
		this.map = map;
	}

	public Integer getRelevance(String url) {
		Integer relevance = map.get(url);
		return relevance==null ? 0: relevance;
	}
	

	public  void print() {
		List<Entry<String, Integer>> entries = sort();
		for (Entry<String, Integer> entry: entries) {
			System.out.println(entry);
		}
	}
	
	public Retriever or(Retriever that) {
		Map<String, Integer> temp = map;
		for(String key: that.map.keySet()){
			if(temp.containsKey(key)){
				temp.put(key,map.get(key)+that.map.get(key));			
			}else{


				temp.put(key, that.map.get(key));
			}
		}
		return new Retriever(temp);
	}
	
	public Retriever and(Retriever that) {
	Map<String, Integer> temp = new HashMap<>();
		for(String key: that.map.keySet()){
			if(map.containsKey(key)){
				temp.put(key,map.get(key)+that.map.get(key));			
			}
		}
		return new Retriever(temp);
	}
	
	public Retriever minus(Retriever that) {
		Map<String, Integer> temp = map;
		for(String key: that.map.keySet()){
			if(temp.containsKey(key))
				temp.remove(key);
		}
		return new Retriever(temp);
	}
	public List<Entry<String, Integer>> sort() {
		List<Entry<String,Integer>> list = new LinkedList<Entry<String,Integer>>();
		list.addAll(map.entrySet());
//Merge Sort, Single List		
		Collections.sort(list, comparator);
//Heap Sort, Ability to Grab Top 10
/*		int i = list.size();
		PriorityQueue<Entry<String,Integer>> queue = new PriorityQueue<Entry<String,Integer>>(i,comparator);
		for(Entry<String,Integer> element: list){
			queue.offer(element);
		}
		list.clear();
		for(i = i; i != 0; i--){
			list.add(queue.poll());
		}
*/		return list;
	}
	Comparator <Entry<String,Integer>> comparator= new Comparator<Entry<String,Integer>>(){	
		@Override
		public int compare(Entry<String,Integer> entry1, Entry<String,Integer> entry2){
			if(entry1.getValue() > entry2.getValue())
				return 1;
			if(entry1.getValue() < entry2.getValue())
				return -1; 
			return 0;		
		}	
	};

	public static Retriever search(String term, JedisIndex index) {
		Map<String, Integer> map = index.getCounts(term);
		return new Retriever(map);
	}
}
