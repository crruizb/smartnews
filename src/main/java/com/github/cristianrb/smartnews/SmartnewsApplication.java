package com.github.cristianrb.smartnews;

import com.github.cristianrb.smartnews.rss.RSSDownloader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SmartnewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartnewsApplication.class, args);
		RSSDownloader rssDownloader = new RSSDownloader();
		rssDownloader.downloadFromAllSources();
	}

}
