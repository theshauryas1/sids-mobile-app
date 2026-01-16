package com.nurthure.monitor.data.repository;

import android.content.Context;
import com.nurthure.monitor.data.local.NurthureDatabase;
import com.nurthure.monitor.data.remote.PiApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class SensorRepositoryImpl_Factory implements Factory<SensorRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<NurthureDatabase> databaseProvider;

  private final Provider<PiApiService> piApiServiceProvider;

  public SensorRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<NurthureDatabase> databaseProvider, Provider<PiApiService> piApiServiceProvider) {
    this.contextProvider = contextProvider;
    this.databaseProvider = databaseProvider;
    this.piApiServiceProvider = piApiServiceProvider;
  }

  @Override
  public SensorRepositoryImpl get() {
    return newInstance(contextProvider.get(), databaseProvider.get(), piApiServiceProvider.get());
  }

  public static SensorRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<NurthureDatabase> databaseProvider, Provider<PiApiService> piApiServiceProvider) {
    return new SensorRepositoryImpl_Factory(contextProvider, databaseProvider, piApiServiceProvider);
  }

  public static SensorRepositoryImpl newInstance(Context context, NurthureDatabase database,
      PiApiService piApiService) {
    return new SensorRepositoryImpl(context, database, piApiService);
  }
}
