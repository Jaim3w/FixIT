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

    fun actualizarCarro(placa: String, nuevaImagen: String, fechaNuevaCarro: String, colorNuevo: String, nuevaDescripcionCarro: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            val updateCarro = objConexion?.prepareStatement("UPDATE Carro SET ImagenCarro = ?, FechaRegistro = ?, Color = ?, DescripcionCarro = ? WHERE Placa_carro = ?")!!
            updateCarro.setString(1, nuevaImagen)
            updateCarro.setString(2, fechaNuevaCarro)
            updateCarro.setString(3, colorNuevo)
            updateCarro.setString(4, nuevaDescripcionCarro)
            updateCarro.setString(5, placa)
            updateCarro.executeUpdate()
            val commit = objConexion.prepareStatement("COMMIT")
            commit.executeUpdate()
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
            alertDialogBuilder.setTitle("Actualizar la descripción del carro")
            alertDialogBuilder.setMessage("Ingrese la nueva descripción del carro:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val nuevoDescripcion = EditText(holder.itemView.context)
            nuevoDescripcion.setText(item.DescripcionCarro)
            layout.addView(nuevoDescripcion)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Guardar") { dialog, which ->
                val nuevaDescripcion = nuevoDescripcion.text.toString().trim()
                if (nuevaDescripcion.isNotEmpty()) {
                    actualizarCarro(item.Placa_carro, item.ImagenCarro, item.FechaRegistro, item.Color, nuevaDescripcion)
                    actualizarItem(item.Placa_carro, item.ImagenCarro, item.FechaRegistro, item.Color, nuevaDescripcion)
                } else {
                    Toast.makeText(holder.itemView.context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
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