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
import com.google.firebase.firestore.FirebaseFirestore
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.ViewModelFactory
import com.wastecreative.wastecreative.data.models.ResponseItem
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.databinding.ActivityRegisterBinding
import com.wastecreative.wastecreative.presentation.view.MainActivity
import com.wastecreative.wastecreative.presentation.view.profile.ProfileActivity
import com.wastecreative.wastecreative.presentation.view.viewModel.LoginViewModel
import com.wastecreative.wastecreative.presentation.view.viewModel.RegisterViewModel


class RegisterActivity : AppCompatActivity() {
    private val Context.dataStores: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var _binding : ActivityRegisterBinding
    private lateinit var registerViewModel : RegisterViewModel
    private lateinit var responseItem: ResponseItem
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setuUPVm()
        setUpFirebase()
    }

    private fun isSukses(value: Boolean){
        if (value){return}
        else{
            Toast.makeText(this,"Register Berhasil", Toast.LENGTH_SHORT).show()
//
        }
    }
    private fun setuUPVm(){
        registerViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreferences.getInstance(dataStores))
        )[RegisterViewModel::class.java]

        registerViewModel.sukses.observe(this,{
            isSukses(it)

        })


        registerViewModel.nameEmpty.observe(this,{
            if(it) _binding.nameEditTextLayout.setError(getString(R.string.name_error))
        })

        registerViewModel.passwordEmpty.observe(this,{
            if (it) _binding.passwordEditTextLayout.setError(getString(R.string.password_error))
        })

        registerViewModel.emailValidasi.observe(this,{
            if (it) _binding.emailEditTextLayout.setError(getString(R.string.mail_error))
        })
        registerViewModel.passwordValidasi.observe(this,{
            if(!it) _binding.passwordEditTextLayout.setError(getString(R.string.password_min))
        })
    }

    private fun setUpFirebase() {

        _binding.registerLogin.setOnClickListener {
            var intent =Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        auth= FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()
        _binding.signupButton.setOnClickListener {
            if(checking())
            {

                var email=_binding.emailEditText.text.toString()
                var password= _binding.passwordEditText.text.toString()
                var name=_binding.nameEditText.text.toString()
                Toast.makeText(this,getString(R.string.waiting), Toast.LENGTH_LONG).show()

                val user= hashMapOf(
                    "Name" to name,
                    "email" to email
                )
                val Users=db.collection("USERS")
                val query =Users.whereEqualTo("email",email).get()
                    .addOnSuccessListener {
                            tasks->
                        if(tasks.isEmpty)
                        {

                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this){
                                        task->
                                    if(task.isSuccessful)
                                    {
                                        registerViewModel.getRegister(this@RegisterActivity,name,email,password)
                                        registerViewModel.getUser().observe(this@RegisterActivity,
                                            { users ->
                                                this.responseItem = users
//
                                                registerViewModel.logins(
                                                    ResponseItem(
                                                        "",
                                                        password,
                                                        users.avatar,
                                                        users.id,
                                                        email,
                                                        users.username,
                                                        true

                                                    )
                                                )
                                                AlertDialog.Builder(this).apply {
                                                    setTitle("Sukses")
                                                    setMessage("Register Berhasil")
                                                    setPositiveButton("Oke") { _, _ ->
                                                        Intent(this@RegisterActivity, MainActivity::class.java).let {
                                                            it.putExtra("email",email)
                                                            it.putExtra(ProfileActivity.avatarObject,users.avatar)
                                                            it.putExtra(ProfileActivity.usernameObject,users.username)
                                                                 intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                            Log.d("cek_item","ini adalah item ${responseItem.avatar } , ${responseItem.username}")
                                                            startActivity(it)
                                                            finish()
                                                        }
                                                    }
                                                    show()
                                                }
                                                Users.document(email).set(user)

                                            })




                                    }
                                    else
                                    {
                                        Toast.makeText(this,"Authentication Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else
                        {
                            AlertDialog.Builder(this).apply {
                                setTitle("Gagal")
                                setMessage("Register Gagal, ${getString(R.string.isempty)}")
                                setPositiveButton("Oke") { _, _ ->
                                    Intent(this@RegisterActivity, RegisterActivity::class.java).let {
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
                Toast.makeText(this,getString(R.string.validkosong), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checking(): Boolean {
        if(_binding.nameEditText.text.toString().trim{it<=' '}.isNotEmpty()
            && _binding.emailEditText.text.toString().trim{it<=' '}.isNotEmpty()
            && _binding.passwordEditText.text.toString().trim{it<=' '}.isNotEmpty()
        )
        {
            return true
        }
        return false

    }
}


