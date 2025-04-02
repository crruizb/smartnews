package com.github.cristianrb.smartnews.rss;

import com.github.cristianrb.smartnews.config.SpringContextConfig;
import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.handler.*;
import com.github.cristianrb.smartnews.service.contributions.ContributionsService;
import com.github.cristianrb.smartnews.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static java.rmi.server.LogStream.log;

@Service
public class RSSDownloader {

    private static final Logger log = LoggerFactory.getLogger(RSSDownloader.class);
    private List<Pair<String, GenericHandler>> sources;
    private ContributionsService contributionsService;

    public RSSDownloader() {

    }

    // Disabled in order to not overflow the database.
    @Scheduled(fixedRate=900000) // Every hour = (60*60*1000=3600000), minute = 60000, 15 mins = 900000
    public void downloadFromAllSources() {
        this.contributionsService = SpringContextConfig.getBean(ContributionsService.class);
        sources = new ArrayList<Pair<String, GenericHandler>>();
        sources.add(new Pair<>("https://www.abc.es/rss/feeds/abcPortada.xml", new ABCHandler()));
        sources.add(new Pair<>("https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", new ElPaisHandler()));
        sources.add(new Pair<>("https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml", new ElMundoHandler()));
        sources.add(new Pair<>("https://www.esdiario.com/rss/home.xml", new DiarioHandler()));
        sources.add(new Pair<>("https://e00-marca.uecdn.es/rss/portada.xml", new MarcaHandler()));
        List<Contribution> contributionsFromAllSources = new ArrayList<Contribution>();
        System.out.println("Start downloading news...");
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
            log.error("Error saving contribution: {}", e.getMessage());
        }
    }

    public List<Contribution> downloadNews(String xmlUrl, GenericHandler handler) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Contribution> contributionList = null;
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            URL url = new URL(xmlUrl);
            URLConnection conn = url.openConnection();
            saxParser.parse(conn.getInputStream(), handler);

            contributionList = handler.getContributionList();
        } catch (Exception e) {
            log.error("Error downloading news: {}", e.getMessage());
            // Continue reading news
        }
        return contributionList;
    }

    private void logTime() {
        System.out.println("RSSDownloader finished: " + System.currentTimeMillis());
    }

}
