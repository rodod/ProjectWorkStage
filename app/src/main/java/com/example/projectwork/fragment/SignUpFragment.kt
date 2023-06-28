package com.example.projectwork.fragment

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectwork.R
import com.example.projectwork.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

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