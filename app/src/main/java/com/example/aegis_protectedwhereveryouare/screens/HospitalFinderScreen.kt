package com.example.aegis_protectedwhereveryouare.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun HospitalFinderScreen(navController: NavController) {
    val context = LocalContext.current
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf(LatLng(37.5665, 126.9780)) } // Default: Seoul

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 14f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("← Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "🏥 Hospital Finder",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = locationPermissionGranted
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = locationPermissionGranted,
                zoomControlsEnabled = true
            )
        ) {
            // Hospital markers around Seoul for demo
            val hospitals = listOf(
                Pair("Seoul National University Hospital", LatLng(37.5796, 126.9987)),
                Pair("Severance Hospital", LatLng(37.5621, 126.9395)),
                Pair("Samsung Medical Center", LatLng(37.4881, 127.0858)),
                Pair("Asan Medical Center", LatLng(37.5270, 127.1086)),
                Pair("Korea University Hospital", LatLng(37.5894, 127.0268))
            )

            hospitals.forEach { (name, location) ->
                Marker(
                    state = MarkerState(position = location),
                    title = name,
                    snippet = "Tap for details"
                )
            }
        }
    }
}
