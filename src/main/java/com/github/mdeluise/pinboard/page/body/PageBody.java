package com.github.mdeluise.pinboard.page.body;

import com.github.mdeluise.pinboard.page.Page;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Entity
@Table(name = "pages_body")
public class PageBody implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    @NotBlank
    private String content;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id", referencedColumnName = "id")
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
}
