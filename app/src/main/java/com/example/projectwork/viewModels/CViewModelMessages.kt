package com.example.projectwork.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData

class CViewModelMessages(application: Application) : AndroidViewModel(application) {
    // Utilizza l'oggetto Application come necessario nel ViewModel{
    private val _accounts = MutableLiveData<List<CAccount>>()
    init{
        val accounts: List<CAccount> = readData(application, "PREF_ACCOUNT")
        _accounts.value = accounts
    }

    val accounts: LiveData<List<CAccount>> get() = _accounts

}

@Suppress("UNCHECKED_CAST")
class MyViewModelFactoryMess(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CViewModelAccount::class.java) -> CViewModelAccount(application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}