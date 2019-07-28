package com.giannig.starwarskotlin.main.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.giannig.starwarskotlin.R
import com.giannig.starwarskotlin.arch.ViewState
import com.giannig.starwarskotlin.data.StarWarsActions
import com.giannig.starwarskotlin.data.StarWarsState
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto
import com.giannig.starwarskotlin.details.view.DetailsActivity
import com.giannig.starwarskotlin.main.MainReducer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewState<StarWarsState> {

    private val presenter = MainReducer()
    private val adapter = MainListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.setClickLister { onClickItem(it) }
        itemList.layoutManager = LinearLayoutManager(this)
        itemList.adapter = adapter
        presenter.onStart(this)
        presenter.sendAction(StarWarsActions.FetchPlanetList)
        setUpSwipeToRefresh()
    }

    override fun updateState(state: StarWarsState) = when (state) {
        is StarWarsState.PlanetList -> showPlanetList(state.planets)
        is StarWarsState.Error -> showErrorMessage(state.message)
        StarWarsState.Loading -> loadView()
        else -> loadView()
    }

    private fun onClickItem(clickedId :Int){
        startActivity(DetailsActivity.createIntent(this).apply {
            putExtra(DetailsActivity.PLANET_ID_EXTRA, (clickedId+2).toString())
        })
    }

    private fun setUpSwipeToRefresh() {
        swipeToRefreshContainer.setOnRefreshListener {
            presenter.sendAction(StarWarsActions.FetchPlanetList)
        }

        swipeToRefreshContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun showPlanetList(list: List<StarWarsSinglePlanetDto>) {
        showItemList()
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

    private fun showItemList() {
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
