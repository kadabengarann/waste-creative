package com.wastecreative.wastecreative.presentation.view.detailMarketplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastecreative.wastecreative.data.models.MarketplaceComment
import com.wastecreative.wastecreative.data.models.MarketplaceDetail
import com.wastecreative.wastecreative.data.models.UploadResponse
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.data.repositories.MarketplaceRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.flow.collect

class DetailMarketplaceViewModel (private val marketplaceRepository: MarketplaceRepository, private val preference: UserPreferences) : ViewModel() {

    private val _idPost = MutableLiveData<Int>()
    private lateinit var _userData: UserModel
    private val _marketplace = MutableLiveData<Result<MarketplaceDetail>>()
    val marketplaceDetail: LiveData<Result<MarketplaceDetail>> = _marketplace

    private val _comments = MutableLiveData<Result<List<MarketplaceComment>>>()
    val comments: LiveData<Result<List<MarketplaceComment>>> = _comments


    private val _uploadResult = MutableLiveData<Result<UploadResponse>?>()
    val uploadResult: LiveData<Result<UploadResponse>?> = _uploadResult

    init {
        viewModelScope.launch {
            preference.getUser().collect {
                _userData = it
            }
        }
    }
    fun detailMarketplace(param: Int) {
        _idPost.value = param
        viewModelScope.launch {
            marketplaceRepository.fetchPostDetail(param).collect {
                _marketplace.postValue(it)
            }
        }
    }

    fun fetchComments(){
        _idPost.value?.let {
            viewModelScope.launch {
                marketplaceRepository.fetchComment(it).collect {
                    _comments.postValue(it)
                }
            }

        }
    }

    fun sendComment(comment: String){
        val comment = comment.toRequestBody("text/plain".toMediaType())
        val user_id = _userData.id.toString().toRequestBody("text/plain".toMediaType())
        _idPost.value?.let {
            val post_id = it.toString().toRequestBody("text/plain".toMediaType())
            viewModelScope.launch {
                marketplaceRepository.postComment( comment, post_id, user_id).collect {result ->
                    _uploadResult.postValue(result)
                }
            }
        }
    }
}