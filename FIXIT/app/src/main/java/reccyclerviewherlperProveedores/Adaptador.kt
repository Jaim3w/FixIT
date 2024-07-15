package reccyclerviewherlperProveedores

import Modelo.ClaseConexion
import Modelo.RCVproveedor
import PtcFixit.fix_it.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

    fun eliminarProveedor(nombre: String, position: Int) {
        val listaProv = Datos.toMutableList()
        listaProv.removeAt(position)

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            val eliminarProv = objConexion?.prepareStatement("delete from Proveedor where  Nombre = ?")!!
            eliminarProv.setString(1, nombre)
            eliminarProv.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }

        Datos = listaProv.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
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

        holder.imgEditar.setOnClickListener {
            val context = holder.imgEditar.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar Proveedor")
            builder.setMessage("Editar proveedor: ${item.nombre}")

            builder.setPositiveButton("Actualizar") { dialog, _ ->
                val nuevoNombre = "Nuevo Nombre"
                val nuevoTelefono = "Nuevo Teléfono"
                val nuevoApellido = "Nuevo Apellido"
                val nuevoCorreo = "Nuevo Correo"
                val nuevaDireccion = "Nueva Dirección"

                // Llama a la función editarProveedores para actualizar en la base de datos
                editarProveedores(nuevoNombre, nuevoTelefono, nuevoApellido, nuevoCorreo, nuevaDireccion, item.dui)
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        holder.imgBorrar.setOnClickListener {
            val context = holder.txtProveedor.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar Proveedor")
            builder.setMessage("¿Deseas continuar con la eliminación del proveedor?")

            builder.setPositiveButton("Continuar") { _, _ ->
                eliminarProveedor(item.nombre, position)
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }
}