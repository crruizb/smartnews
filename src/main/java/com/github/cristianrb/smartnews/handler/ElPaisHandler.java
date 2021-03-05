package com.github.cristianrb.smartnews.handler;

import com.github.cristianrb.smartnews.entity.Contribution;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ElPaisHandler extends GenericHandler {

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase(urlImage)) {
            getContribution().setUrlImage(attributes.getValue("url"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (getContribution() != null) {
            if (qName.equalsIgnoreCase(title)) {
                getContribution().setTitle(getData().toString());
            } else if (qName.equalsIgnoreCase(link)) {
                getContribution().setLink(getData().toString());
            } else if (qName.equalsIgnoreCase(description)) {
                getContribution().setDescription(getData().toString());
            } else if (qName.equalsIgnoreCase(category)) {
                getContribution().setCategory(getData().toString());
            } else if (qName.equalsIgnoreCase(pubDate)) {
                getContribution().setPubDate(getData().toString());
            } else if (qName.equalsIgnoreCase(creator)) {
                getContribution().setCreator(getData().toString());
            } else if (qName.equalsIgnoreCase(item)) {
                getContributionList().add(new Contribution(getContribution()));
                setContribution(null);
            }
        }
    }
}
