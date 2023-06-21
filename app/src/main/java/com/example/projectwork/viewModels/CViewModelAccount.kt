package com.example.projectwork.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData

class CViewModelAccount(application: Application) : AndroidViewModel(application) {
    // Utilizza l'oggetto Application come necessario nel ViewModel{
    private val _mess = MutableLiveData<List<CAccount>>()
    init{
        val messages: List<CAccount> = readData(application, "PREF_ACCOUNT")
        _mess.value = messages
    }

    val messages: LiveData<List<CAccount>> get() = _mess

}

@Suppress("UNCHECKED_CAST")
class MyViewModelFactoryAcc(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CViewModelAccount::class.java) -> CViewModelAccount(application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
