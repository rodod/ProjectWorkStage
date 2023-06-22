package com.example.projectwork.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectwork.R
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData
import com.example.projectwork.databinding.FragmentAccountBinding
import com.squareup.picasso.Picasso

class UserAccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireActivity().application
        val totAccount= application.readData<CAccount>("PREF_ACCOUNT")
        val bundle = arguments
        val receivedData=bundle!!.getString("username")
        val thisAcc = searchAccount(receivedData!!, totAccount)

        // Modifica il testo delle TextView
        binding.userSurnameView.text = thisAcc!!.surname
        binding.userNameView.text = thisAcc.name
        binding.userBioView.text=thisAcc.bio
        binding.userStepsView.text=thisAcc.steps.toString()
        binding.headerUser.text=thisAcc.username



        val imageView = binding.userPFP

        val imageUrl = thisAcc.profileImage

        Picasso.get().load(imageUrl).resize(500, 500).into(imageView)


        val changeButton=binding.change
        changeButton.setOnClickListener {
            val data = thisAcc.username // Il dato che desideri passare

            val bundle = Bundle().apply {
                putString("username", data)
            }

            val navController = findNavController()
            navController.navigate(R.id.change_account_fragment, bundle)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchAccount(username : String, totAcc : MutableList<CAccount>): CAccount?{
        for(account in totAcc){
            if(username == account.username){
                return account
            }
        }
        return null
    }
}