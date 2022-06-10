package com.wastecreative.wastecreative.presentation.view.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.ViewModelFactory
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.databinding.ActivityLoginBinding
import com.wastecreative.wastecreative.presentation.view.MainActivity
import com.wastecreative.wastecreative.presentation.view.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val Context.dataStores: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var _binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: UserModel
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setUpVM()
        setUpFirebase()
    }


    private fun setUpVM(){
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreferences.getInstance(dataStores))
        )[LoginViewModel::class.java]
        loginViewModel.getUser().observe(this, { user ->
            this.user = user

            if(this.user.isLogin){
                Log.d("cek","login sukses{$user}")
            }

        })
        loginViewModel.passwordEmpty.observe(this, {
            if (it) _binding.passwordEditTextLayout.setError(getString(R.string.password_error))
        })

        loginViewModel.emailValidasi.observe(this, {
            if (it) _binding.emailEditTextLayout.setError(getString(R.string.mail_error))
        })
        loginViewModel.passwordValidasi.observe(this, {
            if (!it) _binding.passwordEditTextLayout.setError(getString(R.string.password_min))
        })
    }

    private fun setUpFirebase(){
        auth= FirebaseAuth.getInstance()
        _binding.btnRegister.setOnClickListener {
            var intent =Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


        _binding.loginButton.setOnClickListener {
            if(checking()){
                val email=_binding.emailEditText.text.toString()
                val password= _binding.passwordEditText.text.toString()
                Toast.makeText(this,getString(R.string.waiting), Toast.LENGTH_LONG).show()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            loginViewModel.getLogin( email, password)
                            loginViewModel.login(UserModel("",email,true))
                            var intent =Intent(this,MainActivity::class.java)
                            intent.putExtra("email",email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, getString(R.string.valid), Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this,getString(R.string.validkosong),Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checking():Boolean
    {
        if(_binding.emailEditText.text.toString().trim{it<=' '}.isNotEmpty()
            && _binding.passwordEditText.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false
    }

}