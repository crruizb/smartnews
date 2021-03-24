package com.github.cristianrb.smartnews.rss;

import com.github.cristianrb.smartnews.config.SpringContextConfig;
import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.handler.*;
import com.github.cristianrb.smartnews.service.contributions.ContributionsService;
import com.github.cristianrb.smartnews.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class RSSDownloader {

    private List<Pair<String, GenericHandler>> sources;
    private ContributionsService contributionsService;

    public RSSDownloader() {

    }

    @Scheduled(fixedRate=60000) // Every hour (60*60*1000=3600000)
    public void downloadFromAllSources() {
        this.contributionsService = SpringContextConfig.getBean(ContributionsService.class);
        sources = new ArrayList<Pair<String, GenericHandler>>();
        sources.add(new Pair<String, GenericHandler>("https://www.abc.es/rss/feeds/abcPortada.xml", new ABCHandler()));
        sources.add(new Pair<String, GenericHandler>("https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", new ElPaisHandler()));
        sources.add(new Pair<String, GenericHandler>("https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml", new ElMundoHandler()));
        sources.add(new Pair<String, GenericHandler>("https://www.lavanguardia.com/newsml/home.xml", new LaVanguardiaHandler()));
        List<Contribution> contributionsFromAllSources = new ArrayList<Contribution>();
        for (Pair<String, GenericHandler> pair : sources) {
            List<Contribution> contributionList = downloadNews(pair.getKey(), pair.getValue());
            if (contributionList != null) {
                contributionsFromAllSources.addAll(contributionList);
            }

        }

        saveContributionsInDB(contributionsFromAllSources);
        logTime();
    }

    private void saveContributionsInDB(List<Contribution> contributionsFromAllSources) {
        try {
            contributionsFromAllSources.forEach(cont -> this.contributionsService.saveContribution(cont));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Contribution> downloadNews(String xmlUrl, GenericHandler handler) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Contribution> contributionList = null;
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new InputSource(new URL(xmlUrl).openStream()), handler);

            contributionList = handler.getContributionList();


        } catch (Exception e) {
            // Continue reading news
        }
        return contributionList;
    }

    private void logTime() {
        System.out.println("RSSDownloader finished: " + System.currentTimeMillis());
    }

}
