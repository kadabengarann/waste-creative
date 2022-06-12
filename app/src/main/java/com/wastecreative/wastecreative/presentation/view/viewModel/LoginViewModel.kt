package com.wastecreative.wastecreative.presentation.view.viewModel

import android.content.ContentValues
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.wastecreative.wastecreative.data.models.Response
import com.wastecreative.wastecreative.data.models.ResponseItem
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.network.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(private var preference: UserPreferences) : ViewModel() {
        private lateinit var data : ResponseItem

        private val EmailEmpty = MutableLiveData<Boolean>()
        val _EmailEmpty : LiveData<Boolean> = EmailEmpty
        private  val _passwordEmpty = MutableLiveData<Boolean>()
        val passwordEmpty : LiveData<Boolean> = _passwordEmpty
        private  val _emailValidasi = MutableLiveData<Boolean>()
        val emailValidasi : LiveData<Boolean> = _emailValidasi
        private val _passwordValidasi = MutableLiveData<Boolean>()
        val passwordValidasi : LiveData<Boolean> = _passwordValidasi


    fun getUser(): LiveData<ResponseItem> {
        return preference.getUser().asLiveData()
    }

    fun logins(responseItem: ResponseItem) {
        viewModelScope.launch {
            preference.loginPrefs(responseItem)


        }
//
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
                .enqueue(object : Callback<ResponseItem>{
                    override fun onResponse(
                        call: Call<ResponseItem>,
                        response: retrofit2.Response<ResponseItem>
                    ) {
                        val _response = response.body()
                        if(response.isSuccessful && _response != null){
                            Log.d("LoginViewModel", "sukses")
                            val data = ResponseItem(
                                "",
                                "",
                                _response.avatar,
                                "","",_response.username,true
                            )
                            logins(data)

                        } else{
                            Log.d("LoginViewModel", "Akun salah")


                        }

                    }

                    override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                        Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")

                    }

                })
        }
    }
}
