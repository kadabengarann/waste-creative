package com.wastecreative.wastecreative.presentation.view.viewModel

import android.content.ContentValues
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastecreative.wastecreative.data.models.ResponseItem
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.data.models.UploadResponse
import com.wastecreative.wastecreative.data.models.postUserResponse
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.network.ApiConfig
import com.wastecreative.wastecreative.data.repositories.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


import retrofit2.Call
import retrofit2.Callback

class RegisterViewModel(private var userRepository: UserRepository) : ViewModel() {
    private val _sukses = MutableLiveData<Boolean>()
    val sukses : LiveData<Boolean> = _sukses
    private  val _passwordEmpty = MutableLiveData<Boolean>()
    val passwordEmpty : LiveData<Boolean> = _passwordEmpty
    private  val _emailValidasi = MutableLiveData<Boolean>()
    val emailValidasi : LiveData<Boolean> = _emailValidasi
    private val _passwordValidasi = MutableLiveData<Boolean>()
    val passwordValidasi : LiveData<Boolean> = _passwordValidasi
    private val _nameEmpty = MutableLiveData<Boolean>()
    val nameEmpty : LiveData<Boolean> = _nameEmpty

    private val _regSuccess = MutableLiveData<Result<postUserResponse>?>()
    val regSuccess : LiveData<Result<postUserResponse>?> = _regSuccess

    fun postUserRegister(name: String, email: String, password: String){
        val avatar = ""
        viewModelScope.launch {
            userRepository.postUserData( name, email, password,avatar ).collect {result ->
                _regSuccess.postValue(result)
            }
        }

    }
    private fun checkEmptyForm(names: String , email: String,password: String): Boolean{
        var notEmpty = true

        if ( names.isEmpty()){
            _nameEmpty.value = true
            notEmpty = false
        }
        else if ( email.isEmpty()){
            _emailValidasi.value = true
            notEmpty = false
        }
        else if (password.isEmpty()){
            _passwordEmpty.value = true
            notEmpty = false
        }
        return notEmpty


    }

    private fun formValidasi(names:String, email : String , password : String) : Boolean{
        var cekValid = true

        if ( checkEmptyForm(names,email, password)){
            if (!emailValidasi(email)){
                _emailValidasi.value = false
                cekValid = false
            }
            else if(!passwordValidasi(password)){
                _passwordValidasi.value = false
                cekValid = false
            }

        }
        else cekValid = false
        return cekValid
    }

    private fun emailValidasi(email : String) : Boolean{
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
    private fun passwordValidasi(password : String) : Boolean {
        return password.length  >= 6

    }

    fun getRegister(names1: Context, names: String, email: String, password: String){
        if(formValidasi(names,email, password)){
            ApiConfig.getApiService()
                .postRegister(names,email, password)
                .enqueue(object : Callback<ResponseItem> {
                    override fun onResponse(
                        call: Call<ResponseItem>,
                        response: retrofit2.Response<ResponseItem>
                    ) {
                        val _response = response.body()
                        if(response.isSuccessful && _response != null){
                            _sukses.value = false
                            Log.d("LoginViewModel", "Reg Berhasil ,{$names},{$email}")





                        } else{
                            if (email.equals(email)){
                                Log.d("LoginViewModel", "email telah di gunakan")

                            }
                        }

                    }

                    override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                        Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")

                    }

                })
        }
    }
}