package com.example.ermolaenkoalex.nytimes.ui.newslist;

import com.google.gson.annotations.SerializedName;

public enum ContentFormat {
    @SerializedName("Standard Thumbnail") ST_THUMBNAIL,
    @SerializedName("thumbLarge") THUMB_LARGE,
    @SerializedName("Normal") NORMAL,
    @SerializedName("mediumThreeByTwo210") MEDIUM_THREE_BY_TWO_210,
    @SerializedName("superJumbo") SUPER_JUMBO
}
