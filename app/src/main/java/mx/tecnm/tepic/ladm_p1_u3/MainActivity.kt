package mx.tecnm.tepic.ladm_p1_u3

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import mx.tecnm.tepic.ladm_p1_u3.Fragments.*
import mx.tecnm.tepic.ladm_p1_u3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        val toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer = b.drawerLayout


        toogle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar.post {
            val d = ResourcesCompat.getDrawable(resources, R.drawable.squares, null)
            toolbar.navigationIcon = d
        }

        val navigationView : NavigationView = b.navView
        navigationView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        //item.isChecked = true
        b.gif.isVisible = false

        when(item.itemId){
           R.id.nav_item_AgregarConductor -> replaceFragment(firstFragment(),item.title.toString())
            R.id.nav_item_VerConductores -> replaceFragment(ListaConductores(),item.title.toString())
            R.id.nav_item_Vencidas -> replaceFragment(ListaConductoresVencidas(),item.title.toString())
            R.id.nav_item_ConductoresSincamion -> replaceFragment(ConductoresSinCamion(),item.toString())
            R.id.nav_item_AgregarCamion -> replaceFragment(AgregarCamion(),item.toString())
            R.id.nav_item_ListaCamiones -> replaceFragment(ListaCamiones(),item.title.toString())
            R.id.nav_item_ListaCamionesSinConductor -> replaceFragment(CamionesSinConductor(),item.title.toString())
            R.id.nav_item_Asignacion ->replaceFragment(ListaAsignacion(),item.title.toString())
            R.id.nav_item_LogOut ->{
                val intento = Intent( this, Login::class.java)
                startActivity(intento)
                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    public fun replaceFragment(fragment: Fragment, title:String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawer.closeDrawers()
        setTitle(title)
    }
}