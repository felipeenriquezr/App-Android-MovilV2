package com.example.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GeolocationActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private val LOCATION_PERMISSION_CODE = 100
    private var isPermissionsGranted = false

    private lateinit var txtLatitud: EditText
    private lateinit var txtLongitud: EditText
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geolocation_activity)

        txtLatitud = findViewById(R.id.txtLatitud)
        txtLongitud = findViewById(R.id.txtLongitud)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionsGranted = true
            initializeLocation()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showExplanationDialog()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permisos necesarios")
            .setMessage("Esta aplicación requiere acceder a tu ubicación para mostrar tus coordenadas ¿Deseas permitir el acceso?")
            .setPositiveButton("Sí") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_CODE
                )
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                showPermissionsDeniedMessage()
            }
            .create()
            .show()
    }

    private fun showPermissionsDeniedMessage() {
        Toast.makeText(this, "La aplicación necesita permisos de ubicación para funcionar", Toast.LENGTH_LONG).show()
    }

    private fun initializeLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            val locationRequest = LocationRequest.Builder(30000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setWaitForAccurateLocation(true)
                .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        updateLocation(it)
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        updateLocation(it)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Ubicación", "Error obteniendo la ubicación", e)
                    Toast.makeText(this, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
                }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        } catch (e: SecurityException) {
            Log.e("Location", "Permission error: ${e.message}")
            Toast.makeText(this, "Error: Permisos no disponibles", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateLocation(location: Location) {
        txtLatitud.setText("%.6f".format(location.latitude))
        txtLongitud.setText("%.6f".format(location.longitude))

        if (::mMap.isInitialized) {
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(userLocation).title("Mi ubicación"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        mMap.setOnMapLongClickListener(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }
    }

    override fun onMapClick(latLng: LatLng) {
        updateMapPosition(latLng)
    }

    override fun onMapLongClick(latLng: LatLng) {
        updateMapPosition(latLng)
    }

    private fun updateMapPosition(latLng: LatLng) {
        txtLatitud.setText(latLng.latitude.toString())
        txtLongitud.setText(latLng.longitude.toString())
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                isPermissionsGranted = true
                initializeLocation()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showExplanationDialog()
                } else {
                    showSettingsDialog()
                }
            }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permisos requeridos")
            .setMessage("Es necesario habilitar los permisos desde la configuración de la aplicación para poder funcionar correctamente.")
            .setPositiveButton("Ir a Configuración") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                showPermissionsDeniedMessage()
            }
            .create()
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::fusedLocationClient.isInitialized && ::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}
