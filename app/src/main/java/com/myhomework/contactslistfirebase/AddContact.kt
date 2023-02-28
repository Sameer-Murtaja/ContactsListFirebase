package com.myhomework.contactslistfirebase

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myhomework.contactslistfirebase.databinding.ActivityAddContactBinding

class AddContact : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore

        binding.btnSave.setOnClickListener {

            if (binding.tvName.text.isNotEmpty() && binding.tvNumber.text.isNotEmpty()
                && binding.tvAddress.text.isNotEmpty()
            ) {
                showProgressDialog("Uploading Contact")
                val name = binding.tvName.text.toString()
                val number = binding.tvNumber.text.toString()
                val address = binding.tvAddress.text.toString()

                val product = hashMapOf(
                    MainActivity.DOCUMENT_NAME to name,
                    MainActivity.DOCUMENT_NUMBER to number,
                    MainActivity.DOCUMENT_ADDRESS to address
                )
                db.collection(MainActivity.COLLECTION_CONTACTS)
                    .add(product)
                    .addOnSuccessListener {
                        Log.e("TAG", "Added Successfully")
                        hideProgressDialog()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Log.w("TAG", "Error getting documents.", it)
                        hideProgressDialog()
                    }

            }
        }

    }

    private fun showProgressDialog(msg: String) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(msg)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

}