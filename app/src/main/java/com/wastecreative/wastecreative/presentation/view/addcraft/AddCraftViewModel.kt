package com.wastecreative.wastecreative.presentation.view.addcraft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastecreative.wastecreative.data.models.request.CraftRequest
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
    val request:LiveData<CraftRequest>  = _request

    private val _uploadResult = MutableLiveData<Result<UploadResponse>?>()
    val uploadResult: LiveData<Result<UploadResponse>?> = _uploadResult
    private lateinit var _userData: UserModel
    var _name :String =""
    val _materials = ArrayList<String>()
    val _tools = ArrayList<String>()
    val _steps = ArrayList<String>()

    init {
        viewModelScope.launch {
            preference.getUser().collect {
                _userData = it
            }
        }
    }

    fun clearData(){
        _name = ""
        _image.value = null
        _request.value = null
        _materials.clear()
        _tools.clear()
        _steps.clear()
    }
    fun setImage(file: File?){
        _image.value = file
    }
    fun setMats(mats: ArrayList<String>){
        _request.value?.materials?.clear()
        _request.value?.materials?.addAll(mats)
    }

    fun setTools(tools: ArrayList<String>){
        _request.value?.tools?.clear()
        _request.value?.tools?.addAll(tools)
    }
    fun setSteps(steps: ArrayList<String>){
        _request.value?.steps?.clear()
        _request.value?.steps?.addAll(steps)
    }
    fun setName(name: String){
        _name = name
    }

    fun uploadCraft(){
        val file = reduceFileImage(_image.value as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "foto",
            file.name,
            requestImageFile
        )

        val name = _name.toString().toRequestBody("text/plain".toMediaType())
        val materials = _materials
        val tools = _tools
        val steps = _steps
        val video = _request.value?.video.toString().toRequestBody("text/plain".toMediaType())
        val user_id = _userData.id.toString().toRequestBody("text/plain".toMediaType())

        viewModelScope.launch {
            craftRepository.postCraft(imageMultipart, name, materials, tools, steps, video, user_id).collect {
                _uploadResult.postValue(it)
            }
        }
    }
}