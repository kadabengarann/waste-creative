package com.wastecreative.wastecreative.presentation.view.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.ViewModelFactory
import com.wastecreative.wastecreative.data.models.ResponseItem
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.databinding.ActivityLoginBinding
import com.wastecreative.wastecreative.presentation.view.MainActivity
import com.wastecreative.wastecreative.presentation.view.profile.ProfileActivity
import com.wastecreative.wastecreative.presentation.view.viewModel.LoginViewModel
import com.wastecreative.wastecreative.presentation.view.viewModel.RegisterViewModel

class LoginActivity : AppCompatActivity() {
    private val Context.dataStores: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var _binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var responseItem: ResponseItem
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setUpVM()
        setUpFirebase()
//        setupAction()
    }


    private fun setUpVM(){
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreferences.getInstance(dataStores))
        )[LoginViewModel::class.java]

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
    private fun setupAction() {
//
        _binding.loginButton.setOnClickListener {
            val emailUsers = _binding.emailEditText.text.toString().trim()
            val passwordUsers = _binding.passwordEditText.text.toString().trim()
            loginViewModel.getLogin( emailUsers, passwordUsers)


            Toast.makeText(this, "Tunggu Sebentar", Toast.LENGTH_SHORT).show()
            loginViewModel.getUser().observe(this, { user ->
                this.responseItem = user
//                                loginViewModel.getLogin( email, password)
                loginViewModel.logins(
                    ResponseItem(
                        "", passwordUsers, user.avatar, user.id, emailUsers, user.username, true

                    )
                )
//                Intent(this@LoginActivity,ProfileActivity::class.java).let {
//                    it.putExtra(ProfileActivity.avatarObject, user.avatar)
//                    it.putExtra(ProfileActivity.usernameObject,user.username)
//                    Log.d(
//                        "cek_item",
//                        "ini adalah item ${responseItem.avatar} , ${responseItem.username}"
//                    )
//                    startActivity(it)
//                    finish()
//                }

            })

        }


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
                val email=_binding.emailEditText.text.toString().trim()
                val password= _binding.passwordEditText.text.toString().trim()
                Toast.makeText(this,getString(R.string.waiting), Toast.LENGTH_LONG).show()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            loginViewModel.getUser().observe(this@LoginActivity, { user ->
                                this.responseItem = user
                                loginViewModel.getLogin( email, password)
                                loginViewModel.logins(
                                    ResponseItem(
                                        "",user.password,user.avatar,user.id,user.email,user.username,true

                                    )
                                )
                                AlertDialog.Builder(this).apply {
                                    setTitle("Sukses")
                                    setMessage("Login Berhasil")
                                    setPositiveButton("Oke") { _, _ ->
                                        Log.d("cek","login sukses{${user.avatar}}")
                                        Intent(this@LoginActivity,MainActivity::class.java).let {
                                            it.putExtra(ProfileActivity.avatarObject,responseItem.avatar)
                                            it.putExtra(ProfileActivity.usernameObject,user.username)
//                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            Log.d("cek_item","ini adalah item ${responseItem.avatar } , ${responseItem.username}")
                                            startActivity(it)
                                            finish()
                                        }
                                    }
                                    show()
                                }


                            })
                        } else {
                            AlertDialog.Builder(this).apply {
                                setTitle("Gagal")
                                setMessage("Login Gagal , ${getString(R.string.valid)}")
                                setPositiveButton("Oke") { _, _ ->
                                    Intent(this@LoginActivity,LoginActivity::class.java).let {
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(it)
                                        finish()
                                    }
                                }
                                show()
                            }
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