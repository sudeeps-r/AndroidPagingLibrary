package com.weatherapp.view.binding_adapter


import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.movie.app.BR


/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class AdapterViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(obj:Any){
        binding.setVariable(BR.rowItem, obj);
        binding.executePendingBindings();
    }

    fun bind(obj:Any,rowClick:Any){
        binding.setVariable(BR.rowItem, obj)
        binding.setVariable(BR.rowClick,rowClick)
        binding.executePendingBindings();
    }
}