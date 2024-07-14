package CarrosHelpers

import PtcFixit.fix_it.CitasHelpers.ViewHolderCitas
import PtcFixit.fix_it.CitasHelpers.tbCita
import PtcFixit.fix_it.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import recyclerViewHelper_CarrosAdmin.viewHolder

class AdaptadorCarros (private var Datos: List<tbCarros>): RecyclerView.Adapter<ViewHolderCarros>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarros {
        val vista =
            LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_carros_card, parent, false)

        return ViewHolderCarros(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderCarros, position: Int) {

        val item = Datos[position]
        holder.imgCarros.tag = item.ImagenCarro
        holder.txtnombreClienteCarros.text = item.Dui_cliente
        holder.txtPlacaCardCarros.text = item.Placa_carro
        holder.txtModeloCardCarros.text = item.UUID_modelo
        holder.txtServicioCardCarros.text = item.DescripcionCarro

    }


}