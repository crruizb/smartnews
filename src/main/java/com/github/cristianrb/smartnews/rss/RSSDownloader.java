package com.github.cristianrb.smartnews.rss;

import com.github.cristianrb.smartnews.config.SpringContextConfig;
import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.handler.*;
import com.github.cristianrb.smartnews.repository.ContributionsRepository;
import com.github.cristianrb.smartnews.service.contributions.ContributionsService;
import com.github.cristianrb.smartnews.service.contributions.impl.ContributionsServiceImpl;
import javafx.util.Pair;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSDownloader {

    private final List<Pair<String, GenericHandler>> sources;
    private ContributionsService contributionsService;

    public RSSDownloader() {
        this.contributionsService = SpringContextConfig.getBean(ContributionsService.class);
        sources = new ArrayList<Pair<String, GenericHandler>>();
        sources.add(new Pair<String, GenericHandler>("https://www.abc.es/rss/feeds/abcPortada.xml", new ABCHandler()));
        sources.add(new Pair<String, GenericHandler>("https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", new ElPaisHandler()));
        sources.add(new Pair<String, GenericHandler>("https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml", new ElMundoHandler()));
        sources.add(new Pair<String, GenericHandler>("https://www.lavanguardia.com/newsml/home.xml", new LaVanguardiaHandler()));
    }

    public void downloadFromAllSources() {
        List<Contribution> contributionsFromAllSources = new ArrayList<Contribution>();
        for (Pair<String, GenericHandler> pair : sources) {
            List<Contribution> contributionList = downloadNews(pair.getKey(), pair.getValue());
            contributionsFromAllSources.addAll(contributionList);
        }

        saveContributionsInDB(contributionsFromAllSources);
    }

    private void saveContributionsInDB(List<Contribution> contributionsFromAllSources) {
        try {
            contributionsFromAllSources.forEach((cont) -> this.contributionsService.saveContribution(cont));
        } catch (Exception e) {
            // Do nothing
        }
    }

    public List<Contribution> downloadNews(String xmlUrl, GenericHandler handler) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Contribution> contributionList = null;
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new InputSource(new URL(xmlUrl).openStream()), handler);

            contributionList = handler.getContributionList();


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return contributionList;
    }

}
