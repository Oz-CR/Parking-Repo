package com.example.parking

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment

class CustomDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar tu layout personalizado
        return inflater.inflate(R.layout.activity_notify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar tus vistas aquí
        view.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dismiss() // Cierra el diálogo
        }

        // Personaliza el tamaño del diálogo
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            // Fondo totalmente transparente
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Elimina cualquier sombra por defecto
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            // Ajusta el tamaño
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}