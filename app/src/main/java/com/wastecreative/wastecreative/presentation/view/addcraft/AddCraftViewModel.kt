package com.wastecreative.wastecreative.presentation.view.addcraft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastecreative.wastecreative.data.models.CraftRequest
import com.wastecreative.wastecreative.data.models.UploadResponse
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.utils.reduceFileImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddCraftViewModel (private val craftRepository: CraftRepository, private val preference: UserPreferences) : ViewModel() {
    private val _image = MutableLiveData<File>()
    val image:LiveData<File> = _image
    private val _request = MutableLiveData<CraftRequest>()

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
        _image.value = null
        _request.value = null
    }
    fun setImage(file: File?){
        _image.value = file
    }
    fun setDetailCraft(name: String, desc: String){
        _request.value = CraftRequest(
            name,
            desc,
            null,
            null,
            null,
            null
        )
    }
    fun setStepsCraft(tools: String, steps: String, video: String?){
        _request.value?.tools = tools
        _request.value?.materials = tools
        _request.value?.steps = steps
        _request.value?.video = video

        uploadCraft()
    }

    private fun uploadCraft(){
        val file = reduceFileImage(_image.value as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "foto",
            file.name,
            requestImageFile
        )

        val name = _request.value?.name.toString().toRequestBody("text/plain".toMediaType())
        val materials = _request.value?.materials.toString().toRequestBody("text/plain".toMediaType())
        val tools = _request.value?.tools.toString().toRequestBody("text/plain".toMediaType())
        val steps = arrayListOf(_request.value?.steps.toString(), _request.value?.steps.toString(), _request.value?.steps.toString())
        val video = _request.value?.video.toString().toRequestBody("text/plain".toMediaType())
        val user_id = _userData.id.toString().toRequestBody("text/plain".toMediaType())

        viewModelScope.launch {
            craftRepository.postCraft(imageMultipart, name, materials, tools, steps, video, user_id).collect {
                _uploadResult.postValue(it)
            }
        }
    }
}