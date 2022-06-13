package com.wastecreative.wastecreative.presentation.view.addMarketplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastecreative.wastecreative.data.models.request.MarketplaceRequest
import com.wastecreative.wastecreative.data.models.UploadResponse
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.data.repositories.MarketplaceRepository
import com.wastecreative.wastecreative.utils.reduceFileImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddMarketplaceViewModel (private val marketplaceRepository: MarketplaceRepository, private val preference: UserPreferences) : ViewModel() {
    private val _image = MutableLiveData<File>()
    val image:LiveData<File> = _image
    private val _request = MutableLiveData<MarketplaceRequest>()

    private val _uploadResult = MutableLiveData<Result<UploadResponse>?>()
    val uploadResult: LiveData<Result<UploadResponse>?> = _uploadResult
    private lateinit var _userData: UserModel



    init {
        viewModelScope.launch {
            preference.getUser().collect {
                _userData = it
            }
        }
    }

    fun clearData(){
        _image.postValue(null)
        _request.postValue(null)
        _uploadResult.postValue(null)
    }
    fun setImage(file: File?){
        _image.postValue(file)
    }

    fun submitDetailPost(title: String, desc: String, price: Int, address: String){
        _request.value = MarketplaceRequest(
            title,
            desc,
            price,
            address
        )
        uploadMarketplace()
    }

    private fun uploadMarketplace(){
        val file = reduceFileImage(_image.value as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "foto",
            file.name,
            requestImageFile
        )

        val title = _request.value?.title.toString().toRequestBody("text/plain".toMediaType())
        val desc = _request.value?.description.toString().toRequestBody("text/plain".toMediaType())
        val price = _request.value?.price.toString().toRequestBody("text/plain".toMediaType())
        val address = _request.value?.address.toString().toRequestBody("text/plain".toMediaType())
        val user_id = _userData.id.toString().toRequestBody("text/plain".toMediaType())

        viewModelScope.launch {
            marketplaceRepository.postMarketplace(imageMultipart, title, desc, price, address, user_id).collect {
                _uploadResult.postValue(it)
            }
        }
    }
}