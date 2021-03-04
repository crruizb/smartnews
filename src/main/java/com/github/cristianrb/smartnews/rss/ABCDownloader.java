package com.github.cristianrb.smartnews.rss;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.handler.ABCHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ABCDownloader extends DefaultHandler {

    private String xmlUrl = "https://www.abc.es/rss/feeds/abcPortada.xml";

    public void readXML() {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ABCHandler handler = new ABCHandler();
            saxParser.parse(new InputSource(new URL(xmlUrl).openStream()), handler);
            //Get Employees list
            List<Contribution> contributionList = handler.getContributionList();
            //print employee information
            for (Contribution contribution : contributionList)
                System.out.println(contribution);
        } catch (ParserConfigurationException | SAXException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
