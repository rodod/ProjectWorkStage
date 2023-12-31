package com.example.projectwork.dataManager

import android.content.Context
import android.content.SharedPreferences
import com.example.projectwork.classes.CAccount
import com.google.gson.Gson

// Generic function that reads data from a SharedPreferences file
inline fun <reified T> Context.readData(file: String): MutableList<T> {
    val items: MutableList<T> = mutableListOf()
    val sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE)
    val allEntries: Map<String, *> = sharedPreferences.all
    val iterator: Iterator<*> = allEntries.entries.iterator()
    val gson = Gson()
    while (iterator.hasNext()) {
        val entry = iterator.next() as Map.Entry<*, *>
        val value = entry.value.toString()
        val item: T = gson.fromJson(value, T::class.java)
        items.add(item)
    }
    return items
}

fun Context.getLatestUsedId(file: String): Int {
    var id = 0
    val sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE)
    val allEntries: Map<String, *> = sharedPreferences.all
    val iterator: Iterator<*> = allEntries.entries.iterator()
    while (iterator.hasNext()) {
        id++
    }
    return id
}


// Generic function that adds data to a SharedPreferences file
inline fun <reified T> Context.addData(items: List<T>, file: String) {
    val pref = getSharedPreferences(file, Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = pref.edit()
    val gson = Gson()
    for (item in items) {
        val itemJson = gson.toJson(item)
        val itemKey = "item_${item.hashCode()}"
        editor.putString(itemKey, itemJson)
    }
    editor.apply()
    println("Data added successfully")
}

inline fun <reified T> Context.addDataSing(item: T, file: String) {
    val pref = getSharedPreferences(file, Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = pref.edit()
    val gson = Gson()
    val itemJson = gson.toJson(item)
    val itemKey = "item_${item.hashCode()}"
    editor.putString(itemKey, itemJson)
    editor.apply()
    println("Data added successfully")
}

fun searchAccount(id : Int, totAcc : MutableList<CAccount>): CAccount?{
    for(account in totAcc){
        if(id == account.accountID){
            return account
        }
    }
    return null
}


// Function to update the JSON file by refreshing the data from the API
/*fun <T> updateFile(context: Context, file: String) {
    aggiungere api successivamente
    val beerApiService: BeerApiService = ApiClient.create()
    val call = beerApiService.getBeers()

    call.enqueue(object : retrofit2.Callback<List<T>> {
        override fun onResponse(call: Call<List<T>>, response: Response<List<T>>) {
            if (response.isSuccessful && response.body() != null) {
                println(response.isSuccessful)
                addData(context, response.body()!!, file)
            }
        }

        override fun onFailure(call: Call<List<T>>, t: Throwable) {
            // Handle failure
        }
    })
}*/
