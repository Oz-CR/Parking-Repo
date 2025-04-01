package com.example.parking

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

class DashboardClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_cliente)

        val notiBoton: ImageView = findViewById(R.id.notifsBtn)

        notiBoton.setOnClickListener {
            val dialog = NotificacionesActivity().apply {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundedDialog)
            }
            dialog.show(supportFragmentManager, "notificaciones")
        }
    }
}