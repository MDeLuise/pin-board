package com.github.mdeluise.pinboard.tag;

import com.github.mdeluise.pinboard.page.Page;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GenericGenerator(
        name = "UseExistingIdOtherwiseGenerateUsingIdentity",
        strategy = "com.github.mdeluise.pinboard.common.UseExistingIdOtherwiseGenerateUsingIdentity"
    )
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String name;
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
}
