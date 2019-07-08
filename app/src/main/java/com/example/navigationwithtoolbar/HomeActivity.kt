package com.example.navigationwithtoolbar

import android.content.Intent
import android.net.Uri
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
    private lateinit var constants: Constants


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navControler = Navigation.findNavController(this,R.id.fragment)
        constants = Constants(this)
        bottomNav.setupWithNavController(navControler)
        drawer_navigation_view.setNavigationItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.profile ->{
               // toast("profile!")
                intent = Intent(applicationContext,ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.settings ->{
                toast("settings")
            }
            R.id.feedback ->{
               // toast("feedback!")
                intent = Intent(applicationContext,FeedbackActivity::class.java)
                startActivity(intent)
            }
            R.id.share ->{
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type="text/plain"
                //TODO:add share link content here.
                shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                startActivity(Intent.createChooser(shareIntent,""))
                //toast("share!")
            }
            R.id.about ->{
                toast("about!")
            }
            R.id.exit ->{
                toast("exit!")
            }
            R.id.logout->{
                //toast("logout")
                constants.removeUser()
                intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
            }
        }
        drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }
}
