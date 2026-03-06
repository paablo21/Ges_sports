package com.example.ges_sports.ui.home

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(navController: NavHostController) {

    val context = LocalContext.current

    var hasPermission by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Aún no se ha solicitado el permiso.") }

    // Configuración obligatoria de OSMDroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE)
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        message = if (granted) {
            "Permiso concedido: mostrando ubicación del centro."
        } else {
            "Permiso denegado: la app continuará sin ubicación."
        }
    }

    val centroPosicion = GeoPoint(38.0894, -0.9954)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                title = { Text("Ubicación del centro") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            if (hasPermission) {

                // Mapa OSM
                val mapView = remember {
                    MapView(context).apply {
                        setMultiTouchControls(true)
                        controller.setZoom(17.0)
                        controller.setCenter(centroPosicion)
                    }
                }

                AndroidView(
                    modifier = Modifier.weight(1f),
                    factory = { mapView },
                    update = { map ->
                        map.overlays.clear()

                        val marker = Marker(map)
                        marker.position = centroPosicion
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = "Centro Multideporte Jacarilla"

                        map.overlays.add(marker)
                        map.invalidate()
                    }
                )

            } else {

                // Sin permiso
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(message)

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    ) {
                        Text("Solicitar permiso de ubicación")
                    }

                    Text("Funcionalidad limitada sin permiso.")
                }
            }
        }
    }
}