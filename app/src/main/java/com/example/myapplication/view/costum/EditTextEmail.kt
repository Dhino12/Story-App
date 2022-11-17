package com.example.myapplication.view.costum

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import android.util.Patterns.EMAIL_ADDRESS

class EditTextEmail : AppCompatEditText {
    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(email: Editable) {
                if(!isEmailValid(email)) {
                    error = "use email format. a@gmail.com"
                }
            }
        })
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return EMAIL_ADDRESS.matcher(email).matches()
    }
}