package com.github.cristianrb.smartnews.handler;

import com.github.cristianrb.smartnews.entity.Contribution;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ABCHandler extends DefaultHandler {

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String DESCRIPTION = "description";
    private static final String CATEGORY = "category";
    private static final String PUBDATE = "pubDate";

    private List<Contribution> contributionList = null;
    private Contribution contribution;
    private StringBuilder data;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(ITEM)) {
            if (this.contributionList == null) {
                this.contributionList = new ArrayList<Contribution>();
            }
            this.contribution = new Contribution();
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (this.contribution != null) {
            switch (qName) {
                case TITLE:
                    this.contribution.setTitle(data.toString());
                    break;
                case LINK:
                    this.contribution.setLink(data.toString());
                    break;
                case DESCRIPTION:
                    String dataDesc = data.toString();
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
                        this.contribution.setUrlImage(urlImage);
                    }
                    if (imageExtension != null) {
                        this.contribution.setDescription(dataDesc.split(imageExtension + "\">")[1]);
                    } else {
                        this.contribution.setDescription(dataDesc);
                    }
                    break;
                case CATEGORY:
                    this.contribution.setCategory(data.toString());
                    break;
                case PUBDATE:
                    this.contribution.setPubDate(data.toString());
                    break;
                case ITEM:
                    this.contributionList.add(new Contribution(this.contribution));
                    this.contribution = null;
                    break;
            }
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    public List<Contribution> getContributionList() {
        return contributionList;
    }
}
