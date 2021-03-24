package com.github.cristianrb.smartnews.handler;

import com.github.cristianrb.smartnews.entity.Contribution;
import org.xml.sax.SAXException;

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
                getContribution().setPubDate(getData().toString());
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
            getContribution().setDescription(dataDesc.substring(startOfDescription));
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
            getContribution().setUrlImage(urlImage);
        }
        return endOfImage+2;
    }


}
