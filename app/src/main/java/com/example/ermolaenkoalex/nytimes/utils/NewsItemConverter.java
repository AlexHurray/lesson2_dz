package com.example.ermolaenkoalex.nytimes.utils;

import com.example.ermolaenkoalex.nytimes.dto.MultimediaDTO;
import com.example.ermolaenkoalex.nytimes.dto.ResultDTO;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;
import com.example.ermolaenkoalex.nytimes.ui.newslist.ContentFormat;
import com.example.ermolaenkoalex.nytimes.ui.newslist.ContentType;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class NewsItemConverter {

    @NonNull
    public static NewsItem resultDTO2NewsItem(@NonNull ResultDTO resultDTO) {

        String imageUrl = getPreviewImageURL(resultDTO);
        String subsection = resultDTO.getSubsection();
        String category = (subsection != null && !subsection.isEmpty())
                ? subsection
                : resultDTO.getSection();

        return new NewsItem(resultDTO.getTitle(),
                imageUrl,
                category,
                resultDTO.getPublishedDate(),
                resultDTO.getAbstractText(),
                resultDTO.getUrl());
    }

    @Nullable
    private static String getPreviewImageURL(@NonNull ResultDTO resultDTO) {

        if (resultDTO.getMultimedia() == null || resultDTO.getMultimedia().isEmpty()) {
            return null;
        }

        List<MultimediaDTO> mediaObjects = resultDTO.getMultimedia();

        for (MultimediaDTO multimediaDTO : mediaObjects) {
            if (isThumblarge(multimediaDTO)) {
                return multimediaDTO.getUrl();
            }
        }

        MultimediaDTO multimediaDTO = mediaObjects.get(0);
        ContentType type = multimediaDTO.getType();
        if (type != null && type == ContentType.IMAGE) {
            return multimediaDTO.getUrl();
        }

        return null;
    }

    private static boolean isThumblarge(@NonNull MultimediaDTO multimediaDTO) {
        ContentType type = multimediaDTO.getType();
        ContentFormat format = multimediaDTO.getFormat();
        return type != null && type == ContentType.IMAGE && format != null && format == ContentFormat.THUMB_LARGE;
    }

    private NewsItemConverter() {
        throw new AssertionError("No instances");
    }
}
