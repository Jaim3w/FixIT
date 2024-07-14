package reccyclerviewherlperProveedores

import Modelo.ClaseConexion
import Modelo.RCVproveedor
import PtcFixit.fix_it.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import recyclerViewHelper_CarrosAdmin.viewHolder

class Adaptador(var Datos :List<RCVproveedor>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarRecyclerView(nuevaLista: List<RCVproveedor>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun actualizarProveedores(nuevoNombre:String, Nuevotelefono: String, Nombre : String){
        val indentificador = Datos.indexOfFirst{it.nombre == Nombre}
        Datos[indentificador].nombre = nuevoNombre
        Datos[indentificador].telefono = Nuevotelefono

        notifyItemChanged(indentificador)
    }

    fun editarProveedores(nombre: String,telefono: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val actProveedoress = objConexion?.prepareStatement("update Proveedor set Nombre = ?, Telefono = ? where Nombre = ?")!!
            actProveedoress.setString(1, nombre)
            actProveedoress.setString(2, telefono)
            actProveedoress.executeUpdate()



        }
    }

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