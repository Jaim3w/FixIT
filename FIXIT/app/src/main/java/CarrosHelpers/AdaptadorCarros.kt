package CarrosHelpers

import Modelo.ClaseConexion
import PtcFixit.fix_it.R
import android.app.DatePickerDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.SQLException
import java.util.Calendar

class AdaptadorCarros(private var Datos: List<tbCarros>) : RecyclerView.Adapter<ViewHolderCarros>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarros {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_carros_card, parent, false)
        return ViewHolderCarros(vista)
    }

    override fun getItemCount() = Datos.size

    fun actualizarListado(nuevaListaCarro: List<tbCarros>) {
        Datos = nuevaListaCarro
        notifyDataSetChanged()
    }

    fun actualizarItem(placaCarro: String, fechaNuevaCarro: String, colorNuevo: String, nuevaDescripcionCarro: String) {
        val index = Datos.indexOfFirst { it.Placa_carro == placaCarro }
        if (index != -1) {
            Datos[index].FechaRegistro = fechaNuevaCarro
            Datos[index].Color = colorNuevo
            Datos[index].DescripcionCarro = nuevaDescripcionCarro
            notifyItemChanged(index)
        }
    }

    fun eliminarCarro(fechaRegistro: String, position: Int) {
        val listaDeCarros = Datos.toMutableList()
        listaDeCarros.removeAt(position)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val deleteCarro = objConexion?.prepareStatement("DELETE FROM Carro WHERE FechaRegistro = ?")!!
                deleteCarro.setString(1, fechaRegistro)
                deleteCarro.executeUpdate()
                val commit = objConexion.prepareStatement("COMMIT")
                commit.executeUpdate()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        Datos = listaDeCarros.toList()
        notifyItemRemoved(position)
    }

    fun actualizarCarro(placa: String, fechaNuevaCarro: String, colorNuevo: String, nuevaDescripcionCarro: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val updateCarro = objConexion?.prepareStatement("UPDATE Carro SET FechaRegistro = ?, Color = ?, DescripcionCarro = ? WHERE Placa_carro = ?")!!
                updateCarro.setString(1, fechaNuevaCarro)
                updateCarro.setString(2, colorNuevo)
                updateCarro.setString(3, nuevaDescripcionCarro)
                updateCarro.setString(4, placa)
                updateCarro.executeUpdate()
                val commit = objConexion.prepareStatement("COMMIT")
                commit.executeUpdate()

                // Reflejar los cambios en la UI
                launch(Dispatchers.Main) {
                    actualizarItem(placa, fechaNuevaCarro, colorNuevo, nuevaDescripcionCarro)
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolderCarros, position: Int) {
        val item = Datos[position]
        holder.txtClienteCarrosCard.text = item.Dui_cliente
        holder.txtPlacaCarrosCard.text = item.Placa_carro
        holder.txtFechaRegistroCarrosCard.text = item.FechaRegistro
        holder.txtDescripcionCarrosCard.text = item.DescripcionCarro
        holder.txtColorCarrosCard.text = item.Color

        holder.imgEliminarCarros.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el Carro?")
            builder.setPositiveButton("Sí") { dialog, which ->
                eliminarCarro(item.FechaRegistro, position)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.imgActualizarCarro.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Actualizar datos del carro")
            alertDialogBuilder.setMessage("Ingrese los nuevos datos del carro:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val nuevaFecha = EditText(holder.itemView.context)
            nuevaFecha.hint = "Fecha de registro"
            layout.addView(nuevaFecha)

            val nuevoColor = EditText(holder.itemView.context)
            nuevoColor.hint = "Color"
            layout.addView(nuevoColor)

            val nuevaDescripcion = EditText(holder.itemView.context)
            nuevaDescripcion.hint = "Descripción"
            layout.addView(nuevaDescripcion)

            alertDialogBuilder.setView(layout)

            nuevaFecha.inputType = InputType.TYPE_NULL
            nuevaFecha.setOnClickListener {
                val calendario = Calendar.getInstance()
                val anio = calendario.get(Calendar.YEAR)
                val mes = calendario.get(Calendar.MONTH)
                val dia = calendario.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    holder.itemView.context,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val fechaSeleccionada = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                        nuevaFecha.setText(fechaSeleccionada)
                    },
                    anio, mes, dia
                )

                datePickerDialog.datePicker.maxDate = calendario.timeInMillis

                datePickerDialog.show()
            }

            alertDialogBuilder.setPositiveButton("Actualizar") { dialog, which ->
                val fechaNuevaCarro = nuevaFecha.text.toString()
                val colorNuevo = nuevoColor.text.toString()
                val nuevaDescripcionCarro = nuevaDescripcion.text.toString()

                if (fechaNuevaCarro.isBlank() || colorNuevo.isBlank() || nuevaDescripcionCarro.isBlank()) {
                    Toast.makeText(holder.itemView.context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                } else {
                    actualizarCarro(item.Placa_carro, fechaNuevaCarro, colorNuevo, nuevaDescripcionCarro)
                    actualizarItem(item.Placa_carro, fechaNuevaCarro, colorNuevo, nuevaDescripcionCarro)
                }
            }

            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}