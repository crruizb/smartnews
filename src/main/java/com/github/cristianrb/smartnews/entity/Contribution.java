package com.github.cristianrb.smartnews.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Contribution {

    private Integer id;
    private String title;
    private String description;
    private String link;
    private List<String> categories;
    private String pubDate;
    private String creator;
    private String urlImage;
    private String source;
    private String sourceUrl;

    public Contribution() {
        this.categories = new ArrayList<String>();
    }

    public Contribution(Integer id, String title, String description, String link, ArrayList<String> categories,
                        String pubDate, String creator, String urlImage, String source, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.categories = categories;
        this.pubDate = pubDate;
        this.creator = creator;
        this.urlImage = urlImage;
        this.source = source;
        this.sourceUrl = sourceUrl;
    }

    public Contribution(Contribution contribution) {
        this.id = contribution.id;
        this.title = contribution.title;
        this.description = contribution.description;
        this.link = contribution.link;
        this.categories = contribution.categories;
        this.pubDate = contribution.pubDate;
        this.creator = contribution.creator;
        this.urlImage = contribution.urlImage;
        this.source = contribution.source;
        this.sourceUrl = contribution.sourceUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategories() {
        return categories;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        return "Contribution{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", categories=" + categories +
                ", pubDate='" + pubDate + '\'' +
                ", creator='" + creator + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", source='" + source + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contribution that = (Contribution) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public static Comparator<Contribution> contributionComparator = new Comparator<Contribution>() {

        public int compare(Contribution c1, Contribution c2) {
            return c2.getPubDate().compareTo(c1.getPubDate());
        }};
}
