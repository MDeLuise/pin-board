package com.github.mdeluise.pinboard.list;

import com.github.mdeluise.pinboard.page.Page;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "page_lists")
public class PageList {
    @Id
    @GenericGenerator(
        name = "IntegrationTestIdentityGenerator",
        strategy = "com.github.mdeluise.pinboard.common.IntegrationTestIdentityGenerator",
        parameters = @org.hibernate.annotations.Parameter(name = "tableName", value = "page_lists")
    )
    @GeneratedValue(generator = "IntegrationTestIdentityGenerator")
    @Column(unique = true, nullable = false)
    private Long id;
    @ManyToMany(mappedBy = "lists", cascade = CascadeType.PERSIST)
    private Set<Page> pages = new HashSet<>();
    @Column(unique = true, nullable = false)
    @NotBlank
    private String name;
    private String description;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Set<Page> getPages() {
        return pages;
    }


    public void setPages(Set<Page> pages) {
        this.pages = pages;
    }


    public void addPage(Page page) {
        pages.add(page);
    }


    public void removePage(Page page) {
        pages.remove(page);
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @PreRemove
    public void beforeRemove() {
        pages.forEach(page -> {
            page.removeList(this);
        });
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageList pageList = (PageList) o;
        return id.equals(pageList.id) && name.equals(pageList.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
