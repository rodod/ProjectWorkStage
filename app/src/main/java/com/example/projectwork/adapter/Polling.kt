package com.example.projectwork.adapter

import com.example.projectwork.classes.ApiSendInfo
import com.example.projectwork.classes.CMessage
import com.example.projectwork.classes.createRetrofitInstance
import kotlinx.coroutines.*
import java.io.IOException

interface PollingListener {
    fun onPollingResult(result: List<CMessage>)
}

class Polling(private val listener: PollingListener) {
    private var pollingJob: Job? = null
    private var pollingInterval: Long = 5000 // Intervallo predefinito di 5 secondi
    private var isPollingActive = false

    fun startPolling(interval: Long = pollingInterval) {
        if (isPollingActive) return // Se il polling è già attivo, esce

        pollingInterval = interval
        isPollingActive = true

        pollingJob = CoroutineScope(Dispatchers.Main).launch {
            while (isPollingActive) {
                val result = performPolling() // Esegue il polling

                // Notifica il risultato al listener solo se non è nullo
                result.let {
                    listener.onPollingResult(it)
                }

                delay(pollingInterval)
            }
        }
    }

    fun stopPolling() {
        isPollingActive = false
        pollingJob?.cancel()
    }

    private suspend fun performPolling(): List<CMessage> {
        // Implementa qui la logica del polling effettiva
        delay(1000) // Simulazione del tempo di attesa

        return getReceivedMessages()
    }

    private suspend fun getReceivedMessages(): List<CMessage> {
        return withContext(Dispatchers.IO) {
            val apiService = createRetrofitInstance().create(ApiSendInfo::class.java)
            val call = apiService.getMessages()

            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val messages = response.body()
                    messages ?: emptyList() // Restituisce la lista dei messaggi se non è nulla, altrimenti una lista vuota
                } else {
                    emptyList() // Restituisce una lista vuota se la chiamata non ha avuto successo
                }
            } catch (e: IOException) {
                emptyList() // Restituisce una lista vuota in caso di eccezione
            }
        }
    }



}

