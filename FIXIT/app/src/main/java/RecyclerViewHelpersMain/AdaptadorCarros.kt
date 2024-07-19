package RecyclerViewHelpersMain

import Modelo.listadoCarros
import Modelo.listadoCita
import PtcFixit.fix_it.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCarros(private var listaCarros: List<listadoCarros>, private val context: Context) : RecyclerView.Adapter<AdaptadorCarros.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtPlaca: TextView = view.findViewById(R.id.placa_val)
        val txtAño: TextView = view.findViewById(R.id.año_val)
        val txtRegistro: TextView = view.findViewById(R.id.registro_val)

        fun bind(carro: listadoCarros) {
            txtPlaca.text = carro.Placa_carro
            txtAño.text = carro.Año
            txtRegistro.text = carro.Fecha_registro
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_menu_coches_despachados_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carro = listaCarros[position]
        holder.bind(carro)
    }

    override fun getItemCount(): Int {
        return listaCarros.size
    }

    fun actualizarCarros(nuevosCarros: List<listadoCarros>) {
        listaCarros = nuevosCarros
        notifyDataSetChanged()
    }
}