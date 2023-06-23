package com.example.projectwork.fragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectwork.adapter.AdapterHome
import com.example.projectwork.databinding.FragmentHomeBinding
import com.example.projectwork.viewModels.CViewModelAccount
import com.example.projectwork.viewModels.CViewModelFriends
import com.example.projectwork.viewModels.MyViewModelFactoryAcc
import com.example.projectwork.viewModels.MyViewModelFactoryFriends

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val adapter = AdapterHome()
    private val binding get() = _binding!!
    val application: Application = requireActivity().application
    private  var viewModel = CViewModelAccount(application)
    private  var viewModelFriends = CViewModelFriends(application)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, MyViewModelFactoryAcc(application))[CViewModelAccount::class.java]

        viewModelFriends = ViewModelProvider(this, MyViewModelFactoryFriends(application))[CViewModelFriends::class.java]


        viewModel.accounts.observe (viewLifecycleOwner) { accounts ->
            adapter.submitList(accounts)
        }

        binding.Recycler.adapter = adapter
        binding.Recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}