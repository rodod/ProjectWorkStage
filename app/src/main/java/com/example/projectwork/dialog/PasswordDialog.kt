package com.example.projectwork.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.projectwork.R
import com.example.projectwork.classes.ApiSendInfo
import com.example.projectwork.classes.createRetrofitInstance
import com.example.projectwork.databinding.DialogChangePasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordDialog : DialogFragment() {
    private var _binding: DialogChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val application = requireActivity().application


    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val receivedData = arguments
        val pw=receivedData!!.getString("accountPW")

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_change_password, null)

        val retrofit=createRetrofitInstance("link")

        val apiService = retrofit.create(ApiSendInfo::class.java)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Change Password")
            .setView(view)
            .setPositiveButton("Change") { _, _ ->
                val oldPassword = binding.editTextOldPassword
                val newPassword = binding.editTextNewPassword
                if(oldPassword.text != null && newPassword.text != null){
                    if (oldPassword.text.toString() == pw ){
                        val call = apiService.sendNewPassword(newPassword.text.toString())

                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                println("Connection completed. Password changed")
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                println("Connection failed")
                            }
                        })
                    }
                    Toast.makeText(application, "Given password is different of the latest one", Toast.LENGTH_SHORT).show()
                }
                else{
                    this.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return dialogBuilder.create()
    }
}
