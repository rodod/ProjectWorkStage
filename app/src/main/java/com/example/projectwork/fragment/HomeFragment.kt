package com.example.projectwork.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectwork.adapter.AdapterHome
import com.example.projectwork.databinding.FragmentHomeBinding
import com.example.projectwork.viewModels.CViewModelAccount

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val adapter = AdapterHome()
    private val binding get() = _binding!!
    val application = requireActivity().application
    private  var viewModel = CViewModelAccount(application)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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