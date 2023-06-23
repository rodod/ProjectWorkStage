import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projectwork.R
import com.example.projectwork.classes.CAccount
import com.example.projectwork.dataManager.readData
import com.example.projectwork.dataManager.searchAccount
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    val application: Application = requireActivity().application
    var accountId : Int = 0

    private val friendLocations = application.readData<CAccount>("PREF_ACCOUNT")
        //con un altra funzione api get prendo la posizione di un amico
        // Aggiungi altre posizioni degli amici qui

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        val bundle = arguments
        accountId=bundle!!.getInt("userId")


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
        val readData= application.readData<CAccount>("PREF_ACCOUNT")
        val user = searchAccount(accountId, readData)
        val latUser = user!!.latitude
        val lngUser =user.longitude
        accountId = user.accountID
        val posUser=LatLng(latUser, lngUser)
        val markerOptions = MarkerOptions()
            .position(posUser)
            .title(user.accountID.toString())
        //.icon(BitmapDescriptorFactory.fromBitmap(getFriendProfileImage())) // Sostituisci con l'immagine del profilo dell'amico
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posUser, 12f))

        // Aggiungi le icone dei profili degli amici sulla mappa
        for (location in friendLocations) {
            val lat = location.latitude
            val lng = location.longitude
            val pos=LatLng(lat, lng)
            val markerOptions = MarkerOptions()
                .position(pos)
                .title(location.accountID.toString())
                //.icon(BitmapDescriptorFactory.fromBitmap(getFriendProfileImage())) // Sostituisci con l'immagine del profilo dell'amico
            googleMap.addMarker(markerOptions)
        }
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

    private fun getFriendProfileImage(){
        // Ottieni l'immagine del profilo dell'amico come Bitmap
        // Esempio: Converte un'immagine drawable in Bitmap
       //faccio una chiamata api get della pfp amica
        TODO("FINIRE TUTTO QUI, METTERE INFORMAZIONI DEI PROFILI DI TUTTE LE PERSONE")
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
