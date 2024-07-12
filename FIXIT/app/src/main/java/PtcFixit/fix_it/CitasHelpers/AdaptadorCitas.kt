package PtcFixit.fix_it.CitasHelpers

import PtcFixit.fix_it.R
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCitas(private var Datos: List<tbCita>) : RecyclerView.Adapter<ViewHolderCitas>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCitas {
         val vista =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_citas_fragment2, parent, false)

        return ViewHolderCitas(vista)

    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderCitas, position: Int) {
        val item =Datos[position]
        holder.lblcliente.text =item.cliente
        holder.lblFecha.text = item.fecha
        holder.lblHora.text = item.hora

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            //val pantallaDetalleCitas = Intent(context, detalle_citas::class.java)





        }
    }


}