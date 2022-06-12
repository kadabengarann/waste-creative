package com.wastecreative.wastecreative.presentation.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.auth.User
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.ViewModelFactory
import com.wastecreative.wastecreative.data.models.Response
import com.wastecreative.wastecreative.data.models.ResponseItem
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.presentation.adapter.SectionProfilAdapter


import com.wastecreative.wastecreative.databinding.ActivityProfileBinding
import com.wastecreative.wastecreative.presentation.view.MainActivity
import com.wastecreative.wastecreative.presentation.view.auth.LoginActivity
import com.wastecreative.wastecreative.presentation.view.boarding.BoardingActivity
import com.wastecreative.wastecreative.presentation.view.pengaturan.PengaturanActivity
import com.wastecreative.wastecreative.presentation.view.viewModel.LoginViewModel
import com.wastecreative.wastecreative.presentation.view.viewModel.MainViewModel



class ProfileActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityProfileBinding


    companion object {
        const val avatarObject = "avatar"
        const val usernameObject = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Profile"

        val sectionProfilAdapter = SectionProfilAdapter(this)
        val viewPager:ViewPager2 = binding.viewPagerDetailProfile
        viewPager.adapter = sectionProfilAdapter
        viewPager.offscreenPageLimit = 2 / 2
        val tabs: TabLayout = binding.tabLayout

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        setUpAction()
        setUp()


    }
    private fun setUpAction(){
        binding.editButton.setOnClickListener {
            startActivity(Intent(this, PengaturanActivity::class.java))

        }
    }
    private fun setUp(){

        var getData = intent.getParcelableExtra<ResponseItem>(avatarObject)
        val getusername = intent.getParcelableExtra<ResponseItem>(usernameObject)

        binding.apply {
            if (getData != null && getusername!=null ) {
                tvNamaUserDetail.setText(getusername.username)
                Glide.with(this@ProfileActivity).load(getData.avatar).into(imageProfile)
            } else{
                Toast.makeText(this@ProfileActivity,"Gambar  profil tidak muncul ", Toast.LENGTH_LONG).show()
            }
        }
        Log.d("cek_profil","ini adalah ${getData}")



    }
}


