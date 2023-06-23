package com.example.projectwork.fragment

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectwork.R
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData
import com.example.projectwork.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private lateinit var binding : FragmentSignInBinding
    val application: Application = requireActivity().application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignInBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val password = binding.signInPassword.text.toString()
        val username = binding.signInUsername.text.toString()

        val readData = application.readData<CAccount>("PREF_ACCOUNT")

        var control = false

        for (user in readData){
            if(password==user.password && username == user.username){
                control = true
            }
        }

        val button = binding.registrationButton
        button.setOnClickListener {
            if (control){
                Toast.makeText(application, "Select another username", Toast.LENGTH_SHORT).show()
            }
            else{
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("accountUsername", username)
                bundle.putString("accountPassword", password)
                navController.navigate(R.id.user_account_fragment, bundle)
            }
        }
    }
}