package com.blog.medium.model;

import javax.persistence.*;
import com.blog.medium.model.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = true, nullable = false)
    public Long id;

    public String title;

    public String content;

    @Column(name = "published_at", updatable = false)
    public LocalDateTime publishedAt;

  /*  @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    public Date updatedAt = new Date();*/

    @CreatedDate
    @Column(updatable = false)
    private Date createDateTime;

    @LastModifiedDate
    private Date updateDateTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , insertable = true , updatable = false)
    private User user;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "postCategory",
            joinColumns = { @JoinColumn(name = "post_id", unique = false) },
            inverseJoinColumns = { @JoinColumn(name = "category_id", unique = false) })
    private Set<Category> categories = new HashSet<>();

    public Set getCategories() {
        return categories;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Post() {

    }
}
