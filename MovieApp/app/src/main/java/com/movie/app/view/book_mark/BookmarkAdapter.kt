package com.movie.app.view.book_mark

import com.movie.app.R
import com.movie.app.core.domain.model.BookMark
import com.movie.app.core.domain.model.Movie
import com.movie.app.view.home.MovieItemListener
import com.weatherapp.view.binding_adapter.BaseAdapter

/**
 * Created by Sudeep SR on 17/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class BookmarkAdapter(private val movieListener: MovieItemListener) : BaseAdapter() {
    override fun getListener(): Any {
        return movieListener
    }

    private val list = kotlin.collections.mutableListOf<BookMark>()

    private var count: Int = 0

    override fun getObjForPosition(position: Int): Any? {
        val bookMark = list.get(position)
        return Movie(bookMark.poster, bookMark.title, bookMark.type, bookMark.year, bookMark.imdbID, position, true)
    }

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.b_item
    }

    override fun getItemCount(): Int {
        return count
    }

    fun addAll(items: List<BookMark>) {
        list.addAll(items)
        count=list.size
        notifyDataSetChanged()
    }

    fun delete(pos: Int) {
        list.removeAt(pos)
        count=list.size
        notifyDataSetChanged() //use item position

    }
}