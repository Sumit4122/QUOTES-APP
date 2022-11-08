package com.sunayanpradhan.quotesapptutorial

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.sunayanpradhan.quotesapptutorial.databinding.AdUnifiedBinding

class QuotesAdapter(private val list:ArrayList<QuotesModel>, private val context: Context):RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val quotesText:TextView=itemView.findViewById(R.id.quotes_text)

        val quotesAuthor:TextView=itemView.findViewById(R.id.quotes_author)

        val quotesCopy:ImageView=itemView.findViewById(R.id.quotes_copy)

        val quotesShare:ImageView=itemView.findViewById(R.id.quotes_share)

        val viewAdsLayout:CardView=itemView.findViewById(R.id.view_ads_layout)

        val viewAds:FrameLayout=itemView.findViewById(R.id.view_ads)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.quotes_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem=list[position]

        holder.quotesText.text="❝ "+currentItem.quotesText+" ❞"

        holder.quotesAuthor.text="~"+currentItem.quotesAuthor

        holder.quotesCopy.setOnClickListener {

            setClipboard(context,currentItem.quotesText+"\n~"+currentItem.quotesAuthor)


        }


        holder.quotesShare.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, currentItem.quotesText+"\n~"+currentItem.quotesAuthor)
            context.startActivity(intent)

        }


        val adLoader=
            AdLoader.Builder(context,"ca-app-pub-3940256099942544/2247696110")
                .forNativeAd { nativeAd:NativeAd->


                    holder.viewAdsLayout.visibility=View.VISIBLE

                    val layoutInflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


                    val viewUnifiedBinding= AdUnifiedBinding.inflate(layoutInflater)


                    populateNativeAdView(nativeAd,viewUnifiedBinding)

                    holder.viewAds.removeAllViews()


                    holder.viewAds.addView(viewUnifiedBinding.root)



                }
                .withAdListener(object : AdListener(){

                    override fun onAdFailedToLoad(p0: LoadAdError) {


                    }

                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder().build()
                )
                .build()



        adLoader.loadAds(AdRequest.Builder().build(),5)



    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun setClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
    }


    private fun populateNativeAdView(nativeAd: NativeAd, unifiedBinding: AdUnifiedBinding){

        val nativeAdView= unifiedBinding.root

        nativeAdView.mediaView= unifiedBinding.adMedia

        nativeAdView.headlineView=unifiedBinding.adHeadline
        nativeAdView.bodyView=unifiedBinding.adBody
        nativeAdView.callToActionView=unifiedBinding.adCallToAction
        nativeAdView.iconView=unifiedBinding.adAppIcon
        nativeAdView.priceView=unifiedBinding.adPrice
        nativeAdView.starRatingView=unifiedBinding.adStars
        nativeAdView.storeView=unifiedBinding.adStore
        nativeAdView.advertiserView=unifiedBinding.adAdvertiser



        unifiedBinding.adHeadline.text=nativeAd.headline

        nativeAd.mediaContent?.let {

            unifiedBinding.adMedia.setMediaContent(it)

        }

        if (nativeAd.body==null){

            unifiedBinding.adBody.visibility= View.INVISIBLE

        }else{

            unifiedBinding.adBody.visibility= View.VISIBLE

            unifiedBinding.adBody.text=nativeAd.body


        }

        if (nativeAd.callToAction==null){

            unifiedBinding.adCallToAction.visibility=View.INVISIBLE

        }
        else{

            unifiedBinding.adCallToAction.visibility=View.VISIBLE

            unifiedBinding.adCallToAction.text=nativeAd.callToAction


        }

        if (nativeAd.icon==null){

            unifiedBinding.adAppIcon.visibility=View.INVISIBLE

        }else{

            unifiedBinding.adAppIcon.visibility=View.VISIBLE

            unifiedBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)


        }

        if (nativeAd.price==null){

            unifiedBinding.adPrice.visibility= View.INVISIBLE



        }
        else{


            unifiedBinding.adPrice.visibility= View.VISIBLE


            unifiedBinding.adPrice.text= nativeAd.price



        }


        //hide
        if (nativeAd.store==null){

//            unifiedBinding.adStore.visibility=View.INVISIBLE

        }
        else{

//            unifiedBinding.adStore.visibility=View.VISIBLE

            unifiedBinding.adStore.text=nativeAd.store


        }

        if (nativeAd.starRating==null){



            unifiedBinding.adStars.visibility= View.INVISIBLE

        }
        else{
            unifiedBinding.adStars.visibility= View.VISIBLE

            unifiedBinding.adStars.rating= nativeAd.starRating!!.toFloat()



        }


        if (nativeAd.advertiser==null){

            unifiedBinding.adAdvertiser.visibility= View.INVISIBLE



        }

        else{

            unifiedBinding.adAdvertiser.visibility= View.VISIBLE


            unifiedBinding.adAdvertiser.text= nativeAd.advertiser


        }


        nativeAdView.setNativeAd(nativeAd)

        val vc=nativeAd.mediaContent?.videoController



        if (vc!=null && vc.hasVideoContent()){

            vc.videoLifecycleCallbacks=
                object : VideoController.VideoLifecycleCallbacks(){

                    override fun onVideoEnd() {

                        super.onVideoEnd()
                    }


                }


        }


        else{



        }








    }




}