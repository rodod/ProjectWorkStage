package com.example.projectwork.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectwork.adapter.AdapterMessage
import com.example.projectwork.databinding.FragmentChatBinding
import com.example.projectwork.viewModels.CViewModelMessages

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val adapter = AdapterMessage()
    private val binding get() = _binding!!
    private lateinit var viewModel : CViewModelMessages

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.messages.observe (viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
        }
        //adapter.submitList(TODO("AGGIUNGERE POI CON L'API LA LISTA DA FAR VEDERE"))

        binding.Recycler.adapter = adapter
        binding.Recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.button.setOnClickListener{
            /*view.findNavController().navigate(
                R.id.other_account_fragment,
                bundle, // Bundle of args
                null, // NavOptions
                extras)*/ TODO("VA ALL'INTERFACCIA DELLA FOTO")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}