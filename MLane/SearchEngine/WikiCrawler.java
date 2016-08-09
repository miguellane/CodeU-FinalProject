package SearchEngine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;


public class WikiCrawler {
	private final String source;
	private JedisIndex index;
	private Queue<String> queue = new LinkedList<String>();

	private long lastRequestTime = -1;
	private long minInterval = 1000;

	public WikiCrawler(String source, JedisIndex index) {
		this.source = source;
		this.index = index;
		queue.offer(source);
	}

	public int queueSize() {
		return queue.size();	
	}

	public String crawl() throws IOException {
		Elements paragraphs;
		String url = queue.poll();
		if(url == null)
			return null;
		else if(index.isIndexed(url))
			return null;
		else{
			paragraphs = fetchWikipedia(url);
		}

		index.indexPage(url, paragraphs);
		queueInternalLinks(paragraphs);
		return url;
	}
	void queueInternalLinks(Elements paragraphs) {	
		for(Element paragraph: paragraphs){
			Elements links = paragraph.select("[href]");
			for(Element link: links){
				String newUrl = link.attr("href");
				if(newUrl.startsWith("/wiki/")){
					newUrl = "https://en.wikipedia.org" + newUrl;
					queue.offer(newUrl);
				}
			}
		}
	}

	public Elements fetchWikipedia(String url) throws IOException {
		if (lastRequestTime != -1) {
			long currentTime = System.currentTimeMillis();
			long nextRequestTime = lastRequestTime + minInterval;
			if (currentTime < nextRequestTime) {
				try {
					//System.out.println("Sleeping until " + nextRequestTime);
					Thread.sleep(nextRequestTime - currentTime);
				} catch (InterruptedException e) {
					System.err.println("Warning: sleep interrupted in fetchWikipedia.");
				}
			}
		}
		lastRequestTime = System.currentTimeMillis();


		// download and parse the document
		Connection conn = Jsoup.connect(url);
		Document doc = conn.get();

		Element content = doc.getElementById("mw-content-text");
		Elements paras = content.select("p");
		return paras;
	}


	public void run() throws IOException {

		Elements paragraphs = fetchWikipedia(source);
		queueInternalLinks(paragraphs);
		String res;
		do {
			res = crawl();
		} while (res == null);
		
//		Map<String, Integer> map = index.getCounts("the");
//		for (Entry<String, Integer> entry: map.entrySet()) {
//			System.out.println(entry);
//		}
	}
}
