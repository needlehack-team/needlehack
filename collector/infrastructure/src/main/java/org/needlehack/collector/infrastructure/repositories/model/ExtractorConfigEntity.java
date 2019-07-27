package org.needlehack.collector.infrastructure.repositories.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = ExtractorConfigEntity.TABLE_NAME)
public class ExtractorConfigEntity implements Serializable {

    private static final long serialVersionUID = 513104478912645703L;

    public static final String TABLE_NAME = "extractor_config";

    public static final String ORIGIN_COLUMN_NAME = "origin";
    public static final String SELECTOR_CONTENT_COLUMN_NAME = "selector_content";
    public static final String SELECTOR_CATEGORIES_COLUMN_NAME = "selector_categories";

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = ORIGIN_COLUMN_NAME, unique = true)
    private String origin;

    @Column(name = SELECTOR_CONTENT_COLUMN_NAME)
    private String selectorContent;

    @Column(name = SELECTOR_CATEGORIES_COLUMN_NAME)
    private String selectorCategories;

    public ExtractorConfigEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSelectorContent() {
        return selectorContent;
    }

    public void setSelectorContent(String selectorContent) {
        this.selectorContent = selectorContent;
    }

    public String getSelectorCategories() {
        return selectorCategories;
    }

    public void setSelectorCategories(String selectorCategories) {
        this.selectorCategories = selectorCategories;
    }

    @Override
    public String toString() {
        return "ExtractorConfigEntity{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", selectorContent='" + selectorContent + '\'' +
                ", selectorCategories='" + selectorCategories + '\'' +
                '}';
    }
}
