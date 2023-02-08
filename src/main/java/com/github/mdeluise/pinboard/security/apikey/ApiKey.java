package com.github.mdeluise.pinboard.security.apikey;

import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.common.IdentifiedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "api_keys")
public class ApiKey implements IdentifiedEntity<Long> {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(
        name = "CustomIdGenerator", strategy = "com.github.mdeluise.pinboard.common.CustomIdGenerator",
        parameters = @org.hibernate.annotations.Parameter(name = "tableName", value = "api_keys")
    )
    @Column(unique = true, nullable = false)
    private Long id;
    @NotNull
    @NotBlank
    @Column(unique = true)
    private String name;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "api_key_value")
    @NotNull
    @Length(min = 25)
    private String value;


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


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }
}
