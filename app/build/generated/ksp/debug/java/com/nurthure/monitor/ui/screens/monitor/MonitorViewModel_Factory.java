package com.nurthure.monitor.ui.screens.monitor;

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
public final class MonitorViewModel_Factory implements Factory<MonitorViewModel> {
  private final Provider<SensorRepository> repositoryProvider;

  public MonitorViewModel_Factory(Provider<SensorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public MonitorViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static MonitorViewModel_Factory create(Provider<SensorRepository> repositoryProvider) {
    return new MonitorViewModel_Factory(repositoryProvider);
  }

  public static MonitorViewModel newInstance(SensorRepository repository) {
    return new MonitorViewModel(repository);
  }
}
