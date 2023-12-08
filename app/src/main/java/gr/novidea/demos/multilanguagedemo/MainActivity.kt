package gr.novidea.demos.multilanguagedemo

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import gr.novidea.demos.multilanguagedemo.fragment.AboutAppFragment
import gr.novidea.demos.multilanguagedemo.fragment.DashboardFragment
import gr.novidea.demos.multilanguagedemo.fragment.LanguageSettingsFragment
import gr.novidea.demos.multilanguagedemo.R
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView

    private var previousMenuItem: MenuItem? = null

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("LanguageDemo", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selectedLanguage", "defaultLanguage")
        setLocale(savedLanguage)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initializing views
        initializeViews()

        //Set up toolbar
        setUpToolbar()

        //Set up navigation drawer and its actions
        setUpNavigationDrawer()

        //Open Dashboard fragment by default
        openDashboard()
    }

    //Function to initialize views from layout
    private fun initializeViews() {
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)
    }

    //Function to set up toolbar
    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
    }

    //Function to set up navigation drawer
    private fun setUpNavigationDrawer() {
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationClick(menuItem)
        }
    }


    //Function to handle click events in navigation drawer items
    private fun handleNavigationClick(menuItem: MenuItem): Boolean {
        if (previousMenuItem != null) {
            previousMenuItem?.isChecked = false
        }
        menuItem.isCheckable = true
        menuItem.isChecked = true
        previousMenuItem = menuItem

        when (menuItem.itemId) {
            R.id.dashboard -> {
                openDashboard()
                drawerLayout.closeDrawers()
                supportActionBar?.title = getString(R.string.dashboard)
            }
            R.id.language -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frame,
                    LanguageSettingsFragment()
                ).commit()
                supportActionBar?.title = getString(R.string.languageSettings)
                drawerLayout.closeDrawers()
            }
            R.id.about -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frame,
                    AboutAppFragment()
                ).commit()
                supportActionBar?.title = getString(R.string.about_app)
                drawerLayout.closeDrawers()
            }
        }
        return true
    }

    //Function to open Dashboard fragment
    private fun openDashboard() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.frame,
            DashboardFragment()
        )
        transaction.commit()
        navigationView.setCheckedItem(R.id.dashboard)
    }

    //Override the back button press
    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frame)) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }

    //Function to set locale/language
    private fun setLocale(language: String?) {
        val locale = Locale(language ?: "en")
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
