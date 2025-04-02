package com.github.cristianrb.smartnews.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiarioHandler extends GenericHandler {

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase(urlImage)) {
            String image = attributes.getValue("url");
            String type = attributes.getValue("type");
            if (checkImageFormat(type)) getContribution().setUrlImage(image);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equalsIgnoreCase(item)) {
            getContribution().setSource("ES Diario");
            getContribution().setSourceUrl("https://www.esdiario.com/");
        } else if (qName.equalsIgnoreCase(pubDate)) {
            Date date = new Date(getData().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String format = formatter.format(date);
            getContribution().setPubDate(format);
        }
    }
}
