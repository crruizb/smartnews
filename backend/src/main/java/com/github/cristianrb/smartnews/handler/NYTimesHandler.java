package com.github.cristianrb.smartnews.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NYTimesHandler extends GenericHandler {

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
                getContribution().setSource("NY Times");
                getContribution().setSourceUrl("https://www.nytimes.com/");
                getContribution().setCountry("EN");
            } else if (qName.equalsIgnoreCase(pubDate)) {
                Date date = new Date(getData().toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String format = formatter.format(date);
                getContribution().setPubDate(format);
            }
        }
    }
}
