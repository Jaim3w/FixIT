package RecyclerViewHelpersMain

import Modelo.listadoCita
import PtcFixit.fix_it.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCitas(private var listaCitas: List<listadoCita>, private val context: Context) : RecyclerView.Adapter<AdaptadorCitas.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewNombreCliente: TextView = view.findViewById(R.id.txtClienteCarroCard)

        fun bind(Nombre_cliente: String) {
            textViewNombreCliente.text = Nombre_cliente
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cita, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cita = listaCitas[position]
        holder.bind(cita.Nombre_cliente)
    }

    override fun getItemCount(): Int {
        return listaCitas.size
    }

    fun actualizarCitas(nuevasCitas: List<listadoCita>) {
        listaCitas = nuevasCitas
        notifyDataSetChanged()
    }
}