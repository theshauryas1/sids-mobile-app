package com.nurthure.monitor.di

import android.content.Context
import androidx.room.Room
import com.nurthure.monitor.data.local.NurthureDatabase
import com.nurthure.monitor.data.remote.PiApiService
import com.nurthure.monitor.data.repository.SensorRepository
import com.nurthure.monitor.data.repository.SensorRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NurthureDatabase {
        return Room.databaseBuilder(
            context,
            NurthureDatabase::class.java,
            "nurthure_database"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            engine {
                connectTimeout = 5000
                socketTimeout = 5000
            }
        }
    }
    
    @Provides
    @Singleton
    fun providePiApiService(client: HttpClient): PiApiService {
        return PiApiService(client)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindSensorRepository(
        impl: SensorRepositoryImpl
    ): SensorRepository
}
