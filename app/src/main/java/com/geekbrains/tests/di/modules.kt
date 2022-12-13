package com.geekbrains.tests.di

import com.geekbrains.tests.BuildConfig
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.presenter.SchedulerProvider
import com.geekbrains.tests.presenter.search.SearchSchedulerProvider
import com.geekbrains.tests.repository.FakeGitHubRepository
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.view.details.DetailsViewModel
import com.geekbrains.tests.view.search.SearchViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<GitHubApi> { get<Retrofit>().create(GitHubApi::class.java) }

    if (BuildConfig.TYPE == "FAKE") {
        single<RepositoryContract> { FakeGitHubRepository() }
    } else {
        single<RepositoryContract> { GitHubRepository(get()) }
    }

    single<SchedulerProvider> { SearchSchedulerProvider() }

    viewModel { SearchViewModel(get()) }

    viewModel { DetailsViewModel() }

}
const val BASE_URL = "https://api.github.com"