package CitasHelpers

import Modelo.ClaseConexion
import PtcFixit.fix_it.R
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdaptadorCitas(private var Datos: List<tbCita>) : RecyclerView.Adapter<ViewHolderCitas>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCitas {
         val vista =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_citas, parent, false)

        return ViewHolderCitas(vista)

    }

    override fun getItemCount() = Datos.size


    fun actualizarCitaRecyclerView(nuevaLista: List<tbCita>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarCita(fecha: String, posicion: Int){
        val listadoDatos = Datos.toMutableList()
        listadoDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){

            val objConexion = ClaseConexion().cadenaConexion()

            //2- Creo una variable que contenga un PrepareStatement
            val deleteCita = objConexion?.prepareStatement("delete Cita where Fecha_cita = ?")!!
            deleteCita.setString(1, fecha)
            deleteCita.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos = listadoDatos.toList()

        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    fun actualizarListadoDespuesDeEditar(uuid: String, nuevaFecha: String, hora:String){

        val identificador = Datos.indexOfFirst { it.uuid == uuid }

        Datos[identificador].fecha = nuevaFecha
        Datos[identificador].hora = hora

        notifyItemChanged(identificador)
    }

    fun editarCita(fecha:String, hora:String, uuid:String){
        GlobalScope.launch(Dispatchers.IO){
            //1- Creo un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Creo una variable que contenga un PrepareStatement
            val updateCita = objConexion?.prepareStatement("update Cita set Fecha_cita = ? , Hora_cita = ?  where uuid_cita = ?")!!
            updateCita.setString(1, fecha)
            updateCita.setString(2, hora)
            updateCita.setString(3, uuid)
            updateCita.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }

    }

    override fun onBindViewHolder(holder: ViewHolderCitas, position: Int) {

        val cita = Datos[position]
        holder.lblcliente.text = cita.cliente
        holder.lblFecha.text = cita.fecha
        holder.lblHora.text = cita.hora

        holder.imgEditar.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Editar cita")
            alertDialogBuilder.setMessage("Ingrese el nuevo titulo de la cita:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputTitulo = EditText(holder.itemView.context)
            inputTitulo.setText(cita.fecha)
            inputTitulo.setText(cita.hora)
            layout.addView(inputTitulo)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Guardar") { dialog, which ->
                val nuevaFecha = inputTitulo.text.toString().trim()
                val nuevaHora = inputTitulo.text.toString().trim()
                if (nuevaFecha.isNotEmpty() && nuevaHora.isNotEmpty()) {
                    editarCita(cita.uuid, nuevaFecha, nuevaHora)
                    actualizarListadoDespuesDeEditar(cita.uuid, nuevaFecha, nuevaHora)
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

        holder.imgEliminar.setOnClickListener {
            val context = holder.lblcliente.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Estas seguro que deseas eliminar?")

            builder.setPositiveButton("Si") { dialog, which ->
                val cita = Datos[position]
                eliminarCita(cita.cliente, position)
            }

            builder.setNegativeButton("No") { dialog, wich ->
                //Si doy en clic en "No" se cierra la alerta
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()


        }

    }

}