package com.kalaari.entity.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.springframework.web.context.request.RequestContextHolder;

import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity<T> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    @Version
    @Column(nullable = false)
    protected Integer version;

    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "created_on", columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    protected Date createdOn;

    @Column(name = "last_updated_on", columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    protected Date lastUpdatedOn;

    @Column(name = "last_updated_by")
    protected String lastUpdatedBy;

    @Column(name = "deleted")
    protected Boolean deleted = false;

    @PrePersist
    public void beforeInsert() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            this.lastUpdatedBy = "Admin";
            this.createdBy = "Admin";
        }

        this.createdOn = new Date();
        this.lastUpdatedOn = new Date();
    }

    @PreUpdate
    public void beforeUpdate() {
        this.lastUpdatedBy = "Admin";
        this.lastUpdatedOn = new Date();
    }

}