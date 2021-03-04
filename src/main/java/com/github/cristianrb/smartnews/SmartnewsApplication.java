package com.github.cristianrb.smartnews;

import com.github.cristianrb.smartnews.rss.ABCDownloader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartnewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartnewsApplication.class, args);
		ABCDownloader abcdownloader = new ABCDownloader();
		abcdownloader.readXML();
	}

}
