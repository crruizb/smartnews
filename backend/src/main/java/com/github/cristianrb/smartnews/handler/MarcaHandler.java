package com.github.cristianrb.smartnews.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MarcaHandler extends GenericHandler {

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (getContribution() != null) {
            if (qName.equalsIgnoreCase(urlImage)) {
                String image = attributes.getValue("url");
                if (image != null && checkImageFormat(image)) getContribution().setUrlImage(image);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (getContribution() != null) {
            super.endElement(uri, localName, qName);
            if (qName.equalsIgnoreCase(item)) {
                getContribution().setSource("Marca");
                getContribution().setSourceUrl("https://www.marca.com/");
            } else if (qName.equalsIgnoreCase(pubDate)) {
                Date date = new Date(getData().toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String format = formatter.format(date);
                getContribution().setPubDate(format);
            } else if (qName.equalsIgnoreCase(description)) {
                getContribution().setDescription(cleanText(getData().toString().split("&nbsp;<a href")[0]));
            }
        }
    }
}
