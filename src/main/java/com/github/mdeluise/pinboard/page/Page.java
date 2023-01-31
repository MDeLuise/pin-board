package com.github.mdeluise.pinboard.page;

import com.github.mdeluise.pinboard.list.PageList;
import com.github.mdeluise.pinboard.tag.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pages")
public class Page {
    @Id
    @GenericGenerator(
        name = "UseExistingIdOtherwiseGenerateUsingIdentity",
        strategy = "com.github.mdeluise.pinboard.common.UseExistingIdOtherwiseGenerateUsingIdentity"
    )
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    @Column(unique = true, nullable = false)
    private Long id;
    private String title;
    private String url;
    private String headerImgUrl;
    @ManyToMany(mappedBy = "pages")
    private Set<Tag> tags = new HashSet<>();
    @ManyToMany(mappedBy = "pages")
    private Set<PageList> lists = new HashSet<>();


    public Page() {
    }


    public Page(Page page) {
        this.id = page.getId();
        this.url = page.getUrl();
        this.headerImgUrl = page.getHeaderImgUrl();
        this.title = page.getTitle();
        this.tags = page.getTags();
        this.lists = page.getLists();
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getHeaderImgUrl() {
        return headerImgUrl;
    }


    public void setHeaderImgUrl(String headerImgUrl) {
        this.headerImgUrl = headerImgUrl;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public Set<Tag> getTags() {
        return tags;
    }


    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    public Set<PageList> getLists() {
        return lists;
    }


    public void setLists(Set<PageList> lists) {
        this.lists = lists;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Page page = (Page) o;
        return url.equals(page.url);
    }


    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
