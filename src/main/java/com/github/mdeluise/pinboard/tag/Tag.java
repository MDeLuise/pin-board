package com.github.mdeluise.pinboard.tag;

import com.github.mdeluise.pinboard.common.IdentifiedEntity;
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
@Table(name = "tags")
public class Tag implements IdentifiedEntity<Long> {
    @Id
    @GenericGenerator(
        name = "CustomIdGenerator",
        strategy = "com.github.mdeluise.pinboard.common.CustomIdGenerator",
        parameters = @org.hibernate.annotations.Parameter(name = "tableName", value = "tags")
    )
    @GeneratedValue(generator = "CustomIdGenerator")
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String name;
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.PERSIST)
    private Set<Page> pages = new HashSet<>();


    @Override
    public Long getId() {
        return id;
    }


    @Override
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


    @PreRemove
    public void beforeRemove() {
        pages.forEach(page -> {
            page.removeTag(this);
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
        Tag tag = (Tag) o;
        return id.equals(tag.id) && name.equals(tag.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
