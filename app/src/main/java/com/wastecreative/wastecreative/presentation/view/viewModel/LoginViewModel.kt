package com.wastecreative.wastecreative.presentation.view.viewModel

import android.content.ContentValues
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.wastecreative.wastecreative.data.models.MarketplaceDetail
import com.wastecreative.wastecreative.data.models.Response
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.models.userResponse
import com.wastecreative.wastecreative.data.network.ApiConfig
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.data.repositories.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import kotlinx.coroutines.flow.collect

class LoginViewModel(private var preference: UserPreferences, private var userRepository: UserRepository) : ViewModel() {
        private val EmailEmpty = MutableLiveData<Boolean>()
        val _EmailEmpty : LiveData<Boolean> = EmailEmpty
        private  val _passwordEmpty = MutableLiveData<Boolean>()
        val passwordEmpty : LiveData<Boolean> = _passwordEmpty
        private  val _emailValidasi = MutableLiveData<Boolean>()
        val emailValidasi : LiveData<Boolean> = _emailValidasi
        private val _passwordValidasi = MutableLiveData<Boolean>()
        val passwordValidasi : LiveData<Boolean> = _passwordValidasi

    private val _userLogin = MutableLiveData<Result<userResponse>>()
    val userLogin: LiveData<Result<userResponse>> = _userLogin

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }



    fun login(userModel: UserModel) {
        viewModelScope.launch {
            preference.loginPref(userModel)
        }
    }

    private fun checkEmptyForm(email : String , password : String): Boolean{
        var notEmpty = true

        if ( email.length == 0){
            EmailEmpty.value = true
            notEmpty = false
        }
        else if (password.length == 0){
            _passwordEmpty.value = true
            notEmpty = false
        }
        return notEmpty
    }

    private fun formValidasi( email : String , password : String) : Boolean{
        var cekValid = true

        if ( checkEmptyForm(email, password )){
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

    fun getLogin(email: String, password: String){
        if(formValidasi(email, password,)){
            ApiConfig.getApiService()
                .getLogin(email, password)
                .enqueue(object : Callback<Response>{
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        val _response = response.body()
                        if(response.isSuccessful && _response != null){
                            Log.d("LoginViewModel", "Login Berhasil")
//                            login(UserModel("",email,true))


                        } else{
                            Log.d("LoginViewModel", "Akun salah")


                        }

                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")

                    }

                })
        }
    }
    fun fetchUserLogin(email: String, password: String) {
        viewModelScope.launch {
            userRepository.fetctUserLoginData(email, password).collect {
                _userLogin.postValue(it)
            }
        }
    }
}
