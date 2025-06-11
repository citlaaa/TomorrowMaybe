package com.example.tomorrowmaybe.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.tomorrowmaybe.R
import com.example.tomorrowmaybe.databinding.ActivityOnboardingBinding
import com.example.tomorrowmaybe.utils.FragmentCommunicator

class OnboardingActivity : AppCompatActivity(), FragmentCommunicator {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var biding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(biding.root)
    }

    override fun showLoader(value: Boolean) {
        biding.loaderContainerView.visibility = if (value) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}