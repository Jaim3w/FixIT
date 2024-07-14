package RecyclerViewHelpersMain

import Modelo.listadoCita
import PtcFixit.fix_it.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCitas(
    private var citas: List<listadoCita>,
    private val context: Context
) : RecyclerView.Adapter<AdaptadorCitas.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_cita, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cita = citas[position]
        holder.bind(cita)
    }

    override fun getItemCount(): Int {
        return citas.size
    }

    fun actualizarCitas(nuevasCitas: List<listadoCita>) {
        this.citas = nuevasCitas
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtCliente: TextView = view.findViewById(R.id.txtCliente)
        private val btnDetallesCitas: Button = view.findViewById(R.id.btnDetallesCitas)

        fun bind(cita: listadoCita) {
            txtCliente.text = cita.Nombre_cliente
        }
    }
}