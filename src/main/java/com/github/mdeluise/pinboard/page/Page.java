package com.github.mdeluise.pinboard.page;

import com.github.mdeluise.pinboard.list.PageList;
import com.github.mdeluise.pinboard.page.body.PageBody;
import com.github.mdeluise.pinboard.tag.Tag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pages")
public class Page implements Serializable {
    @Id
    @GenericGenerator(
        name = "IntegrationTestIdentityGenerator",
        strategy = "com.github.mdeluise.pinboard.common.IntegrationTestIdentityGenerator",
        parameters = @org.hibernate.annotations.Parameter(name = "tableName", value = "pages")
    )
    @GeneratedValue(generator = "IntegrationTestIdentityGenerator")
    @Column(unique = true, nullable = false)
    private Long id;
    private String title;
    private String url;
    @OneToOne(mappedBy = "page", cascade = CascadeType.ALL)
    private PageBody body;
    @Column(unique = true)
    private Long pageBodyId;
    private String headerImgUrl;
    @ManyToMany(
        cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
        }
    )
    @JoinTable(
        name = "page_tags",
        joinColumns = @JoinColumn(name = "page_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Set<Tag> tags = new HashSet<>();
    @ManyToMany(
        cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
        }
    )
    @JoinTable(
        name = "page_page_lists",
        joinColumns = @JoinColumn(name = "page_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "page_list_id", referencedColumnName = "id")
    )
    private Set<PageList> lists = new HashSet<>();


    public Page() {
    }


    public Page(Page page) {
        this.id = page.getId();
        this.url = page.getUrl();
        this.headerImgUrl = page.getHeaderImgUrl();
        this.body = page.getBody();
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


    public PageBody getBody() {
        return body;
    }


    public void setBody(PageBody body) {
        this.body = body;
    }


    public Long getPageBodyId() {
        return pageBodyId;
    }


    public void setPageBodyId(Long pageBodyId) {
        this.pageBodyId = pageBodyId;
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
        return id.equals(page.id) && url.equals(page.url);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
