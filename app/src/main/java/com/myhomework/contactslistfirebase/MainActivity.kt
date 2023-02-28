package com.myhomework.contactslistfirebase

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myhomework.contactslistfirebase.adapter.ContactsAdapter
import com.myhomework.contactslistfirebase.databinding.ActivityMainBinding
import com.myhomework.contactslistfirebase.model.Contact

class MainActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null

    companion object{
        const val COLLECTION_CONTACTS = "contacts"
        const val DOCUMENT_NAME = "name"
        const val DOCUMENT_NUMBER = "number"
        const val DOCUMENT_ADDRESS = "address"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProgressDialog("Loading contacts")

        val db = Firebase.firestore
        val contactsArr = ArrayList<Contact>()
        db.collection(COLLECTION_CONTACTS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    contactsArr.add(
                        Contact(
                            document.id,
                            document.getString(DOCUMENT_NAME)!!,
                            document.getString(DOCUMENT_NUMBER)!!,
                            document.getString(DOCUMENT_ADDRESS)!!
                        )
                    )
                }

                val adapter = ContactsAdapter(contactsArr)
                binding.rvTasks.layoutManager = LinearLayoutManager(this)
                binding.rvTasks.adapter = adapter

                hideProgressDialog()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()
                Log.w("TAG", "Error getting documents.", exception)
            }

        binding.btnAdd.setOnClickListener {
            val i = Intent(this,AddContact::class.java)
            startActivity(i)
        }
    }


    private fun showProgressDialog(msg:String) {
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