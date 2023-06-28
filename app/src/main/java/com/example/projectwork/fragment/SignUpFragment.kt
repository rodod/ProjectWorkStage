package com.example.projectwork.fragment

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectwork.R
import com.example.projectwork.classes.ApiSendInfo
import com.example.projectwork.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    val application: Application = requireActivity().application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = FragmentSignInBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = binding.registrationButton
        button.text = "Sign Up" // Modifica il testo del pulsante

        button.setOnClickListener {
            val email = binding.signInUsername.text.toString()
            val password = binding.signInPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUserWithEmailAndPassword(email, password) // Chiamata al metodo di registrazione
            } else {
                Toast.makeText(application, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val token = user?.getIdToken(false)?.result?.token

                    if (token != null) {
                        val retrofit = Retrofit.Builder()
                            .baseUrl("http://your_server_url/") // Sostituisci con l'URL del tuo server
                            .build()

                        val apiService = retrofit.create(ApiSendInfo::class.java)
                        val call = apiService.sendToken(token)

                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                // Gestisci la risposta dal server
                                if (response.isSuccessful) {
                                    println("Token created and sent to the server")
                                } else {
                                    Toast.makeText(application, "Unable to communicate with the server", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }

                    val navController = findNavController()
                    val bundle = Bundle()
                    bundle.putString("accountEmail", email)
                    bundle.putString("accountPassword", password)
                    navController.navigate(R.id.initialize_account_fragment, bundle)
                } else {
                    Toast.makeText(application, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}