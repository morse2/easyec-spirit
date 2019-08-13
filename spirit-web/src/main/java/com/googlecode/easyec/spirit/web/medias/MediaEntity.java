package com.googlecode.easyec.spirit.web.medias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "path", "name", "lastModified" })
public class MediaEntity implements Serializable {

    private static final long serialVersionUID = -3001328394519561341L;
    @JsonProperty
    private String path;
    @JsonProperty
    private String name;
    @JsonProperty
    private long lastModified;
    @JsonProperty
    private long length;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
