package com.example.ermolaenkoalex.nytimes.dto;

import com.example.ermolaenkoalex.nytimes.ui.newslist.ContentFormat;
import com.example.ermolaenkoalex.nytimes.ui.newslist.ContentType;
import com.google.gson.annotations.SerializedName;

public class MultimediaDTO {

    @SerializedName("url")
    private String url;

    @SerializedName("type")
    private ContentType type;

    @SerializedName("format")
    private ContentFormat format;

    public String getUrl() {
        return url;
    }

    public ContentType getType() {
        return type;
    }

    public ContentFormat getFormat() {
        return format;
    }
}
