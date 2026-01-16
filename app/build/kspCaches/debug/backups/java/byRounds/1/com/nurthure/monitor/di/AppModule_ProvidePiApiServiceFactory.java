package com.nurthure.monitor.di;

import com.nurthure.monitor.data.remote.PiApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.ktor.client.HttpClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AppModule_ProvidePiApiServiceFactory implements Factory<PiApiService> {
  private final Provider<HttpClient> clientProvider;

  public AppModule_ProvidePiApiServiceFactory(Provider<HttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public PiApiService get() {
    return providePiApiService(clientProvider.get());
  }

  public static AppModule_ProvidePiApiServiceFactory create(Provider<HttpClient> clientProvider) {
    return new AppModule_ProvidePiApiServiceFactory(clientProvider);
  }

  public static PiApiService providePiApiService(HttpClient client) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePiApiService(client));
  }
}
