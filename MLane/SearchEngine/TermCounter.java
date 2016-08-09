package SearchEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;


/**
 * Encapsulates a map from search term to frequency (count).
 * 
 * @author downey
 *
 */
public class TermCounter {
	
	private Map<String, Integer> map;
	private String label;
	
	public TermCounter(String label) {
		this.label = label;
		this.map = new HashMap<String, Integer>();
	}
	
	public String getLabel() {
		return label;
	}
	public int size() {
		int total = 0;
		for (Integer value: map.values()) {
			total += value;
		}
		return total;
	}

//Takes a collection of Elements and counts their words.
	public void processElements(Elements paragraphs) {
		for (Node node: paragraphs) {
			processTree(node);
		}
	}
//Finds TextNodes in a DOM tree and counts their words.
	public void processTree(Node root) {
		// NOTE: we could use select to find the TextNodes, but since
		// we already have a tree iterator, let's use it.
		for (Node node: new WikiNodeIterable(root)) {
			if (node instanceof TextNode) {
				processText(((TextNode) node).text());
			}
		}
	}
//Splits `text` into words and counts them.
	public void processText(String text) {
		// replace punctuation with spaces, convert to lower case, and split on whitespace
		String[] array = text.replaceAll("\\pP", " ").toLowerCase().split("\\s+");
		
		for (int i=0; i<array.length; i++) {
			String term = array[i];
			incrementTermCount(term);
		}
	}
//Increments the counter associated with `term`.
	public void incrementTermCount(String term) {
		// System.out.println(term);
		put(term, get(term) + 1);
	}
	public void put(String term, int count) {
		map.put(term, count);
	}
	public Integer get(String term) {
		Integer count = map.get(term);
		return count == null ? 0 : count;
	}
	public Set<String> keySet() {
		return map.keySet();
	}
	public void printCounts() {
		for (String key: keySet()) {
			Integer count = get(key);
			System.out.println(key + ", " + count);
		}
		System.out.println("Total of all counts = " + size());
	}
}
