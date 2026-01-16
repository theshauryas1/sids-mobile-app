package com.nurthure.monitor.ui.screens.trends;

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
public final class TrendsViewModel_Factory implements Factory<TrendsViewModel> {
  private final Provider<SensorRepository> repositoryProvider;

  public TrendsViewModel_Factory(Provider<SensorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TrendsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static TrendsViewModel_Factory create(Provider<SensorRepository> repositoryProvider) {
    return new TrendsViewModel_Factory(repositoryProvider);
  }

  public static TrendsViewModel newInstance(SensorRepository repository) {
    return new TrendsViewModel(repository);
  }
}
