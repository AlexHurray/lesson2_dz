package com.example.ermolaenkoalex.nytimes.ui.newslist;

import com.example.ermolaenkoalex.nytimes.R;

public enum Section {

    Home("home", R.string.section_home),
    Opinion("opinion", R.string.section_opinion),
    World("world", R.string.section_world),
    National("national", R.string.section_national),
    Politics("politics", R.string.section_politics),
    Upshot("upshot", R.string.section_upshot),
    NYregion("nyregion", R.string.section_nyregion),
    Business("business", R.string.section_business),
    Technology("technology", R.string.section_technology),
    Science("science", R.string.section_science),
    Health("health", R.string.section_health),
    Sports("sports", R.string.section_sports),
    Arts("arts", R.string.section_arts),
    Books("books", R.string.section_books),
    Movies("movies", R.string.section_movies),
    Theater("theater", R.string.section_theater),
    Sundayreview("sundayreview", R.string.section_sundayreview),
    Fashion("fashion", R.string.section_fashion),
    Tmagazine("tmagazine", R.string.section_tmagazine),
    Food("food", R.string.section_food),
    Travel("travel", R.string.section_travel),
    Magazine("magazine", R.string.section_magazine),
    Realestate("realestate", R.string.section_realestate),
    Automobiles("automobiles", R.string.section_automobiles),
    Obituaries("obituaries", R.string.section_obituaries),
    Insider("insider", R.string.section_insider);

    private String sectionName;
    private int sectionNameResId;

    Section(String sectionName, int sectionNameResId) {
        this.sectionName = sectionName ;
        this.sectionNameResId = sectionNameResId ;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getSectionNameResId() {
        return sectionNameResId;
    }
}
