package com.example.demo.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URI;

@Entity
@Table(name = "link")
public class Link  implements Serializable {
    public Link() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private URI href;
    private String text;
    private String type;
    @ManyToOne
    private Metadata metadata;

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
