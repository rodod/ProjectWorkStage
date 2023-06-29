package com.example.projectwork.fragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectwork.adapter.AdapterMessage
import com.example.projectwork.adapter.Polling
import com.example.projectwork.adapter.PollingListener
import com.example.projectwork.classes.CMessage
import com.example.projectwork.databinding.FragmentChatBinding
import com.example.projectwork.viewModels.CViewModelMessages
import com.example.projectwork.viewModels.MyViewModelFactoryMess

class ChatFragment : Fragment() , PollingListener{

    private var _binding: FragmentChatBinding? = null
    private val adapter = AdapterMessage()
    private val application : Application = requireActivity().application
    private val polling = Polling(this)
    private val binding get() = _binding!!
    private lateinit var viewModel : CViewModelMessages

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, MyViewModelFactoryMess(application))[CViewModelMessages::class.java]


        viewModel.messages.observe (viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
        }

        binding.Recycler.adapter = adapter
        binding.Recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.button.setOnClickListener{
            /*view.findNavController().navigate(
                R.id.other_account_fragment,
                bundle, // Bundle of args
                null, // NavOptions
                extras)*/ TODO("VA ALL'INTERFACCIA DELLA FOTO")
        }

        polling.startPolling()

    }

    override fun onPollingResult(result: List<CMessage>) {
        // Aggiorna l'adapter con la nuova lista di elementi
        adapter.submitList(result)
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
        polling.stopPolling()
    }
}