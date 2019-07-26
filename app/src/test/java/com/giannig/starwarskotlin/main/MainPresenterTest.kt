package com.giannig.starwarskotlin.main

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.State
import com.giannig.starwarskotlin.main.view.MainView
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    lateinit var mockView: MainView

    @Mock
    lateinit var mockProvider: StarWarsDataProvider

    lateinit var testee: MainPresenter

    @Before
    fun setup() {
        testee = MainPresenter(mockView)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `normal startup`() = runTesting {
        //Given
        whenever(mockProvider.providePlanets()).thenReturn()

        //When
        testee.update()

        //Then
        verify(mockView).showItemList()

    }


    @Test
    fun `load list`() = runBlocking {
        //        //Given
//
//        //When
//        whenever(testee.update()).thenReturn(State.PlanetList(emptyList()))
//
//        //Then
//        verify(mockView).showErrorMessage("banana")

    }


}


@ExperimentalCoroutinesApi
fun runTesting(
    block: suspend CoroutineScope.() -> Unit
){
    runBlocking {
        block()
    }
}


