package com.nurthure.monitor.ui.screens.alerts;

import com.nurthure.monitor.data.repository.SensorRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AlertsViewModel_Factory implements Factory<AlertsViewModel> {
  private final Provider<SensorRepository> repositoryProvider;

  public AlertsViewModel_Factory(Provider<SensorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AlertsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AlertsViewModel_Factory create(Provider<SensorRepository> repositoryProvider) {
    return new AlertsViewModel_Factory(repositoryProvider);
  }

  public static AlertsViewModel newInstance(SensorRepository repository) {
    return new AlertsViewModel(repository);
  }
}
