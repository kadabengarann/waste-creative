package com.wastecreative.wastecreative.presentation.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.model.preference.UserModel
import com.wastecreative.wastecreative.databinding.ActivityRegisterBinding
import com.wastecreative.wastecreative.presentation.view.viewModel.RegisterViewModel


class RegisterActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityRegisterBinding
    private lateinit var registerViewModel : RegisterViewModel
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
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
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
                            registerViewModel.getRegister(this@RegisterActivity,name,email,password)
                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this){
                                        task->
                                    if(task.isSuccessful)
                                    {
                                        Users.document(email).set(user)
                                        val intent=Intent(this,LoginActivity::class.java)
                                        Toast.makeText(this,getString(R.string.waiting), Toast.LENGTH_SHORT).show()
                                        intent.putExtra("email",email)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else
                                    {
                                        Toast.makeText(this,"Authentication Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else
                        {
                            Toast.makeText(this,getString(R.string.isempty), Toast.LENGTH_LONG).show()

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


