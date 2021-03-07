package com.github.cristianrb.smartnews.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LaVanguardiaHandler extends GenericHandler {

    public LaVanguardiaHandler() {
        item = "newsitem";
        title = "headline";
        creator = "byline";
        urlImage = "media-reference";
        pubDate = "dateline";
        link = "deriveredfrom";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase(urlImage)) {
            getContribution().setUrlImage(attributes.getValue("source"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equalsIgnoreCase(item)) {
            getContribution().setSource("La Vanguardia");
            getContribution().setSourceUrl("www.lavanguardia.com");
        }
    }
}
