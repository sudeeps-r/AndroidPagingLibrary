package com.movie.app.core.di.component

import android.content.Context
import com.movie.app.MovieApp
import com.movie.app.core.di.module.ActivityModule
import com.movie.app.core.di.module.DaoModule
import com.movie.app.core.di.module.ServiceModule
import com.movie.app.core.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class,DaoModule::class, ViewModelModule::class, ActivityModule::class, ServiceModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun context(context: Context):Builder
        @BindsInstance
        fun server(endPoint:String):Builder

        fun build():AppComponent
    }
    fun inject(applicationContext:MovieApp)


}