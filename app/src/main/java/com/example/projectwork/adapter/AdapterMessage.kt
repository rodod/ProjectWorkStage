package com.example.projectwork.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectwork.classes.CMessage
import com.example.projectwork.databinding.ItemMessageBinding

class AdapterMessage : ListAdapter<CMessage, HolderMess>(object : DiffUtil.ItemCallback<CMessage>(){
    override fun areItemsTheSame(oldItem: CMessage, newItem: CMessage): Boolean {

        return oldItem.messageID==newItem.messageID
    }
    //controlla se le due classi sono uguali basandosi su tutta la classe, si controlla l'id
    override fun areContentsTheSame(oldItem: CMessage, newItem: CMessage): Boolean {
        return oldItem==newItem //qui si controllano tutti gli attributi della classe e non solo la memoria per accertarsi se sono due elementi uguali
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMess {
        return HolderMess(ItemMessageBinding.inflate(LayoutInflater.from(parent.context)))
    } //crea un istanza "Holder" (definita poi) che poi verrata spostata, nell'Activity main, all'interno del recycler view

    override fun onBindViewHolder(holder: HolderMess, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }
    //quando l'elemento poi verrà visto nello schermo
}

class HolderMess(private val binding: ItemMessageBinding) :RecyclerView.ViewHolder(binding.root){
    fun bind(message : CMessage){

        binding.sender.text=message.senderUsername
        binding.steps.text =message.steps.toString()
        binding.textMessage.text=message.message
        binding.date.text=message.date.toString()
    }
}