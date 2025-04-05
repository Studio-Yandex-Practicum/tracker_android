package com.nemislimus.tratometr.di

import android.content.Context
import com.nemislimus.tratometr.case.db.DBHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    // DATABASE SECTION
    @Singleton
    @Provides
    fun provideDataBaseHelper(context: Context): DBHelper {
        return DBHelper(context)
    }


    // NETWORK SECTION


}