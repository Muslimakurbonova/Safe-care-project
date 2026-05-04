package com.programmsoft.sevgisherlari

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.sevgisherlari.databinding.ActivityMainBinding
import com.programmsoft.utils.Functions
import com.programmsoft.utils.Functions.createNotificationChannel
import com.programmsoft.utils.Functions.fragmentList
import com.programmsoft.utils.SharedPreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreference.init(this)
        SharedPreference.isAllowNotification = Functions.isAllowNotifications(this)
        Functions.connectivityManager(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_UzbekEnglishDictionary)
        }
        setContentView(R.layout.activity_main)
        Functions.appearanceStatusNavigationBars(
            window, 0,
            appearanceLightStatusBars = true,
            appearanceNavigationBars = false
        )
        Functions.statusNavigationBarsColor(window, this, R.color.status_bar, R.color.navigation_bar)
        createNav()
        isAppFirstRun()
    }

    private fun createNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = binding.btnNav
        bottomNavigationView.itemIconTintList = null
        appBarConfiguration = AppBarConfiguration(
            fragmentList
        )
        binding.btnNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.btnNav.isVisible = destination.id !in Functions.destinationsWithoutBottomNav
        }
    }

    private fun isAppFirstRun() {
        val count = Functions.db.contentDataDao().getAllContent().size
        if (count == 0) {
            Functions.preloadMockData()
            // Optional: You can do other first-run things here
        }
        if (SharedPreference.isAppFirstOpen != 1) {
            //  Functions.insertCategories()
            createNotificationChannel(this)
            Functions.setTimeOfAlarmManager(this)
            SharedPreference.isAppFirstOpen = 1

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.content_nav)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
        super.attachBaseContext(newBase)
    }
}