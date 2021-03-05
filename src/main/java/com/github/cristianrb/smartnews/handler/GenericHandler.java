package com.github.cristianrb.smartnews.handler;

import com.github.cristianrb.smartnews.entity.Contribution;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class GenericHandler extends DefaultHandler  {

    protected String item = "item";
    protected String title = "title";
    protected String link = "link";
    protected String description = "description";
    protected String category = "category";
    protected String pubDate = "pubDate";
    protected String creator = "dc:creator";
    protected String urlImage = "media:content";

    private List<Contribution> contributionList = null;
    private Contribution contribution;
    private StringBuilder data;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(item)) {
            if (getContributionList() == null) {
                initializeContributionList();
            }
            initializeContribution();
        }
        initializeData();
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    public List<Contribution> getContributionList() {
        return contributionList;
    }

    public void setContributionList(List<Contribution> contributionList) {
        this.contributionList = contributionList;
    }

    public Contribution getContribution() {
        return contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
    }

    public StringBuilder getData() {
        return data;
    }

    public void setData(StringBuilder data) {
        this.data = data;
    }

    public void initializeContributionList() {
        this.contributionList = new ArrayList<Contribution>();
    }

    public void initializeContribution() {
        this.contribution = new Contribution();
    }

    public void initializeData() {
        this.data = new StringBuilder();
    }
}
