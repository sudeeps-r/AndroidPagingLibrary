package com.movie.app.core.util

/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */

enum class DStatus {
    INSERT,
    DELETE,
    FAILED
}

//update with sealed class

@Suppress("DataClassPrivateConstructor")
data class DBState  constructor(
        val status: DStatus,
        val message: String? = null,
        val obj:Any?=null) {
    companion object {
        /*val INSERT = DBState(DStatus.INSERT)
        val DELETE = DBState(DStatus.DELETE)*/
        fun error(msg: String?) = DBState(DStatus.FAILED, msg)
    }
}