package com.github.mdeluise.pinboard.tag;

import com.github.mdeluise.pinboard.page.Page;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GenericGenerator(
        name = "IntegrationTestIdentityGenerator",
        strategy = "com.github.mdeluise.pinboard.common.IntegrationTestIdentityGenerator",
        parameters = @org.hibernate.annotations.Parameter(name = "tableName", value = "tags")
    )
    @GeneratedValue(generator = "IntegrationTestIdentityGenerator")
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Page> pages = new HashSet<>();


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return id.equals(tag.id) && name.equals(tag.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
