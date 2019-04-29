package com.movie.app.view.book_mark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movie.app.R
import com.movie.app.core.di.component.Injectable
import com.movie.app.core.domain.model.BookMark
import com.movie.app.core.domain.model.Movie
import com.movie.app.core.util.DBState
import com.movie.app.core.util.DStatus
import com.movie.app.databinding.ListViewBinding
import com.movie.app.view.home.HomeViewModel
import com.movie.app.view.home.MovieItemListener
import com.movie.app.view.util.BaseFragment
import com.movie.app.view.util.RetryCallback
import com.movie.app.view.util.setLayoutManager
import com.movie.app.view.util.showMessage
import com.weatherapp.view.binding_adapter.autoCleared
import kotlinx.android.synthetic.main.list_view.view.*
import javax.inject.Inject

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class BookMarkView: BaseFragment(),Injectable, MovieItemListener,RetryCallback {

    override fun onRetry() {
       navbar().navigateUp()
    }

    override fun onItemClicked(movie: Movie) {

    }

    override fun onBookMark(movie: Movie) {
        bookmarkViewModel.deleteBookMark(movie)
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private var databing by autoCleared<ListViewBinding>()
    private val bookmarkAdapter=BookmarkAdapter(this)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBindingUtil = DataBindingUtil.inflate<ListViewBinding>(inflater, R.layout.list_view, container, false)
        databing = dataBindingUtil
        databing.setLifecycleOwner(this)
        return databing.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init(){
        bookmarkViewModel = ViewModelProviders.of(this, viewModelFactory).get(BookmarkViewModel::class.java)
        setLayoutManager(getListView,LinearLayoutManager.HORIZONTAL)
        hideAll()
        databing.retry=this
        getListView.adapter=bookmarkAdapter
        bookmarkViewModel.getAllData()
        bookmarkViewModel.getAllBookMark().observe(this, Observer  {it->bookmarkAdapter.addAll(it);  showEmpty(bookmarkAdapter.itemCount)})
        bookmarkViewModel.getDBState().observe(this, Observer(::hanldeDBCallback))
    }

    fun hanldeDBCallback(dbState: DBState) {
        when (dbState.status) {

             DStatus.DELETE -> {
                val m = dbState.obj as Movie
                showMessage(dbState.message!!)
                bookmarkAdapter.delete(m.pos)
            }

            else -> {
                showMessage(dbState.message!!)
            }
        }
        showEmpty(bookmarkAdapter.itemCount)
    }

    val getListView by lazy{
        view?.findViewById(R.id.rvListView) as RecyclerView
    }

    fun showEmpty(count:Int){
        if(count<=0){
            showError(true)
            databing.title="Browse"
            databing.message="Please start bookmarking the movies!!!"
        }
    }
}