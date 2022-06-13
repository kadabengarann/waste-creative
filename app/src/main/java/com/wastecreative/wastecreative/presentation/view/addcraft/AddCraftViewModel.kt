package com.wastecreative.wastecreative.presentation.view.addcraft

import android.util.Log
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
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlinx.coroutines.flow.collect


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
        _uploadResult.value = null
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
        val materials = _materials
        val tools = _tools
        val steps = _steps
        val requestBodyMats = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart(
                "foto",
                file.name,
                requestImageFile
            )
            addFormDataPart("nama", _name)
            addFormDataPart("video", _request.value?.video.toString())
            addFormDataPart("pengguna_id", _userData.id.toString())
            materials.forEach{
                addFormDataPart("bahan[]", it)
            }
            tools.forEach{
                addFormDataPart("alat[]", it)
            }
            steps.forEach{
                addFormDataPart("langkah[]", it)
            }
        }.build()

        viewModelScope.launch {
            craftRepository.postCraft(requestBodyMats).collect {
                _uploadResult.postValue(it)
            }
        }
    }
}