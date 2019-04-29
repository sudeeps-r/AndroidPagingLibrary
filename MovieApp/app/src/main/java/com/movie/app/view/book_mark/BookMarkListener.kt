package com.movie.app.view.book_mark

import com.movie.app.core.domain.model.BookMark

/**
 * Created by Sudeep SR on 17/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
interface BookMarkListener {

    fun deleteItem(item:BookMark)
}