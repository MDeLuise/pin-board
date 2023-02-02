package com.github.mdeluise.pinboard.page.body;

import com.github.mdeluise.pinboard.page.Page;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "pages_body")
public class PageBody implements Serializable {
    @Id
    private Long id;
    @Lob
    @NotBlank
    private String content;
    @OneToOne
    @MapsId
    private Page page;



    public PageBody(Page page) {
        this.page = page;
    }


    public PageBody() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public Page getPage() {
        return page;
    }


    public void setPage(Page page) {
        this.page = page;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageBody pageBody = (PageBody) o;
        return id.equals(pageBody.id) && content.equals(pageBody.content);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }
}
