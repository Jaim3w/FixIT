package reccyclerviewherlperProveedores

import Modelo.ClaseConexion
import Modelo.RCVproveedor
import PtcFixit.fix_it.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import recyclerViewHelper_CarrosAdmin.viewHolder
import java.sql.SQLException

class Adaptador(var Datos :List<RCVproveedor>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarRecyclerView(nuevaLista: List<RCVproveedor>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun actualizarItemprov(dui: String, nombre: String, telefono:String) {
        val index = Datos.indexOfFirst { it.dui == dui }
        if (index != -1) {
            Datos[index].nombre = nombre
            Datos[index].telefono = telefono
            notifyItemChanged(index)
        }
    }

    fun editarProveedores(nombre: String, telefono: String, dui: String){
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()

            try {
                val actProveedoress = objConexion?.prepareStatement(
                    "UPDATE Proveedor SET Nombre = ?, Telefono = ? WHERE Dui_proveedor = ?"
                )
                actProveedoress?.setString(1, nombre)
                actProveedoress?.setString(2, telefono)
                actProveedoress?.setString(3, dui)
                actProveedoress?.executeUpdate()

                val commit = objConexion?.prepareStatement("COMMIT")
                commit?.executeUpdate()
            } catch (e: SQLException) {
                Log.e("editarProveedores", "Error al actualizar proveedor: ${e.message}")
            } finally {
                objConexion?.close()
            }
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
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Editar Proveedor")
            alertDialogBuilder.setMessage("Cambie los datos del proveedor:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputNombre = EditText(holder.itemView.context)
            inputNombre.setText(item.nombre)
            layout.addView(inputNombre)

            val inputTelefono = EditText(holder.itemView.context)
            inputTelefono.setText(item.telefono)
            layout.addView(inputTelefono)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Actualizar") { dialog, _ ->
                val nuevoNombre = inputNombre.text.toString()
                val nuevoTelefono = inputTelefono.text.toString()

                // Llama a la función editarProveedores para actualizar en la base de datos
                editarProveedores(item.dui, nuevoNombre, nuevoTelefono, )
                actualizarItemprov(item.dui, nuevoNombre, nuevoTelefono)
            }

            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
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