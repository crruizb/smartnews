package com.github.cristianrb.smartnews.entity;

import java.util.ArrayList;

public class Contribution {

    private String title;
    private String description;
    private String link;
    private String category;
    private ArrayList<String> keywords;
    private String pubDate;
    private String creator;
    private String urlImage;

    public Contribution() {

    }

    public Contribution(String title, String description, String link, String category,
                        ArrayList<String> keywords, String pubDate, String creator, String urlImage) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.category = category;
        this.keywords = keywords;
        this.pubDate = pubDate;
        this.creator = creator;
        this.urlImage = urlImage;
    }

    public Contribution(Contribution contribution) {
        this.title = contribution.title;
        this.description = contribution.description;
        this.link = contribution.link;
        this.category = contribution.category;
        this.keywords = contribution.keywords;
        this.pubDate = contribution.pubDate;
        this.creator = contribution.creator;
        this.urlImage = contribution.urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return "Contribution{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", category='" + category + '\'' +
                ", keywords=" + keywords +
                ", pubDate='" + pubDate + '\'' +
                ", creator='" + creator + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
