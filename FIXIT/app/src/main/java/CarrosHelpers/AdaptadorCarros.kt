package CarrosHelpers

import Modelo.ClaseConexion
import PtcFixit.fix_it.R
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

    fun actualizarItem(placaCarro: String, nuevaImagen: String, fechaNuevaCarro: String, colorNuevo: String, nuevaDescripcionCarro: String) {

        val index = Datos.indexOfFirst { it.Placa_carro == placaCarro }
        if (index != -1) {
            Datos[index].ImagenCarro = nuevaImagen
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
            val objConexion = ClaseConexion().cadenaConexion()
            val deleteCarro = objConexion?.prepareStatement("DELETE FROM Carro WHERE FechaRegistro = ?")!!
            deleteCarro.setString(1, fechaRegistro)
            deleteCarro.executeUpdate()
            val commit = objConexion.prepareStatement("COMMIT")
            commit.executeUpdate()
        }
        Datos = listaDeCarros.toList()
        notifyItemRemoved(position)
    }


    fun actualizarCarro(placa: String, nuevaImagen: String, fechaNuevaCarro: String, anioNuevo: String, colorNuevo: String, nuevaDescripcionCarro: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Crear una instancia de ClaseConexion y obtener la conexión
                val objConexion = ClaseConexion().cadenaConexion()

                // Crear una variable que contenga un PrepareStatement
                val updateCarro = objConexion?.prepareStatement("UPDATE Carro SET ImagenCarro = ?, FechaRegistro = ?, Anio = ?, Color = ?, DescripcionCarro = ? WHERE Placa_carro = ?")!!
                updateCarro.setString(1, nuevaImagen)
                updateCarro.setString(2, fechaNuevaCarro)
                updateCarro.setString(3, anioNuevo)
                updateCarro.setString(4, colorNuevo)
                updateCarro.setString(5, nuevaDescripcionCarro)
                updateCarro.setString(6, placa)
                updateCarro.executeUpdate()

                // Realizar el commit de la transacción
                val commit = objConexion.prepareStatement("COMMIT")
                commit.executeUpdate()
            } catch (e: SQLException) {
                // Manejo de excepciones de SQL
                e.printStackTrace()
                // Aquí puedes manejar o registrar la excepción según sea necesario
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolderCarros, position: Int) {

        val item = Datos[position]
        holder.imgCarros.tag = item.ImagenCarro
        holder.txtfechaRegistroCard.text = item.FechaRegistro
        holder.txtColorCarroCard.text = item.Color
        holder.txtanioCarro.text = item.Anio
        holder.txtServicioCardCarros.text = item.DescripcionCarro

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

            val nuevoAnio = EditText(holder.itemView.context)
            nuevoAnio.setText(item.Anio)
            layout.addView(nuevoAnio)

            val nuevoColor = EditText(holder.itemView.context)
            nuevoColor.setText(item.Color)
            layout.addView(nuevoColor)

            val nuevaDescripcion = EditText(holder.itemView.context)
            nuevaDescripcion.setText(item.DescripcionCarro)
            layout.addView(nuevaDescripcion)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Guardar") { dialog, which ->
                val anioNuevo = nuevoAnio.text.toString().trim()
                val colorNuevo = nuevoColor.text.toString().trim()
                val nuevaDescripcion = nuevaDescripcion.text.toString().trim()

                if (anioNuevo.isNotEmpty() && colorNuevo.isNotEmpty() && nuevaDescripcion.isNotEmpty()) {
                    actualizarCarro(
                        item.Placa_carro,
                        item.ImagenCarro,
                        item.FechaRegistro,
                        anioNuevo,
                        colorNuevo,
                        nuevaDescripcion
                    )
                    // Aquí puedes realizar cualquier otra acción después de actualizar el carro, como actualizar la interfaz de usuario.
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Complete todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
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