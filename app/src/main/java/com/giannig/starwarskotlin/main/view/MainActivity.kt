package com.giannig.starwarskotlin.main.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.giannig.starwarskotlin.R
import com.giannig.starwarskotlin.arch.ViewState
import com.giannig.starwarskotlin.data.StarWarsState
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto
import com.giannig.starwarskotlin.details.view.DetailsActivity
import com.giannig.starwarskotlin.main.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewState<StarWarsState> {

    private val presenter = MainPresenter()
    private val adapter = MainListAdapter()


    override fun updateState(state: StarWarsState) {
        when (state) {
            is StarWarsState.PlanetList -> updateList(state.planets)
            is StarWarsState.NetworkError -> showErrorMessage(state.message)
            StarWarsState.Error -> showErrorMessage("")
            StarWarsState.Loading -> loadView()
            else -> loadView()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.setClickLister { onClickItem(it) }
        itemList.layoutManager = LinearLayoutManager(this)
        itemList.adapter = adapter
        presenter.onStart(this)
        setUpSwipeToRefresh()
    }

    private fun onClickItem(clickedId :Int){
        startActivity(DetailsActivity.createIntent(this).apply {
            putExtra(DetailsActivity.PLANET_ID_EXTRA, (clickedId+2).toString())
        })
    }

    private fun setUpSwipeToRefresh() {
        swipeToRefreshContainer.setOnRefreshListener {
            //todo
        }

        swipeToRefreshContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun updateList(list: List<StarWarsSinglePlanetDto>) {
        adapter.addValues(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onClose()
    }

    private fun loadView() {
        swipeToRefreshContainer.isRefreshing = true
        errorText.visibility = View.GONE
    }

    fun showItemList() {
        swipeToRefreshContainer.isRefreshing = false
        itemList.visibility = View.VISIBLE
        errorText.visibility = View.GONE
    }

    private fun showErrorMessage(message: String?) {
        message?.let {
            errorText.text = it
        }

        swipeToRefreshContainer.isRefreshing = false
        itemList.visibility = View.GONE
        errorText.visibility = View.VISIBLE
    }
}
