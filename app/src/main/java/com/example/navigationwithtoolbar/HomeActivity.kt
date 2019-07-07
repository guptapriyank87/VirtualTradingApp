package com.example.navigationwithtoolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    private lateinit var navControler :NavController
    private lateinit var toast: Toast


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navControler = Navigation.findNavController(this,R.id.fragment)
        bottomNav.setupWithNavController(navControler)
        drawer_navigation_view.setNavigationItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.profile ->{
                toast("profile!")
            }
            R.id.settings ->{
                toast("settings")
            }
            R.id.feedback ->{
                toast("feedback!")
            }
            R.id.share ->{
                toast("share!")
            }
            R.id.about ->{
                toast("about!")
            }
            R.id.exit ->{
                toast("exit!")
            }
        }
        drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }
}
