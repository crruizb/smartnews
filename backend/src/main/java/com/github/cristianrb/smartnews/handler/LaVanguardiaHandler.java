package com.github.cristianrb.smartnews.handler;

import com.github.cristianrb.smartnews.util.FillWithZero;
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
            String image = attributes.getValue("source");
            if (checkImageFormat(image)) getContribution().setUrlImage(image);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equalsIgnoreCase(item)) {
            getContribution().setSource("La Vanguardia");
            getContribution().setSourceUrl("https://www.lavanguardia.com");
            getContribution().setCountry("ES");
        } else if (qName.equalsIgnoreCase(pubDate)) {
            String date = getData().toString().split("T")[0];
            String time = getData().toString().split("T")[1];
            String hour = FillWithZero.fillWithZero(Integer.parseInt(time.split(":")[0]));
            String minute = FillWithZero.fillWithZero(Integer.parseInt(time.split(":")[1]));

            String dateTime = date + " " + hour + ":" + minute;
            getContribution().setPubDate(dateTime);
        } else if (qName.equalsIgnoreCase(title)) {
            getContribution().setTitle(cleanText(getData().toString()));
        } else if (qName.equalsIgnoreCase(description)) {
            getContribution().setDescription(cleanText(getData().toString()));
        }
    }

}
