package RepuestosHelpers

import Modelo.ClaseConexion
import PtcFixit.fix_it.R
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.sql.Connection
import java.sql.SQLException

class AdaptadorRepuestos(private var Datos: List<tbRepuesto>) : RecyclerView.Adapter<ViewHolderRepuestos>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRepuestos {
        return try {
            val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_repuestos_card, parent, false)
            println("Layout inflated successfully")
            ViewHolderRepuestos(vista)
        } catch (e: Exception) {
            e.printStackTrace()
            // Manejar la excepción según sea necesario, por ejemplo, devolver un ViewHolder vacío o mostrar un mensaje de error.
            throw e
        }
    }

    override fun getItemCount(): Int {
        return try {
            Datos.size
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun actualizarListado(nuevaListaRepuestos: List<tbRepuesto>) {
        try {
            Datos = nuevaListaRepuestos
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun actualizarItem(UUID: String, nuevoNombreRepuesto: String, nuevoPrecioRepuesto: Double) {
        try {
            val index = Datos.indexOfFirst { it.UUID_productoRepuesto == UUID }
            if (index != -1) {
                Datos[index].Nombre = nuevoNombreRepuesto
                Datos[index].Precio = nuevoPrecioRepuesto
                notifyItemChanged(index)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun eliminarRepuesto(NombreRep: String, position: Int) {
        val listaDeRepuestos = Datos.toMutableList()
        listaDeRepuestos.removeAt(position)
        CoroutineScope(Dispatchers.IO).launch {
            var objConexion: Connection? = null
            try {
                objConexion = ClaseConexion().cadenaConexion()
                val deleteRepuesto = objConexion?.prepareStatement("DELETE FROM ProductoRepuesto WHERE Nombre = ?")
                deleteRepuesto?.setString(1, NombreRep)
                deleteRepuesto?.executeUpdate()
                objConexion?.commit()
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                try {
                    objConexion?.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
        Datos = listaDeRepuestos.toList()
        notifyItemRemoved(position)
    }

    fun actualizarRepuesto(uuid: String, nombreNuevo: String, precioNuevo: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            var objConexion: Connection? = null
            try {
                objConexion = ClaseConexion().cadenaConexion()
                val updateCarro = objConexion?.prepareStatement("UPDATE ProductoRepuesto SET Nombre = ?, Precio = ? WHERE UUID_productoRepuesto = ?")
                updateCarro?.setString(1, nombreNuevo)
                updateCarro?.setDouble(2, precioNuevo)
                updateCarro?.setString(3, uuid)
                updateCarro?.executeUpdate()
                objConexion?.commit()

                withContext(Dispatchers.Main) {
                    actualizarItem(uuid, nombreNuevo, precioNuevo)
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                try {
                    objConexion?.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolderRepuestos, position: Int) {
        try {
            val item = Datos[position]
            holder.txtNombreRepuesto.text = item.Nombre
            holder.txtCategoria.text = item.UUID_item
            holder.txtPrecio.text = item.Precio.toString()
            holder.txtuuid.text = item.UUID_productoRepuesto

            holder.imgBorrarRep.setOnClickListener {
                val context = holder.itemView.context
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Eliminar")
                builder.setMessage("¿Desea eliminar el Repuesto?")
                builder.setPositiveButton("Sí") { dialog, which ->
                    eliminarRepuesto(item.Nombre, position)
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

            holder.imgActuRep.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
                alertDialogBuilder.setTitle("Actualizar datos del repuesto")
                alertDialogBuilder.setMessage("Ingrese los nuevos datos del repuesto:")

                val layout = LinearLayout(holder.itemView.context)
                layout.orientation = LinearLayout.VERTICAL

                val nuevoNombreRepuesto = EditText(holder.itemView.context)
                nuevoNombreRepuesto.hint = "Nombre"
                layout.addView(nuevoNombreRepuesto)

                val nuevoPrecioRepuesto = EditText(holder.itemView.context)
                nuevoPrecioRepuesto.hint = "Precio"
                nuevoPrecioRepuesto.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                layout.addView(nuevoPrecioRepuesto)

                alertDialogBuilder.setView(layout)

                alertDialogBuilder.setPositiveButton("Actualizar") { dialog, which ->
                    val nuevoNombre = nuevoNombreRepuesto.text.toString()
                    val nuevoPrecio = nuevoPrecioRepuesto.text.toString()
                    if (nuevoNombre.isBlank() || nuevoPrecio.isBlank()) {
                        Toast.makeText(holder.itemView.context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            val precioRepuestoDouble = nuevoPrecio.toDouble()
                            actualizarRepuesto(item.UUID_productoRepuesto, nuevoNombre, precioRepuestoDouble)
                            actualizarItem(item.UUID_productoRepuesto, nuevoNombre, precioRepuestoDouble)
                        } catch (e: NumberFormatException) {
                            Toast.makeText(holder.itemView.context, "El precio debe ser un número válido", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



