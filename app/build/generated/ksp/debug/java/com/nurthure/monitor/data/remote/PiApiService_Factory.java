package com.nurthure.monitor.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PiApiService_Factory implements Factory<PiApiService> {
  private final Provider<HttpClient> clientProvider;

  public PiApiService_Factory(Provider<HttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public PiApiService get() {
    return newInstance(clientProvider.get());
  }

  public static PiApiService_Factory create(Provider<HttpClient> clientProvider) {
    return new PiApiService_Factory(clientProvider);
  }

  public static PiApiService newInstance(HttpClient client) {
    return new PiApiService(client);
  }
}
