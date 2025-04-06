package com.github.cristianrb.smartnews.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ElMundoHandler extends GenericHandler {

    public ElMundoHandler() {
        description = "media:description";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase(urlImage)) {
            String image = attributes.getValue("url");
            if (checkImageFormat(image)) getContribution().setUrlImage(image);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (getContribution() != null) {
            if (qName.equalsIgnoreCase(item)) {
                getContribution().setSource("El Mundo");
                getContribution().setSourceUrl("https://www.elmundo.es");
            } else if (qName.equalsIgnoreCase(pubDate)) {
                java.util.Date date = new Date(getData().toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String format = formatter.format(date);
                getContribution().setPubDate(format);
            }
        }
    }
}
