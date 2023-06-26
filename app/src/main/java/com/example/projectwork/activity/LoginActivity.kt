package com.example.projectwork.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.projectwork.R
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData
import com.example.projectwork.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        binding = ActivityLoginBinding.inflate(layoutInflater)

        val readData = this.readData<CAccount>("PREF_ACCOUNT")

        val button = findViewById<Button>(R.id.commitBtn)
        button.setOnClickListener {
            val password = binding.passwordLogin.text.toString()
            val email = binding.usernameLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUserWithEmailAndPassword(email, password, readData)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }


        val buttonRegister = findViewById<Button>(R.id.sign_in_button)
        buttonRegister.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.sign_in_fragment)
        }


    }

    private fun searchAccount(email : String, totAcc : MutableList<CAccount>): CAccount?{
        for(account in totAcc){
            if(email == account.email){
                return account
            }
        }
        return null
    }

    private fun loginUserWithEmailAndPassword(email: String, password: String, readData : MutableList<CAccount>) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val intent = Intent(this, MainActivity::class.java)
                        val account = searchAccount(email, readData)
                        intent.putExtra("userId", account!!.accountID)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}