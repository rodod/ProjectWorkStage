package com.example.projectwork.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.projectwork.R
import com.example.projectwork.classes.ApiSendInfo
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.getLatestUsedId
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class InitializeLogin : Fragment() {
    private lateinit var profileImageView: ImageView
    private lateinit var changePhotoButton: Button
    private var selectedImageUri: Uri? = null
    lateinit var supportAccount : CAccount


    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            setProfileImage(uri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_initialize_login, container, false)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.223:3000") //mettere indirizzo giuto
            .build()

        val accountService = retrofit.create(ApiSendInfo::class.java)


        profileImageView = rootView.findViewById(R.id.firstPFP)
        changePhotoButton = rootView.findViewById(R.id.selectFirstPhoto)

        changePhotoButton.setOnClickListener {
            pickImageFromGallery()
        }

        val application = requireActivity().application
        val bundle = arguments
        val receivedEmail=bundle!!.getString("accountEmail")
        val receivedPassword=bundle.getString("accountPassword")

        val usernameText = rootView.findViewById<EditText>(R.id.firstUsername)
        val nameText = rootView.findViewById<EditText>(R.id.firstName)
        val surnameText = rootView.findViewById<EditText>(R.id.firstSurname)
        val bioText = rootView.findViewById<EditText>(R.id.firstBio)

        usernameText.hint="Username here"
        nameText.hint="Name here"
        surnameText.hint="Surname here"
        bioText.hint="Biography here"

        val commitBtn=rootView.findViewById<Button>(R.id.returnToLogin)
        commitBtn.setOnClickListener {
            val textUser = usernameText.text.toString()

            val textName = nameText.text.toString()

            val textSurname = surnameText.text.toString()

            val textBio = bioText.text.toString()

            var list : MutableList<Int>? = null


            if (textName=="" || textUser==""|| textSurname==""|| textBio=="" ){
                Toast.makeText(application, "Fill all the obligatory fields", Toast.LENGTH_SHORT).show()
            }


            else{
                supportAccount = CAccount(
                    application.getLatestUsedId("PREF_ACCOUNT"),
                    receivedEmail!!,
                    textName,
                    textSurname,
                    textUser,
                    receivedPassword!!,
                    selectedImageUri.toString(), // Passa l'URI selezionato direttamente
                    0,
                    textBio,
                    list,
                    0.0,
                    0.0)


                val gson = Gson()
                val itemJson = gson.toJson(supportAccount)
                val jsonObject = JSONObject(itemJson)
                replaceLineInSharedPreferences(application, "item_${supportAccount.hashCode()}", jsonObject)

                val call = accountService.createAccount(supportAccount)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            // L'account è stato inviato con successo al server
                            Toast.makeText(application, "Account created successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            // Gestisci l'errore in base alla risposta del server
                            Toast.makeText(application, "Failed to create account", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Gestisci l'errore di connessione o altre eccezioni
                        Toast.makeText(application, "Error: " + t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        return rootView
    }

    private fun pickImageFromGallery() {
        pickImageContract.launch("image/*")
    }

    private fun setProfileImage(imageUri: Uri) {
        profileImageView.setImageURI(imageUri)
        selectedImageUri = imageUri
        Toast.makeText(requireContext(), "Profile picture set", Toast.LENGTH_SHORT).show()
    }

    private fun replaceLineInSharedPreferences(context: Context, key: String, newData: JSONObject) {
        val sharedPreferences = context.getSharedPreferences("PREF_ACCOUNT", Context.MODE_PRIVATE)

        // Leggi il contenuto JSON esistente dalle SharedPreferences
        val jsonString = sharedPreferences.getString(key, null)

        // Verifica se il contenuto JSON esistente è valido
        if (jsonString != null) {
            // Analizza il contenuto JSON in una struttura dati
            val existingData = JSONObject(jsonString)

            // Sostituisci i dati della riga con i nuovi dati forniti
            existingData.put("accountID", newData.getString("accountID"))
            existingData.put("name", newData.getString("name"))
            existingData.put("surname", newData.getString("surname"))
            existingData.put("username", newData.getString("username"))
            existingData.put("password", newData.getString("password"))
            existingData.put("imageUri", newData.getString("imageUri"))
            existingData.put("steps", newData.getInt("steps"))
            existingData.put("bio", newData.getString("bio"))
            existingData.put("friends", newData.getJSONArray("friends"))
            existingData.put("longitude", newData.getDouble("longitude"))
            existingData.put("latitude", newData.getDouble("latitude"))

            // Salva la struttura dati aggiornata nelle SharedPreferences
            sharedPreferences.edit().putString(key, existingData.toString()).apply()
        }
    }
}