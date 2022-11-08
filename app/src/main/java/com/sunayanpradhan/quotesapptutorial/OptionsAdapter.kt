package com.sunayanpradhan.quotesapptutorial

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class OptionsAdapter(private val list:ArrayList<OptionsModel>, private val context: Context):RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    private var mInterstitialAd: InterstitialAd?=null



    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val optionsImage:ImageView=itemView.findViewById(R.id.options_image)

        val optionsTitle:TextView=itemView.findViewById(R.id.options_title)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.options_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem=list[position]

        Glide.with(context.applicationContext)
            .load(currentItem.optionsImage)
            .into(holder.optionsImage)

        holder.optionsTitle.text=currentItem.optionsTitle


        MobileAds.initialize(context){

        }

        if (mInterstitialAd!=null){

            mInterstitialAd!!.show(context as Activity)


        }
        else{


        }


        holder.itemView.setOnClickListener {

            val intent= Intent(context,QuotesActivity::class.java)

            intent.putExtra("optionsTitle",currentItem.optionsTitle)

            context.startActivity(intent)

            loadAds()



        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun loadAds(){

        val adRequest= AdRequest.Builder().build()

        InterstitialAd.load(context,"ca-app-pub-3940256099942544/1033173712",adRequest,object :
            InterstitialAdLoadCallback(){

            override fun onAdFailedToLoad(p0: LoadAdError) {

                mInterstitialAd=null


            }

            override fun onAdLoaded(p0: InterstitialAd) {


                mInterstitialAd=p0

            }


        })



    }




}