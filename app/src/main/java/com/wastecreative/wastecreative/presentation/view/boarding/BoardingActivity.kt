package com.wastecreative.wastecreative.presentation.view.boarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wastecreative.wastecreative.databinding.ActivityBoardingBinding
import com.wastecreative.wastecreative.presentation.auth.LoginActivity

class BoardingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAction()

    }
    private fun setAction(){
        binding.boardingButton.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

}