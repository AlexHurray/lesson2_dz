package com.example.ermolaenkoalex.nytimes.utils;

import com.example.ermolaenkoalex.nytimes.dto.MultimediaDTO;
import com.example.ermolaenkoalex.nytimes.dto.ResultDTO;
import com.example.ermolaenkoalex.nytimes.model.NewsItem;
import com.example.ermolaenkoalex.nytimes.dto.MultimediaFormat;
import com.example.ermolaenkoalex.nytimes.dto.MultimediaType;

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
        if (multimediaDTO.getType() == MultimediaType.IMAGE) {
            return multimediaDTO.getUrl();
        }

        return null;
    }

    private static boolean isThumblarge(@NonNull MultimediaDTO multimediaDTO) {
        return multimediaDTO.getType() == MultimediaType.IMAGE
                && multimediaDTO.getFormat() == MultimediaFormat.THUMB_LARGE;
    }

    private NewsItemConverter() {
        throw new AssertionError("No instances");
    }
}
