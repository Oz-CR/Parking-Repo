package com.example.parking.ui.dashboard

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.parking.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificacionesActivity : DialogFragment() {
    private lateinit var notificationsList: ListView
    private val notifications = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private val CHANNEL_ID = "ParkingNotifications"
    private var pollingJob: Job? = null
    private val previousStates = mutableMapOf<Int, ParkingLot>()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_notify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsList = view.findViewById(R.id.notificationsList)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notifications)
        notificationsList.adapter = adapter

        view.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dismiss()
        }

        createNotificationChannel()
        startPolling()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pollingJob?.cancel()
    }

    private fun startPolling() {
        pollingJob = lifecycleScope.launch {
            while (true) {
                try {
                    val response: Response<List<ParkingLot>> = RetrofitClient.parkingApiService.getParkingStatus()
                    Log.d("API_DEBUG", "Respuesta: ${response.body()}")

                    if (response.isSuccessful) {
                        val parkingLots = response.body()
                        parkingLots?.let { lots ->
                            if (lots.isEmpty()) {
                                addNotification("No hay estacionamientos registrados (${getCurrentTime()})")
                            } else {
                                processParkingLots(lots)
                            }
                        } ?: addNotification("Datos nulos recibidos de la API (${getCurrentTime()})")
                    } else {
                        addNotification("Error en la API: Código ${response.code()} (${getCurrentTime()})")
                    }
                } catch (e: Exception) {
                    addNotification("Error de conexión: ${e.message} (${getCurrentTime()})")
                    Log.e("NETWORK_ERROR", "Error al consultar API", e)
                }
                delay(5000) // Consulta cada 5 segundos (ajusta según necesidades)
            }
        }
    }

    private fun processParkingLots(lots: List<ParkingLot>) {
        lots.forEach { currentLot ->
            val previousLot = previousStates[currentLot.id]

            if (previousLot == null) {
                // Primer registro de este estacionamiento
                addNotification("Nuevo estacionamiento: ${currentLot.name} - Estado: ${currentLot.status} (${getCurrentTime()})")
                previousStates[currentLot.id] = currentLot
            } else if (previousLot.status != currentLot.status) {
                // Cambio de estado detectado
                val message = buildString {
                    append("Cambio en ${currentLot.name}: ")
                    append("${previousLot.status} → ${currentLot.status}")

                    currentLot.distance?.let {
                        append(" - Distancia: ${it}cm")
                    }

                    append(" (${currentLot.lastUpdated ?: getCurrentTime()})")
                }

                addNotification(message)
                showNotification(message)
                previousStates[currentLot.id] = currentLot
            }
        }
    }

    private fun addNotification(message: String) {
        activity?.runOnUiThread {
            notifications.add(0, message) // Agrega al inicio
            adapter.notifyDataSetChanged()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Parking Notifications"
            val description = "Notificaciones sobre cambios en estacionamientos"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }

            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("NotificationPermission")
    private fun showNotification(message: String) {
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Cambio en estacionamiento")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun getCurrentTime(): String {
        return dateFormat.format(Date())
    }
}