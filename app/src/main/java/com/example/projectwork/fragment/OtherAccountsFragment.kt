package com.example.projectwork.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectwork.R
import com.example.projectwork.classes.ApiSendInfo
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.addDataSing
import com.example.projectwork.dataManager.readData
import com.example.projectwork.dataManager.searchAccount
import com.example.projectwork.databinding.FragmentOtherAccountsBinding
import com.example.projectwork.viewModels.CViewModelAccount
import com.example.projectwork.viewModels.MyViewModelFactoryAcc
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class OtherAccountsFragment : Fragment() {

    private var _binding: FragmentOtherAccountsBinding? = null
    val application = requireActivity().application!!
    private  var viewModel = CViewModelAccount(application)
    lateinit var friendAcc : CAccount
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_other_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, MyViewModelFactoryAcc(application))[CViewModelAccount::class.java]

        val bundle = arguments
        val userId=bundle!!.getString("userId")
        val friendId= bundle.getString("friendId")
        val accountsList = application.readData<CAccount>("PREF_ACCOUNT")

        val userAccount = searchAccount(userId!!.toInt(), accountsList)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://indirizzo_server:porta") // Sostituisci con il tuo indirizzo del server
            .build()

        val apiService = retrofit.create(ApiSendInfo::class.java)


        val call = apiService.getProfile(friendId!!.toInt())
        call.enqueue(object : Callback<CAccount> {
            override fun onResponse(call: Call<CAccount>, response: Response<CAccount>) {
                if (response.isSuccessful) {
                    friendAcc = response.body()!!
                } else {
                    Toast.makeText(application, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CAccount>, t: Throwable) {
                Toast.makeText(application, "Unable to reach the server", Toast.LENGTH_SHORT).show()
            }
        })


        //sistemare gui
        binding.headerOther.text = friendAcc.username
        Picasso.get().load(friendAcc.profileImage).into(binding.friendPFP)
        binding.otherNameView.text = friendAcc.name
        binding.otherSurnameView.text = friendAcc.surname
        binding.otherBioView.text= friendAcc.bio
        binding.otherStepsView.text= friendAcc.steps.toString()



        //aggiungi amico
        val button = view.findViewById<Button>(R.id.add_to_friends)
        button.setOnClickListener {
            userAccount!!.friends!!.add(friendId.toInt())
            application.addDataSing(friendAcc, "PREF_ACCOUNT")
        }
    }
}