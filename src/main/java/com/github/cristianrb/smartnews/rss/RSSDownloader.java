package com.github.cristianrb.smartnews.rss;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.handler.ABCHandler;
import com.github.cristianrb.smartnews.handler.ElMundoHandler;
import com.github.cristianrb.smartnews.handler.ElPaisHandler;
import com.github.cristianrb.smartnews.handler.GenericHandler;
import javafx.util.Pair;
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

    public RSSDownloader() {
        sources = new ArrayList<Pair<String, GenericHandler>>();
        sources.add(new Pair<String, GenericHandler>("https://www.abc.es/rss/feeds/abcPortada.xml", new ABCHandler()));
        //sources.add(new Pair<String, GenericHandler>("https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", new ElPaisHandler()));
        //sources.add(new Pair<String, GenericHandler>("https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml", new ElMundoHandler()));
        //sources.add(new Pair<String, GenericHandler>("https://www.lavanguardia.com/newsml/home.xml", new ABCHandler()));
    }

    public void downloadFromAllSources() {
        for (Pair<String, GenericHandler> pair : sources) {
            readXml(pair.getKey(), pair.getValue());
        }

    }

    public void readXml(String xmlUrl, GenericHandler handler) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new InputSource(new URL(xmlUrl).openStream()), handler);

            List<Contribution> contributionList = handler.getContributionList();

            for (Contribution contribution : contributionList)
                System.out.println(contribution);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

}
