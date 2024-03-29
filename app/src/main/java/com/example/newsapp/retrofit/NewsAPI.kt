package com.example.newsapp.retrofit

import com.example.newsapp.data.entities.NewsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale.IsoCountryCode

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String="tr",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ) : Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ) : Response<NewsResponse>



}