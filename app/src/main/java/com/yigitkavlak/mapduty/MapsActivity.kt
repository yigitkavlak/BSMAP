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
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_maps.view.*
import java.lang.Exception
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fauth: FirebaseAuth //Firebase
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val fragmentManager = supportFragmentManager
    private val taskFragment = TaskFragment()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu, menu) //options menu

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //Options menüsü logout butonu

        if (item.itemId == R.id.logout) { //logout
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


        }

        override fun onMarkerDrag(p0: Marker?) {

        }

        override fun onMarkerDragEnd(p0: Marker?) {

            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

            if (p0 != null) {
                var address = ""
                try {


                    val addressList =
                        geocoder.getFromLocation(p0.position.latitude, p0.position.longitude, 1)
                    if (addressList.isNotEmpty()) {

                        if (addressList[0].thoroughfare != null) {
                            address += addressList[0].thoroughfare
                            if (addressList[0].subThoroughfare != null) {
                                address += addressList[0].subThoroughfare
                            }
                        }

                        for (indices in addressList) {
                            println(indices)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                mMap.addMarker(MarkerOptions().position(p0.position).title(address).draggable(true))

            } else {
                Toast.makeText(applicationContext, "Try again", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun addTaskClicked(view: View) {


        if (addTaskbutton.tag == "CheckButton") {
            addTaskbutton.setBackgroundResource(R.drawable.ic_add_button)
            addTaskbutton.setTag("AddButton")
            openTaskFragment()
            addTaskbutton.visibility = View.INVISIBLE

            println(addTaskbutton.tag.toString())

        } else {
            addTaskbutton.setBackgroundResource(R.drawable.ic_check_button)
            addTaskbutton.setTag("CheckButton")
            closeTaskFragment()
            mMap.clear()
            addMarker()
            if (addTaskbutton.visibility == View.INVISIBLE) {
                addTaskbutton.visibility = View.VISIBLE
            }


        }


    }

    fun openTaskFragment() {

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, taskFragment, "Add")
        fragmentTransaction.commit()
    }

    fun closeTaskFragment() {

        val fragmentTransaction = fragmentManager.beginTransaction()
        if (taskFragment != null) {
            fragmentTransaction.remove(taskFragment)
            fragmentTransaction.commit()
        } else {
            Toast.makeText(this, "fragmentBulunamadı", Toast.LENGTH_LONG).show()
        }

    }

    fun addMarker() {
        mMap.setOnMarkerDragListener(myListener)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { p0 ->
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


}