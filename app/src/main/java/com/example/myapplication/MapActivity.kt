package com.example.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient

import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.LocationServices
import java.util.Locale
import java.util.List

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var map: GoogleMap
    private var selectedLocation: LatLng? = null  // Para almacenar la ubicación seleccionada
    private lateinit var btnSelectLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Inicializa el SDK de Places
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyDQLqMSW2aaBkqb82bQchF_daX7kWIxaoE")
        }

        val placesClient: PlacesClient = Places.createClient(this)

        // Configura el fragmento de mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configura el fragmento de autocompletar
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Configura un listener para manejar la selección de lugares
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Mover el mapa a la ubicación seleccionada
                val latLng = place.latLng
                if (latLng != null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    map.addMarker(MarkerOptions().position(latLng).title(place.name))

                    // Actualizar la ubicación seleccionada
                    selectedLocation = latLng
                    btnSelectLocation.visibility = View.VISIBLE
                }
            }


            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(this@MapActivity, "Error al seleccionar lugar: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
        })

        // Inicializa el botón
        btnSelectLocation = findViewById(R.id.btn_select_location)

        // Establece la acción de clic para el botón
        btnSelectLocation.setOnClickListener {
            selectedLocation?.let { latLng ->
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val addressLine = address.getAddressLine(0)

                    // Crear un Intent para iniciar ActivityBandMenu
                    val intent = Intent(this, BandMenuActivity::class.java)
                    intent.putExtra("selectedAddress", addressLine)
                    intent.putExtra("targetFragment", "AddBandFragment")
                    Log.i("direccion",addressLine)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No se pudo obtener la dirección de la ubicación seleccionada.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        requestLocationPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true

        map.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))
            btnSelectLocation.visibility = View.VISIBLE
            geocodeLocation(latLng)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }

        setUpMap()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            setUpMap()
        }
    }

    private fun setUpMap() {
        if (::map.isInitialized && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            // Obtener la ubicación actual del dispositivo
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    map.addMarker(MarkerOptions().position(currentLatLng).title("Tu ubicación actual"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
    }


    private fun geocodeLocation(latLng: LatLng) {
        // Usa el Geocoder para obtener la dirección
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            // Realiza la geocodificación
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addresses!!.isNotEmpty()) {
                // Obtén la dirección y muéstrala al usuario
                val address: Address = addresses.get(0)
                val addressLine = address.getAddressLine(0)

                // Muestra la dirección al usuario (puedes cambiar esto según tus necesidades)
                Toast.makeText(this, "Dirección: $addressLine", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al obtener la dirección: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpMap()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
