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
                getContributionList().add(new Contribution(getContribution()));
                getContribution().setSource("ABC");
                getContribution().setSourceUrl("www.abc.es");
            }
        }
    }

    private void setDescriptionAndImage() {
        String dataDesc = getData().toString();
        String imageExtension = setImage(dataDesc);
        setDescription(dataDesc, imageExtension);
    }

    private void setDescription(String dataDesc, String imageExtension) {
        if (imageExtension != null) {
            String[] dataSplit = dataDesc.split(imageExtension + "\">");
            if (dataSplit.length > 1) {
                getContribution().setDescription(dataSplit[1]);
            }

        } else {
            getContribution().setDescription(dataDesc);
        }
    }

    private String setImage(String dataDesc) {
        String imageExtension = null;
        if (dataDesc.contains("src=")) {
            if (dataDesc.contains("JPG")) {
                imageExtension = "JPG";
            } else if (dataDesc.contains("jpg")) {
                imageExtension = "jpg";
            } else if (dataDesc.contains("jpeg")) {
                imageExtension = "jpeg";
            } else if (dataDesc.contains("png")) {
                imageExtension = "png";
            }
            String urlImage = dataDesc.split("src=" + "\"")[1].split(imageExtension + "\">")[0] + imageExtension;
            getContribution().setUrlImage(urlImage);
        }
        return imageExtension;
    }


}
