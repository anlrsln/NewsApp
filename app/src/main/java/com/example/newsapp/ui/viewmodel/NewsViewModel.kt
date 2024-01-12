package com.example.newsapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.entities.Article
import com.example.newsapp.data.entities.NewsResponse
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.util.Constants.Companion.COUNTRY_CODE
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository) : ViewModel() {

    // API'den gelen response'u tutacağımız live data
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val newsPage = 1
    val searchNewsPage = 1


    //viewmodel nesnesi oluşturulduğunda API'den verileri çekmek için ilk init çalışır
    init {
        getBreakingNews(COUNTRY_CODE)
    }


    // breakingNews verilerini almak için kullanılan metot. Asenkron çalıştırmak için CoroutineScope kullanır
    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode,newsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }
    // searchNews verilerini almak için kullanılan metot
    fun getSearchNews(searchQuery:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery,searchNewsPage)
        breakingNews.postValue(handleSearchNewsResponse(response))
    }


    // response dönüşünü kontrol eder. Başarılı dönerse livedata'ya aktarmak için Resource.Source() cevabını döner,
    // başarılı değilse Resource.Error(response.message()) mesajını döner
    private fun handleBreakingNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    // response dönüşünü kontrol eder. Başarılı dönerse livedata'ya aktarmak için Resource.Source() cevabını döner,
    // başarılı değilse Resource.Error(response.message()) mesajını döner
    private fun handleSearchNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    // Article'yi favoriye almak için kullanılan metot
    fun favoriteArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    // Favori article'leri getiren metot
    fun getFavoriteArticles() = repository.getFavoriteNews()

    // Favori Article silmek için kullanılan metot
    fun deleteNews(article: Article)=viewModelScope.launch {
        repository.deleteNews(article)
    }


}