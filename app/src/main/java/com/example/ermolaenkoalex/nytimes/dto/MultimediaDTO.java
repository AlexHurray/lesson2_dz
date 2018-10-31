package com.example.ermolaenkoalex.nytimes.dto;

import com.google.gson.annotations.SerializedName;

public class MultimediaDTO {

    @SerializedName("url")
    private String url;

    @SerializedName("type")
    private String type;

    @SerializedName("format")
    private String format;

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }
}
