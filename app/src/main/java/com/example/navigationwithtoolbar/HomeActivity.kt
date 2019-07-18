package com.example.navigationwithtoolbar

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
                val builder = AlertDialog.Builder(this);
                builder.setTitle("Exit")
                builder.setMessage("Are you sure you want to Exit?")
                builder.setPositiveButton("YES"){dialog, which ->
                    // Do something when user press the positive button
                    finish()
                }
                builder.setNeutralButton("NO"){dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            R.id.logout->{
//                constants.removeUser()
//                intent = Intent(applicationContext,MainActivity::class.java)
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
                val builder = AlertDialog.Builder(this);
                builder.setTitle("Logout")
                builder.setMessage("Are you sure you want to logout?")
                builder.setPositiveButton("YES"){dialog, which ->
                    // Do something when user press the positive button
                    constants.removeUser()
                    intent = Intent(applicationContext,MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                builder.setNeutralButton("NO"){dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
        drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }
}
