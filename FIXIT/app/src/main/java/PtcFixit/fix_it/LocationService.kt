package PtcFixit.fix_it

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.provider.CallLog.Locations
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class LocationService {
    @SuppressLint("MissingPermission")
    suspend fun getuserLocation(context: Context):Location?{
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val isUserLocationPermissionGranted = true
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if(!isGPSEnable || !isUserLocationPermissionGranted){
            return null
        }
        return suspendCancellableCoroutine { cont  ->
            fusedLocationProviderClient.lastLocation.apply {
                if (isComplete){
                    if (isSuccessful){
                        cont.resume(result){}
                    }else{
                        cont.resume(null){}
                    }

                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it){}
                }
                addOnFailureListener {
                    cont.resume(null){}
                }
                addOnCanceledListener {
                    cont.resume(null){}
                }
            }

        }
    }

}