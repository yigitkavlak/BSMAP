package com.yigitkavlak.mapduty

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.Exception
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fauth: FirebaseAuth
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            fauth.signOut()
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fauth = FirebaseAuth.getInstance()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location) {

                val userLocation = LatLng(p0.latitude, p0.longitude)
                mMap.addMarker(
                    MarkerOptions().position(userLocation).title("Konumunuz").draggable(true)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())


                try {
                    val addressList = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)

                    if (addressList != null && addressList.isNotEmpty()) {
                        println(addressList.get(0).toString()) //logcatte görmek için
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1f,
                locationListener
            )
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {
                val lastKnowLatLng = LatLng(lastLocation.longitude, lastLocation.longitude)
                mMap.addMarker(
                    MarkerOptions().position(lastKnowLatLng).title("Konumunuz").draggable(true)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnowLatLng, 15f))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        100,
                        10f,
                        locationListener
                    )
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    val myListener = object : GoogleMap.OnMarkerDragListener {
        override fun onMarkerDragStart(p0: Marker?) {

            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

            if (p0 != null) {
                try {


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }

        override fun onMarkerDrag(p0: Marker?) {

        }

        override fun onMarkerDragEnd(p0: Marker?) {

        }

    }

    fun addTaskClicked(view : View){

        if (addTaskbutton.tag == "pic2"){
            addTaskbutton.setBackgroundResource(R.drawable.ic_add_button)
            addTaskbutton.setTag("pic1")
        }else{
            addTaskbutton.setBackgroundResource(R.drawable.ic_check_button)
            addTaskbutton.setTag("pic2")
        }


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val taskFragment = TaskFragment()
        fragmentTransaction.add(R.id.frameLayout,taskFragment).commit()


    }
}