package com.myhomework.contactslistfirebase.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myhomework.contactslistfirebase.MainActivity
import com.myhomework.contactslistfirebase.databinding.ContactCardViewBinding
import com.myhomework.contactslistfirebase.model.Contact
import kotlin.collections.ArrayList

class ContactsAdapter(var data: ArrayList<Contact>): RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {
    lateinit var context: Context
    private var initialData = data
    private val db = Firebase.firestore

    class MyViewHolder(val cardViewBinding: ContactCardViewBinding): RecyclerView.ViewHolder(cardViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding : ContactCardViewBinding
                = ContactCardViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardViewBinding.apply {
            tvTaskName.text = data[position].name
            tvDate.text = data[position].address
        }


        holder.cardViewBinding.btnDelete.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Delete Contact")
                setMessage("Are you sure that you want to delete this Contact?")
                setPositiveButton("Yse"){
                        _, _ ->
                    db.collection(MainActivity.COLLECTION_CONTACTS).document(data[position].id)
                        .delete()
                        .addOnSuccessListener {
                            Log.e("TAG", "Deleted Successfully")
                            data.removeAt(position)
                            notifyDataSetChanged()
                        }.addOnFailureListener { exception ->
                            Log.e("TAG", exception.message.toString())
                        }
                }
                setCancelable(true)
                setNegativeButton("No"){ dialogInterface: DialogInterface, _ ->
                    dialogInterface.cancel()
                }
                create()
                show()
            }
        }

        /*holder.cardViewBinding.root.setOnClickListener {
            val intent = Intent(context, TaskDetails::class.java)
            intent.putExtra("id",data[position].id)
            intent.putExtra("title",data[position].title)
            intent.putExtra("description",data[position].description)

            val dateTimeArr = data[position].date.split(" ")
            intent.putExtra("date",dateTimeArr[0])
            intent.putExtra("time",dateTimeArr[1])

            context.startActivity(intent)
        }*/

    }

    override fun getItemCount(): Int {
        return data.size
    }

}