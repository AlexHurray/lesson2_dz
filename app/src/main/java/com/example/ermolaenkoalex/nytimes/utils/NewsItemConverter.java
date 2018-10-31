package com.example.ermolaenkoalex.nytimes.utils;

import com.example.ermolaenkoalex.nytimes.dto.MultimediaDTO;
import com.example.ermolaenkoalex.nytimes.dto.ResultDTO;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsItemConverter {

    private static final String IMAGE_TYPE = "image";
    private static final String THUMBLARGE_FORMAT = "thumbLarge";

    @NonNull
    public static NewsItem resultDTO2NewsItem(@NonNull ResultDTO resultDTO) {

        String imageUrl = getPreviewImageURL(resultDTO);
        String subsection = resultDTO.getSubsection();
        String category = (subsection != null && !subsection.isEmpty())
                ? subsection
                : resultDTO.getSection();

        return new NewsItem(resultDTO.getTitle()
                , imageUrl
                , category
                , resultDTO.getPublishedDate()
                , resultDTO.getAbstractText()
                , resultDTO.getUrl());
    }

    @Nullable
    private static String getPreviewImageURL(@NonNull ResultDTO resultDTO) {

        if (resultDTO.getMultimedia() == null || resultDTO.getMultimedia().isEmpty()) {
            return null;
        }

        List<MultimediaDTO> mediaObjects = resultDTO.getMultimedia();

        // Usually we need to get second record
        if (mediaObjects.size() > 2) {
            MultimediaDTO multimediaDTO = mediaObjects.get(1);
            if (isThumblarge(multimediaDTO)) {
                return multimediaDTO.getUrl();
            }
        }

        for (MultimediaDTO multimediaDTO : mediaObjects) {
            if (isThumblarge(multimediaDTO)) {
                return multimediaDTO.getUrl();
            }
        }

        MultimediaDTO multimediaDTO = mediaObjects.get(0);
        if (multimediaDTO.getType().equalsIgnoreCase(IMAGE_TYPE)) {
            return multimediaDTO.getUrl();
        }

        return null;
    }

    private static boolean isThumblarge(@NonNull MultimediaDTO multimediaDTO) {
        return multimediaDTO.getType().equalsIgnoreCase(IMAGE_TYPE)
                && multimediaDTO.getFormat().equalsIgnoreCase(THUMBLARGE_FORMAT);
    }

    private NewsItemConverter() {
        throw new AssertionError("No instances");
    }
}
