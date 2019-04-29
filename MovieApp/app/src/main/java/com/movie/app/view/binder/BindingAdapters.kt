package com.weatherapp.view.binding_adapter

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("preComText")
    fun preComText(textView: AppCompatTextView, preComText: String?) {
        preComText?.let {
            textView.setTextFuture(PrecomputedTextCompat.getTextFuture(it, TextViewCompat.getTextMetricsParams(textView), null))
        }

    }

    @JvmStatic
    @BindingAdapter("imgUrl")
    fun setImage(imageView:ImageView,imgUrl:String?){
        if(imgUrl!=null)
        Picasso.get().load(imgUrl).into(imageView)

    }
}