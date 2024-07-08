package di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class SearchRepositoryQualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ForecastRepositoryQualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class FavouriteRepositoryQualifier