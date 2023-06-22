import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projectwork.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private val friendLocations: List<LatLng> = listOf(
        //con un altra funzione api get prendo la posizione di un amico
        // Aggiungi altre posizioni degli amici qui
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

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

        // Imposta la posizione della mappa sulla tua posizione attuale
        // Esempio: LatLng della tua posizione
        val myLocation = LatLng(37.7749, -122.4194)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12f))

        // Aggiungi le icone dei profili degli amici sulla mappa
        for (location in friendLocations) {
            val markerOptions = MarkerOptions()
                .position(location)
                //.icon(BitmapDescriptorFactory.fromBitmap(getFriendProfileImage())) // Sostituisci con l'immagine del profilo dell'amico
            googleMap.addMarker(markerOptions)
        }
    }

    private fun getFriendProfileImage(){
        // Ottieni l'immagine del profilo dell'amico come Bitmap
        // Esempio: Converte un'immagine drawable in Bitmap
       //faccio una chiamata api get della pfp amica
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
}
