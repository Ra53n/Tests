package com.geekbrains.tests

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.FakeGitHubRepository
import com.geekbrains.tests.view.search.ScreenState
import com.geekbrains.tests.view.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: FakeGitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(repository)
    }

    @Test //Проверим вызов метода searchGitHub() у нашей ВьюМодели
    fun search_Test() {
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(
                    1,
                    listOf()
                )
            )
            searchViewModel.searchGitHub(SEARCH_QUERY)
            verify(repository, times(1)).searchGithubAsync(SEARCH_QUERY)
        }
    }

    @Test
    fun liveData_TestReturnValueIsNotNull() {
        testCoroutineRule.runBlockingTest {
            //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
            //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
            val observer = Observer<ScreenState> {}
            //Получаем LiveData
            val liveData = searchViewModel.subscribeToLiveData()

            //При вызове Репозитория возвращаем шаблонные данные
            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(
                    1,
                    listOf()
                )
            )

            try {
                //Подписываемся на LiveData без учета жизненного цикла
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
                Assert.assertNotNull(liveData.value)
            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun liveData_TestReturnTotalCountIsNull() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            //При вызове Репозитория возвращаем ошибку
            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(null, emptyList())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                //Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает ошибку
                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, NULL_RESULT_VALUES_ERROR_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun liveData_TestReturnSearchResultIsNull() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(
                    1,
                    null
                )
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, NULL_RESULT_VALUES_ERROR_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @After
    fun close() {
        stopKoin()
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
        private const val NULL_RESULT_VALUES_ERROR_TEXT = "Search results or total count are null"
        private const val NULL_RESULT_ERROR_TEXT = "Response is null or unsuccessful"
    }
}
