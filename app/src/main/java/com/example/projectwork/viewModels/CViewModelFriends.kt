package com.example.projectwork.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectwork.classes.CFriends

class CViewModelFriends(application: Application) : AndroidViewModel(application) {
    // Utilizza l'oggetto Application come necessario nel ViewModel{
    private val _friends = MutableLiveData<List<CFriends>>()
    init{
        val friends: List<CFriends> = TODO("api che prende tutti gli amici di una persona")
        _friends.value = friends
    }

    val friends: LiveData<List<CFriends>> get() = _friends

}

@Suppress("UNCHECKED_CAST")
class MyViewModelFactoryFriends(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CViewModelFriends::class.java) -> CViewModelFriends(application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}