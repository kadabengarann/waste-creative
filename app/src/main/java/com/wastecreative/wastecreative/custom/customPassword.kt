package com.wastecreative.wastecreative.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.wastecreative.wastecreative.R

class customPassword : AppCompatEditText {
    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet):super(context,attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,attrs,defStyleAttr){
        init()
    }

    private fun init(){
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotEmpty()){
                    if(passwordValidation(s.toString())) setError(null) else setError(context.getString(
                        R.string.invalid_password))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun passwordValidation(pass: String) : Boolean{
        return pass.length >= 6
    }
}