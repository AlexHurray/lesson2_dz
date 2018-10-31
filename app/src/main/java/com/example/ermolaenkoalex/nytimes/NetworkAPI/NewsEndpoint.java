package com.example.ermolaenkoalex.nytimes.NetworkAPI;

import com.example.ermolaenkoalex.nytimes.dto.ResultsDTO;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsEndpoint {

    @GET("{section}.json")
    Single<Response<ResultsDTO>> getNews(@Path("section") @NonNull String section);
}
