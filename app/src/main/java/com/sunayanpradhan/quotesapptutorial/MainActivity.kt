package com.sunayanpradhan.quotesapptutorial

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sunayanpradhan.quotesapptutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var list: ArrayList<OptionsModel>

    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)

        firebaseDatabase=FirebaseDatabase.getInstance()


        MobileAds.initialize(this)

        val adRequest= AdRequest.Builder().build()

        binding.bannerAds.loadAd(adRequest)


        list= ArrayList()

        val adapter=OptionsAdapter(list,this)

        val layoutManger=GridLayoutManager(this,2)

        binding.optionsRv.layoutManager=layoutManger

        firebaseDatabase.reference.child("Quotes")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear()

                    if (snapshot.exists()){

                        for (dataSnapshot in snapshot.children){

                            val data=dataSnapshot.getValue(OptionsModel::class.java)

                            list.add(data!!)

                        }

                        adapter.notifyDataSetChanged()

                        binding.optionsRv.adapter=adapter


                    }



                }

                override fun onCancelled(error: DatabaseError) {


                }


            })

        binding.optionsSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {



            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


                searchFilter(p0, adapter, list)


            }

            override fun afterTextChanged(editable: Editable?) {



            }
        })


    }

    private fun searchFilter(
        p0: CharSequence?,
        adapter: OptionsAdapter,
        list: ArrayList<OptionsModel>
    ) {


        firebaseDatabase.reference.child("Quotes")
            .orderByChild("optionsTitle")
            .startAt(p0.toString()).endAt(p0.toString() + "\uf8ff")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear()

                    if (snapshot.exists()){


                        for (dataSnapshot in snapshot.children){

                            val data=dataSnapshot.getValue(OptionsModel::class.java)

                            list.add(data!!)

                        }

                        adapter.notifyDataSetChanged()

                        binding.optionsRv.adapter=adapter


                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }


            })



    }

}