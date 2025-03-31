package com.github.cristianrb.smartnews.handler;

import org.xml.sax.SAXException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ABCHandler extends GenericHandler {

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (getContribution() != null) {
            if (qName.equalsIgnoreCase(title)) {
                getContribution().setTitle(getData().toString());
            } else if (qName.equalsIgnoreCase(link)) {
                getContribution().setLink(getData().toString());
            } else if (qName.equalsIgnoreCase(description)) {
                setDescriptionAndImage();
            } else if (qName.equalsIgnoreCase(category)) {
                getContribution().getCategories().add(getData().toString());
            } else if (qName.equalsIgnoreCase(pubDate)) {
                java.util.Date date = new Date(getData().toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String format = formatter.format(date);
                getContribution().setPubDate(format);
            } else if (qName.equalsIgnoreCase(creator)) {
                getContribution().setCreator(getData().toString());
            } else if (qName.equalsIgnoreCase(item)) {
                getContributionList().add(getContribution());
                getContribution().setSource("ABC");
                getContribution().setSourceUrl("www.abc.es");
            }
        }
    }

    private void setDescriptionAndImage() {
        String dataDesc = getData().toString();
        int startOfDescription = setImage(dataDesc);
        if (!dataDesc.endsWith(getContribution().getUrlImage() + "\">")) {
            String description = dataDesc.substring(startOfDescription);
            description = description.replace("href=\"/", "href=\"https://www.abc.es/");
            description = description.replace("data-src=\"/", "src=\"https://www.abc.es/");
            description = description.replace("data-src=", "src=");
            description = cleanText(description);
            getContribution().setDescription(description);
        }

    }

    private int setImage(String dataDesc) {
        String imageExtension = null;
        int endOfImage = -1;
        int startOfImage = -1;
        if (dataDesc.contains("src=")) {
            startOfImage = dataDesc.indexOf("src=") + "src=".length()+1;
            endOfImage = dataDesc.indexOf("\">");
            if (startOfImage > endOfImage) return 0;
            String urlImage = dataDesc.substring(startOfImage, endOfImage);
            if (checkImageFormat(urlImage)) getContribution().setUrlImage(urlImage);
        }
        return endOfImage+2;
    }


}
