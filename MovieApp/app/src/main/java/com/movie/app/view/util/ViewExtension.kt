package com.movie.app.view.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.movie.app.R
import kotlinx.android.synthetic.main.list_view.view.*

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */

inline fun BaseFragment.setLayoutManager(recylerview: RecyclerView, orientation:Int){
    val layoutManager= LinearLayoutManager(context)
    layoutManager.orientation=orientation
    recylerview.layoutManager=layoutManager
}

inline fun BaseFragment.showMessage(message:String){
    view?.let {

        Snackbar.make(it,message,Snackbar.LENGTH_SHORT).show()
    }

}

