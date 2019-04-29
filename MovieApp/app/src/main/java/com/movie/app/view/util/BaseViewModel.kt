package com.movie.app.view.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movie.app.core.util.DBState
import com.movie.app.core.util.NetworkState
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
open class BaseViewModel : ViewModel() {

    private val networkState= MutableLiveData<NetworkState>()
    private val dbState=MutableLiveData<DBState>()

    protected val compositeDp=CompositeDisposable()

    fun getNetWorkState()=networkState

    protected fun setState(state: NetworkState){
        networkState.postValue(state)
    }

    protected fun setError(message:String){
        networkState.postValue(NetworkState.error(message))
    }

    fun getDBState()=dbState

    protected fun setState(state: DBState){
        dbState.postValue(state)
    }

    protected fun setDBError(message:String){
        dbState.postValue(DBState.error(message))
    }

    override fun onCleared() {
        super.onCleared()
        setDBError("")
        compositeDp.clear()
    }
}