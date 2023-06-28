package com.example.projectwork.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projectwork.R
import com.example.projectwork.classes.ApiSendInfo
import com.example.projectwork.classes.CAccount
import com.example.projectwork.classes.createRetrofitInstance
import com.example.projectwork.dataManager.readData
import com.example.projectwork.dataManager.searchAccount
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CompletableFuture

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    val application: Application = requireActivity().application
    lateinit var accountList: List<CAccount>
    private var accountId: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        val bundle = arguments
        accountId = bundle!!.getInt("userId")


        val apiService = createRetrofitInstance().create(ApiSendInfo::class.java)

        val call = apiService.getNearbyAccounts()
        call.enqueue(object : Callback<List<CAccount>> {
            override fun onResponse(call: Call<List<CAccount>>, response: Response<List<CAccount>>) {
                if (response.isSuccessful) {
                    accountList = response.body()!!
                    addMarkersToMap()
                } else {
                    Toast.makeText(application, "Error of the server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CAccount>>, t: Throwable) {
                Toast.makeText(application, "Unable to reach the server", Toast.LENGTH_SHORT).show()
            }
        })

        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Verifica il permesso di ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            // Richiedi il permesso di ACCESS_FINE_LOCATION se non è stato ancora concesso
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Permesso già concesso, ottieni le coordinate
            getCurrentLocation()
        } else {
            // Richiedi il permesso di ACCESS_FINE_LOCATION se non è stato ancora concesso
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        }


        mapView.onResume() // Resume map rendering


        val readData = application.readData<CAccount>("PREF_ACCOUNT")
        val user = searchAccount(accountId, readData)
        val latUser = user!!.latitude
        val lngUser = user.longitude
        accountId = user.accountID
        val posUser = LatLng(latUser, lngUser)
        val markerOptions = MarkerOptions()
            .position(posUser)
            .title(user.accountID.toString())
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posUser, 12f))

        googleMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val friendId = marker.title

        // Ottieni il NavController dal Navigation Host (Activity o Fragment)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)

        // Naviga al fragment dell'amico passando l'ID o l'identificativo dell'amico come argomento
        val bundle: Bundle = bundleOf("friendId" to friendId)
        bundle.putInt("userID", accountId)
        navController.navigate(R.id.other_account_fragment, bundle)

        // Restituisci true per indicare che l'evento di click del marker è stato gestito
        return true
    }

    private fun addMarkersToMap() {
        if (::googleMap.isInitialized) {
            // Aggiungi le icone dei profili degli amici sulla mappa
            for (location in accountList) {
                val lat = location.latitude
                val lng = location.longitude
                val pos = LatLng(lat, lng)
                val markerOptions = MarkerOptions()
                    .position(pos)
                    .title(location.accountID.toString())

                getFriendProfileImage(location.accountID)
                    .thenAccept { bitmap ->
                        if (bitmap != null) {
                            val markerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
                            markerOptions.icon(markerIcon)
                        }
                        googleMap.addMarker(markerOptions)
                    }
                    .exceptionally {
                        // Handle exceptions
                        googleMap.addMarker(markerOptions)
                        null
                    }
            }
        }
    }

    private fun getFriendProfileImage(id: Int): CompletableFuture<Bitmap> {
        val account = searchAccount(id, accountList.toMutableList())
        val future = CompletableFuture<Bitmap>()

        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap != null) {
                    future.complete(bitmap)
                } else {
                    future.completeExceptionally(IllegalArgumentException("Bitmap is null"))
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                future.completeExceptionally(e)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        }

        Picasso.get().load(account!!.profileImage).into(target)

        return future
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 1001
    }

    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Coordinate ottenute con successo, puoi utilizzarle qui
                    val lat = location.latitude
                    val lng = location.longitude

                    // Esegui le operazioni necessarie con le coordinate ottenute
                    // ...
                } else {
                    // Errore nel recupero delle coordinate
                    Toast.makeText(application, "Failed to retrieve current location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Gestisci l'eccezione se si verifica un errore nel recupero delle coordinate
                Toast.makeText(application, "Failed to retrieve current location: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

