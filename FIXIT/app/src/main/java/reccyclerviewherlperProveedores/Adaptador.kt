package reccyclerviewherlperProveedores

import Modelo.ClaseConexion
import Modelo.RCVproveedor
import PtcFixit.fix_it.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.SQLException

class Adaptador(var Datos: List<RCVproveedor>,  private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarRecyclerView(nuevaLista: List<RCVproveedor>) {
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun actualizarItemprov(dui: String, nombre: String, telefono: String, apellido: String, correo: String, direccion: String) {
        val index = Datos.indexOfFirst { it.dui == dui }
        if (index != -1) {
            Datos[index].nombre = nombre
            Datos[index].telefono = telefono
            Datos[index].apellido = apellido
            Datos[index].correo = correo
            Datos[index].direccion = direccion
            notifyItemChanged(index)
        }
    }

    fun editarProveedores(dui: String, nombre: String, apellido: String, telefono: String, correo: String, direccion: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            try {
                val actProveedoress = objConexion?.prepareStatement(
                    "UPDATE Proveedor SET Nombre = ?, Apellido = ?, Telefono = ?, Correo_Electronico = ?, Direccion = ? WHERE Dui_proveedor = ?"
                )
                actProveedoress?.setString(1, nombre)
                actProveedoress?.setString(2, apellido)
                actProveedoress?.setString(3, telefono)
                actProveedoress?.setString(4, correo)
                actProveedoress?.setString(5, direccion)
                actProveedoress?.setString(6, dui)
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
            try {
                val eliminarProv = objConexion?.prepareStatement("DELETE FROM Proveedor WHERE Nombre = ?")
                eliminarProv?.setString(1, nombre)
                eliminarProv?.executeUpdate()

                val commit = objConexion?.prepareStatement("COMMIT")
                commit?.executeUpdate()
            } catch (e: SQLException) {
                Log.e("eliminarProveedor", "Error al eliminar proveedor: ${e.message}")
            } finally {
                objConexion?.close()
            }
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

        holder.itemView.setOnClickListener {
            ShowProveedoresInfo(item)
        }

        holder.imgEditar.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Editar Proveedor")
            alertDialogBuilder.setMessage("Cambie los datos del proveedor:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputNombre = EditText(holder.itemView.context)
            inputNombre.setText(item.nombre)
            layout.addView(inputNombre)

            val inputApellido = EditText(holder.itemView.context)
            inputApellido.setText(item.apellido)
            layout.addView(inputApellido)

            val inputTelefono = EditText(holder.itemView.context)
            inputTelefono.setText(item.telefono)
            layout.addView(inputTelefono)

            val inputCorreo = EditText(holder.itemView.context)
            inputCorreo.setText(item.correo)
            layout.addView(inputCorreo)

            val inputDireccion = EditText(holder.itemView.context)
            inputDireccion.setText(item.direccion)
            layout.addView(inputDireccion)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Actualizar") { dialog, _ ->
                val nuevoNombre = inputNombre.text.toString().trim()
                val nuevoTelefono = inputTelefono.text.toString().trim()
                val nuevoApellido = inputApellido.text.toString().trim()
                val nuevoCorreo = inputCorreo.text.toString().trim()
                val nuevoDireccion = inputDireccion.text.toString().trim()

                if (nuevoNombre.isBlank() || nuevoTelefono.isBlank()) {
                    Toast.makeText(holder.itemView.context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        editarProveedores(item.dui, nuevoNombre, nuevoApellido, nuevoTelefono, nuevoCorreo, nuevoDireccion)
                        actualizarItemprov(item.dui, nuevoNombre, nuevoApellido, nuevoTelefono, nuevoCorreo, nuevoDireccion)
                    } catch (e: Exception) {
                        Log.e("onBindViewHolder", "Error al actualizar proveedor: ${e.message}")
                    }
                }
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
                try {
                    eliminarProveedor(item.nombre, position)
                } catch (e: Exception) {
                    Log.e("onBindViewHolder", "Error al eliminar proveedor: ${e.message}")
                }
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    fun ShowProveedoresInfo (proveedor: RCVproveedor) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_verproveedores, null)

        val textViewNombres = dialogView.findViewById<TextView>(R.id.tvNombre)
        val textViewApellidos = dialogView.findViewById<TextView>(R.id.tvApellidos)
        val textViewTelefono = dialogView.findViewById<TextView>(R.id.tvTelefonoProveedor)
        val textViewCorreo = dialogView.findViewById<TextView>(R.id.tvCorreo)
        val textViewDui = dialogView.findViewById<TextView>(R.id.tvDui)
        val textViewDireccion = dialogView.findViewById<TextView>(R.id.txtDescripcion)

        textViewNombres.text = "${proveedor.nombre}"
        textViewApellidos.text = "${proveedor.apellido}"
        textViewTelefono.text = "${proveedor.telefono}"
        textViewCorreo.text = "${proveedor.correo}"
        textViewDui.text = "${proveedor.dui}"
        textViewDireccion.text = "${proveedor.direccion}"

        android.app.AlertDialog.Builder(context)
            .setTitle("Información del Proveedor")
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }



}