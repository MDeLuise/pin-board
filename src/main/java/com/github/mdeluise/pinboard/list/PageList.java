package com.github.mdeluise.pinboard.list;

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
@Table(name = "page_lists")
public class PageList {
    @Id
    @GenericGenerator(
        name = "UseExistingIdOtherwiseGenerateUsingIdentity",
        strategy = "com.github.mdeluise.pinboard.common.UseExistingIdOtherwiseGenerateUsingIdentity"
    )
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    @Column(unique = true, nullable = false)
    private Long id;
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
}
