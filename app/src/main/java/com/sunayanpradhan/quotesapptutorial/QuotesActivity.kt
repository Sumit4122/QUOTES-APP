package com.sunayanpradhan.quotesapptutorial

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sunayanpradhan.quotesapptutorial.databinding.ActivityQuotesBinding

class QuotesActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuotesBinding

    lateinit var list: ArrayList<QuotesModel>

    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_quotes)

        firebaseDatabase=FirebaseDatabase.getInstance()

        MobileAds.initialize(this)

        val adRequest= AdRequest.Builder().build()

        binding.bannerAds.loadAd(adRequest)

        val intent=intent

        val optionsTitle=intent.getStringExtra("optionsTitle").toString()

        list= ArrayList()

        binding.optionsTitle.text=optionsTitle

        binding.quotesLayoutBack.setOnClickListener {
            finish()
        }

        val adapter=QuotesAdapter(list,this)

        val layoutManager=LinearLayoutManager(this)

        binding.quotesRv.layoutManager=layoutManager


        firebaseDatabase.reference
            .child("Quotes")
            .child(optionsTitle)
            .child("Content")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){

                        for (dataSnapshot in snapshot.children){

                            val data=dataSnapshot.getValue(QuotesModel::class.java)

                            list.add(data!!)

                        }

                        adapter.notifyDataSetChanged()

                        binding.quotesRv.adapter=adapter
                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }


            })


    }





}