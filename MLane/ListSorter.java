/**
 * 
 */
package com.flatironschool.javacs;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Provides sorting algorithms.
 *
 */
public class ListSorter<T> {

	/**
	 * Sorts a list using a Comparator object.
	 * 
	 * @param list
	 * @param comparator
	 * @return
	 */
	public void insertionSort(List<T> list, Comparator<T> comparator) {
	
		for (int i=1; i < list.size(); i++) {
			T elt_i = list.get(i);
			int j = i;
			while (j > 0) {
				T elt_j = list.get(j-1);
				if (comparator.compare(elt_i, elt_j) >= 0) {
					break;
				}
				list.set(j, elt_j);
				j--;
			}
			list.set(j, elt_i);
		}
	}

	/**
	 * Sorts a list using a Comparator object.
	 * 
	 * @param list
	 * @param comparator
	 * @return
	 */
	public void mergeSortInPlace(List<T> list, Comparator<T> comparator) {
		List<T> sorted = mergeSort(list, comparator);
		list.clear();
		list.addAll(sorted);
	}

	/**
	 * Sorts a list using a Comparator object.
	 * 
	 * Returns a list that might be new.
	 * 
	 * @param list
	 * @param comparator
	 * @return
	 */
	public List<T> mergeSort(List<T> list, Comparator<T> comparator) {
		if(list.size() == 1){
			return list;
		}else if(list.size() == 2){
			insertionSort(list, comparator);
			return list;		
		}
		List<T> lista = mergeSort(list.subList(0,list.size()/2), comparator);
		List<T> listb = mergeSort(list.subList(list.size()/2,list.size()), comparator);
		List<T> listc = merge(lista, listb, comparator);
	        return listc;
	}
	public List<T> merge(List<T> lista, List<T> listb, Comparator<T> comparator){
		List<T> listc = new ArrayList<T>();

		int a = 0;
		int b = 0;
		while(a < lista.size() && b < listb.size()){
			if(comparator.compare(lista.get(a), listb.get(b)) < 0){
				listc.add(lista.get(a));
				a++;
			}else{
				listc.add(listb.get(b));
				b++;
			}
		}
		while(a < lista.size()){
			listc.add(lista.get(a));
			a++;
		}
		while(b < listb.size()){
			listc.add(listb.get(b));
			b++;
		}
		return listc;
	}

	/**
	 * Sorts a list using a Comparator object.
	 * 
	 * @param list
	 * @param comparator
	 * @return
	 */
	public void heapSort(List<T> list, Comparator<T> comparator) {
		int i = list.size();
		PriorityQueue<T> queue = new PriorityQueue<T>(i,comparator);
		for(T element: list){
			queue.offer(element);
		}
		list.clear();
		while(i != 0){
			list.add(queue.poll());
			i--;
		}
	}

	
	/**
	 * Returns the largest `k` elements in `list` in ascending order.
	 * 
	 * @param k
	 * @param list
	 * @param comparator
	 * @return 
	 * @return
	 */
	public List<T> topK(int k, List<T> list, Comparator<T> comparator) {
		System.out.println(k);
		PriorityQueue<T> queue = new PriorityQueue<T>(k,comparator);
		for(T element: list){
			queue.add(element);
			if(queue.size() > k)
				queue.poll();
		}
		List<T> top = new ArrayList<T>();
		while(k != 0){
			top.add(queue.poll());
			k--;
		}
        	return top;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(3, 5, 1, 4, 2));
		
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer n, Integer m) {
				return n.compareTo(m);
			}
		};
		
		ListSorter<Integer> sorter = new ListSorter<Integer>();
		sorter.insertionSort(list, comparator);
		System.out.println(list);

		list = new ArrayList<Integer>(Arrays.asList(3, 5, 1, 4, 2));
		sorter.mergeSortInPlace(list, comparator);
		System.out.println(list);

		list = new ArrayList<Integer>(Arrays.asList(3, 5, 1, 4, 2));
		sorter.heapSort(list, comparator);
		System.out.println(list);
	
		list = new ArrayList<Integer>(Arrays.asList(6, 3, 5, 8, 1, 4, 2, 7));
		List<Integer> queue = sorter.topK(4, list, comparator);
		System.out.println(queue);
	}
}
