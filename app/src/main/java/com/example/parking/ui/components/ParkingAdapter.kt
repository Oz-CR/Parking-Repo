package com.example.parking.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parking.R
import com.example.parking.data.model.Result
import com.example.parking.databinding.ItemCardBinding

class ParkingAdapter(
    private val parkingList: MutableList<Result>,
    private val onItemRemoved: (Int) -> Unit // Callback para notificar la eliminación
) : RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCardBinding.bind(view)

        fun bind(parkingResult: Result) {
            binding.cardTitle.text = parkingResult.topic
            binding.elementText.text = when (parkingResult.text) {
                "0" -> "Ocupado"
                "1" -> "Libre"
                else -> "Cargando..."
            }
            // El color de card2 y la imagen se mantienen como en el XML
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parkingList[position])
    }

    override fun getItemCount(): Int = parkingList.size

    // Función para agregar un nuevo parking
    fun addParking(parking: Result) {
        parkingList.add(parking)
        notifyItemInserted(parkingList.size - 1)
    }

    // Función para eliminar un parking en una posición específica
    fun removeParkingAt(position: Int) {
        parkingList.removeAt(position)
        notifyItemRemoved(position)
        onItemRemoved(position) // Notificar a la actividad para guardar los cambios
    }

    // Función para actualizar la lista de parkings
    fun updateParkings(newParkings: List<Result>) {
        parkingList.clear()
        parkingList.addAll(newParkings)
        notifyDataSetChanged()
    }
}