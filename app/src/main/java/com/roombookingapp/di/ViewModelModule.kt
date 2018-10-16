package com.roombookingapp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.roombookingapp.ui.roombook.BookRoomViewModel
import com.roombookingapp.ui.rooms.RoomsListViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RoomsListViewModel::class)
    abstract fun bindRoomsListViewModel(viewModel: RoomsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookRoomViewModel::class)
    abstract fun bindBookRoomViewModel(viewModel: BookRoomViewModel): ViewModel


}