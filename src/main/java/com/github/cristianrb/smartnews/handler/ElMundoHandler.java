package com.github.cristianrb.smartnews.handler;

import com.github.cristianrb.smartnews.entity.Contribution;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ElMundoHandler extends GenericHandler {

    public ElMundoHandler() {
        description = "media:description";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase(urlImage)) {
            getContribution().setUrlImage(attributes.getValue("url"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equalsIgnoreCase(item)) {
            getContribution().setSource("El Mundo");
            getContribution().setSourceUrl("www.elmundo.es");
        }
    }
}
