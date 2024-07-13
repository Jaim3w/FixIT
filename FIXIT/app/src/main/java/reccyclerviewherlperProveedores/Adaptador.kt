package reccyclerviewherlperProveedores

import Modelo.RCVproveedor
import PtcFixit.fix_it.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import recyclerViewHelper_CarrosAdmin.viewHolder

class Adaptador(var Datos :List<RCVproveedor>): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vistaProv = LayoutInflater.from(parent.context).inflate(R.layout.activity_proveedor_card, parent, false)
        return ViewHolder(vistaProv)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.txtProveedor.text = item.nombre
        holder.txtTelefono.text = item.telefono
    }

}