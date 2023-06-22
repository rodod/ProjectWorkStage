package com.example.projectwork.fragment

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
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData

class ChangeFragment : Fragment() {
    private lateinit var profileImageView: ImageView
    private lateinit var changePhotoButton: Button
    private var selectedImageUri: Uri? = null


    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            setProfileImage(uri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_change, container, false)

        profileImageView = rootView.findViewById(R.id.profileImageView)
        changePhotoButton = rootView.findViewById(R.id.changePhotoButton)

        changePhotoButton.setOnClickListener {
            pickImageFromGallery()
        }

        val application = requireActivity().application
        val totAccount= application.readData<CAccount>("PREF_ACCOUNT")
        val bundle = arguments
        val receivedData=bundle!!.getString("username")
        val thisAcc = searchAccount(receivedData!!, totAccount)

        val usernameText = rootView.findViewById<EditText>(R.id.editUsername)
        val nameText = rootView.findViewById<EditText>(R.id.editName)
        val surnameText = rootView.findViewById<EditText>(R.id.editSurname)
        val bioText = rootView.findViewById<EditText>(R.id.editBio)

        usernameText.hint=thisAcc!!.username
        nameText.hint=thisAcc.name
        surnameText.hint=thisAcc.surname
        bioText.hint=thisAcc.bio

        val commitBtn=rootView.findViewById<Button>(R.id.sendChanges)
        commitBtn.setOnClickListener {
            var textUser = if(usernameText.text==null){
                thisAcc.username
            } else{
                usernameText.text.toString()
            }


            var textName = if(nameText.text==null){
                thisAcc.name
            } else{
                nameText.text.toString()
            }


            var textSurname = if(surnameText.text==null){
                thisAcc.surname
            } else{
                surnameText.text.toString()
            }


            var textBio = if(bioText.text==null){
                thisAcc.bio
            } else{
                bioText.text.toString()
            }

            var supportAccount = CAccount(
                thisAcc!!.accountID,
                textName,
                textSurname,
                textUser,
                thisAcc.password,
                selectedImageUri.toString(), // Passa l'URI selezionato direttamente
                thisAcc.steps,
                textBio,
                thisAcc.friends,
            )
        }

        return rootView
    }

    private fun pickImageFromGallery() {
        pickImageContract.launch("image/*")
    }

    private fun setProfileImage(imageUri: Uri) {
        profileImageView.setImageURI(imageUri)
        selectedImageUri = imageUri
        Toast.makeText(requireContext(), "Foto del profilo aggiornata", Toast.LENGTH_SHORT).show()
    }


    private fun saveProfileImageUri(uri: Uri){
        // Salva l'URI dell'immagine del profilo nel database o in una variabile di stato
        // Ad esempio, puoi utilizzare Firebase Realtime Database o Firebase Firestore per salvarlo
    }

    private fun searchAccount(username : String, totAcc : MutableList<CAccount>): CAccount?{
        for(account in totAcc){
            if(username == account.username){
                return account
            }
        }
        return null
    }





}
