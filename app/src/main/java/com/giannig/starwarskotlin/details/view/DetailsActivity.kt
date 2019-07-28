package com.giannig.starwarskotlin.details.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.giannig.starwarskotlin.R
import com.giannig.starwarskotlin.arch.ViewState
import com.giannig.starwarskotlin.data.StarWarsActions
import com.giannig.starwarskotlin.data.StarWarsState
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto
import com.giannig.starwarskotlin.details.DetailsReducer
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DetailsActivity : AppCompatActivity(), ViewState<StarWarsState> {

    private val presenter = DetailsReducer(Job() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        planetImageView.setImageDrawable(getDrawable(R.drawable.empire))
        planetImageView.visibility = GONE
        hideTextViews()

        val extraValue = intent.getStringExtra(PLANET_ID_EXTRA)
        presenter.onStart(this)
        presenter.sendAction(StarWarsActions.FetchSinglePlanet(extraValue))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onClose()
    }

    override fun updateState(state: StarWarsState) {
        when (state) {
            is StarWarsState.SinglePlanet -> showPlanetData(state.planet)
            is StarWarsState.Error -> showErrorMessage(state.message)
            StarWarsState.Loading -> loading()
            else -> showErrorMessage(null)
        }
    }

    private fun loading() {
        detailsLoading.visibility = VISIBLE
        planetImageView.visibility = GONE
        hideTextViews()
    }

    private fun hideTextViews() {
        textPlanetCitizens.visibility = GONE
        textDetailsPlanetName.visibility = GONE
        textPlanetDimensions.visibility = GONE
        textPlanetSurface.visibility = GONE
    }

    private fun showErrorMessage(message: String?) {
        textDetailsPlanetName.text = message ?: getString(com.giannig.starwarskotlin.R.string.error_details)
    }

    private fun showPlanetData(planet: StarWarsSinglePlanetDto) {
        detailsLoading.visibility = GONE
        planetImageView.visibility = VISIBLE

        planet.run {
            textDetailsPlanetName.text = name
            textPlanetCitizens.text = getString(
                R.string.population,
                population
            )
            textPlanetDimensions.text = getString(
                R.string.planet_dimensions,
                diameter
            )
            textPlanetSurface.text = getString(
                R.string.terrain_and_water,
                terrain ?: "no terrain",
                surfaceWater ?: "no water"
            )
        }

        showTextViews()
    }

    private fun showTextViews() {
        textPlanetCitizens.visibility = VISIBLE
        textDetailsPlanetName.visibility = VISIBLE
        textPlanetDimensions.visibility = VISIBLE
        textPlanetSurface.visibility = VISIBLE
    }

    companion object {
        const val PLANET_ID_EXTRA = "PLANET_ID_EXTRA"
        fun createIntent(context: Context): Intent = Intent(context, DetailsActivity::class.java)
    }

}
