package com.geekbrains.tests.di

import com.geekbrains.tests.BuildConfig
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.presenter.details.PresenterDetailsContract
import com.geekbrains.tests.presenter.search.PresenterSearchContract
import com.geekbrains.tests.presenter.search.SearchPresenter
import com.geekbrains.tests.repository.FakeGitHubRepository
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<GitHubApi> { get<Retrofit>().create(GitHubApi::class.java) }

    if (BuildConfig.TYPE == "FAKE") {
        single<RepositoryContract> { FakeGitHubRepository() }
    } else {
        single<RepositoryContract> { GitHubRepository(get()) }
    }

    factory<PresenterSearchContract> { SearchPresenter(get()) }

    factory<PresenterDetailsContract> { DetailsPresenter() }

}
const val BASE_URL = "https://api.github.com"