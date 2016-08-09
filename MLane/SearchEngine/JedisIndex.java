package SearchEngine;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * Represents a Redis-backed web search index.
 * 
 */
public class JedisIndex {

	private Jedis jedis;

	public JedisIndex(Jedis jedis) {
		this.jedis = jedis;
	}
	
	private String urlSetKey(String term) {
		return "URLSet:" + term;
	}
	private String termCounterKey(String url) {
		return "TermCounter:" + url;
	}

//Function methods
	public boolean isIndexed(String url) {
		String redisKey = termCounterKey(url);
		return jedis.exists(redisKey);
	}
	
	public void add(String term, TermCounter tc) {
		jedis.sadd(urlSetKey(term), tc.getLabel());
	}

	public Set<String> getURLs(String term) {
		Set<String> set = jedis.smembers(urlSetKey(term));
		return set;
	}

	public Map<String, Integer> getCounts(String term) {
		List<String> urls = new ArrayList<String>();
		urls.addAll(getURLs(term));
		Transaction t = jedis.multi();
		for (String url: urls) {
			String redisKey = termCounterKey(url);
			t.hget(redisKey, term);
		}
		List<Object> res = t.exec();
		Map<String, Integer> map = new HashMap<String, Integer>();
		int i = 0;
		for (String url: urls) {
//			System.out.println(url);
			Integer count = new Integer((String) res.get(i++));
			map.put(url, count);
		}
		return map;
	}

	public Integer getCount(String url, String term) {
		String redisKey = termCounterKey(url);
		String count = jedis.hget(redisKey, term);
		return new Integer(count);
	}

	public void indexPage(String url, Elements paragraphs) {
		System.out.println("Indexing " + url);
		
		// make a TermCounter and count the terms in the paragraphs
		TermCounter tc = new TermCounter(url);
		tc.processElements(paragraphs);
		
		// push the contents of the TermCounter to Redis
		pushTermCounterToRedis(tc);
	}
	public List<Object> pushTermCounterToRedis(TermCounter tc) {
		Transaction t = jedis.multi();
		
		String url = tc.getLabel();
		String hashname = termCounterKey(url);
		
		// if this page has already been indexed; delete the old hash
		t.del(hashname);

		// for each term, add an entry in the termcounter and a new
		// member of the index
		for (String term: tc.keySet()) {
			Integer count = tc.get(term);
			t.hset(hashname, term, count.toString());
			t.sadd(urlSetKey(term), url);
		}
		List<Object> res = t.exec();
		return res;
	}
//Testing methods.
	public Set<String> termSet() {
		Set<String> keys = urlSetKeys();
		Set<String> terms = new HashSet<String>();
		for (String key: keys) {
			String[] array = key.split(":");
			if (array.length < 2) {
				terms.add("");
			} else {
				terms.add(array[1]);
			}
		}
		return terms;
	}
	public Set<String> urlSetKeys() {
		return jedis.keys("URLSet:*");
	}
	public Set<String> termCounterKeys() {
		return jedis.keys("TermCounter:*");
	}
	public int urlNum(){
//		return jedis.llen("TermCounter:");
		return termCounterKeys().size();
 	}
	public void deleteURLSets() {
		Set<String> keys = urlSetKeys();
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}
	public void deleteTermCounters() {
		Set<String> keys = termCounterKeys();
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}
	public void deleteAllKeys() {
		Set<String> keys = jedis.keys("*");
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}

}
