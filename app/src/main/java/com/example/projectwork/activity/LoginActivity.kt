package com.example.projectwork.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectwork.R
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData
import com.example.projectwork.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        val password = binding.passwordLogin.text.toString()
        val username = binding.usernameLogin.text.toString()

        val readData = this.readData<CAccount>("PREF_ACCOUNT")

        var control = false

        for (user in readData){
            if(password==user.password && username == user.username){
                control = true
            }
        }

        val button = findViewById<Button>(R.id.commitBtn)
        button.setOnClickListener {
            if (control){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Wrong credentials or account inexistent", Toast.LENGTH_SHORT).show()
            }
        }
    }
}