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

    fun actualizarProveedores(nuevoNombre:String, NuevoTelefono: String, nuevoApellido:String, nuevoCorreo:String, nuevaDireccion:String, dui: String){
        val indentificador = Datos.indexOfFirst{it.dui == dui}
        Datos[indentificador].nombre = nuevoNombre
        Datos[indentificador].telefono = NuevoTelefono
        Datos[indentificador].apellido = nuevoApellido
        Datos[indentificador].correo = nuevoCorreo
        Datos[indentificador].direccion = nuevaDireccion

        notifyItemChanged(indentificador)
    }

    fun editarProveedores(nombre: String, telefono: String, apellido: String, correo: String, direccion: String, dui: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val actProveedoress = objConexion?.prepareStatement("update Proveedor set Nombre = ?, Apellido = ?, Telefono = ?, Correo_Electronico = ?, Direccion = ? where Dui_proveedor = ?")!!
            actProveedoress.setString(1, nombre)
            actProveedoress.setString(2, apellido)
            actProveedoress.setString(3, telefono)
            actProveedoress.setString(4, correo)
            actProveedoress.setString(5, direccion)
            actProveedoress.setString(6, dui)
            actProveedoress.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
    }

    fun eliminarProveedor(noombre: String, position: Int) {
        val listaProv = Datos.toMutableList()
        listaProv.removeAt(position)

        
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