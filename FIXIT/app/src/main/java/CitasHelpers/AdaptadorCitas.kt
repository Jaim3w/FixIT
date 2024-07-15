package CitasHelpers

import Modelo.ClaseConexion
import PtcFixit.fix_it.DetalleCitaActivity
import PtcFixit.fix_it.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        if (identificador != -1){
            Datos[identificador].fecha = nuevaFecha
            Datos[identificador].hora = hora
            notifyItemChanged(identificador)
        }else{
            Log.e("ActualizarListado", "UUID no encontrado: $uuid")
        }
    }

    fun editarCita( uuid:String, nuevaFecha:String, nuevaHora:String){
        GlobalScope.launch(Dispatchers.IO){
            //1- Creo un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Creo una variable que contenga un PrepareStatement
            val updateCita = objConexion?.prepareStatement("update Cita set Fecha_cita = ? , Hora_cita = ?  where uuid_cita = ?")!!
            updateCita.setString(1, nuevaFecha)
            updateCita.setString(2, nuevaHora)
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
            alertDialogBuilder.setMessage("Cambie la nueva fecha o hora de la cita:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputFecha = EditText(holder.itemView.context)
            inputFecha.setText(cita.fecha)
            layout.addView(inputFecha)

            val inputHora = EditText(holder.itemView.context)
            inputHora.setText(cita.hora)
            layout.addView(inputHora)

            alertDialogBuilder.setView(layout)

            inputFecha.setOnClickListener {
                val calendario = Calendar.getInstance()
                val anio = calendario.get(Calendar.YEAR)
                val mes = calendario.get(Calendar.MONTH)
                val dia = calendario.get(Calendar.DAY_OF_MONTH)

                val fechaMinima = Calendar.getInstance()
                fechaMinima.set(anio, mes, dia + 1)

                val fechaMaxima = Calendar.getInstance()
                fechaMaxima.set(anio, mes, dia + 10)

                val datePickerDialog = DatePickerDialog(
                    holder.itemView.context,
                    { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                        val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                        inputFecha.setText(fechaSeleccionada)
                    },
                    anio, mes, dia
                )

                datePickerDialog.datePicker.minDate = fechaMinima.timeInMillis
                datePickerDialog.datePicker.maxDate = fechaMaxima.timeInMillis

                datePickerDialog.show()
            }

            inputHora.setOnClickListener {
                val cal = Calendar.getInstance()
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                val minute = cal.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(
                    holder.itemView.context,
                    { _, hourOfDay, minuteOfDay ->
                        if (hourOfDay in 8..15) {
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            cal.set(Calendar.MINUTE, minuteOfDay)
                            val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            val formattedTime = format.format(cal.time)
                            inputHora.setText(formattedTime)
                        } else {
                            Toast.makeText(holder.itemView.context, "Por favor, seleccione una hora entre 8 AM y 3 PM", Toast.LENGTH_SHORT).show()
                        }
                    },
                    hour,
                    minute,
                    false
                )

                timePickerDialog.show()

            }

            alertDialogBuilder.setPositiveButton("Guardar") { dialog, which ->
                val nuevaFecha = inputFecha.text.toString().trim()
                val nuevaHora = inputHora.text.toString().trim()
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
            val context = holder.lblFecha.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Estas seguro que deseas eliminar?")

            builder.setPositiveButton("Si") { dialog, which ->
                val cita = Datos[position]
                eliminarCita(cita.fecha, position)
            }

            builder.setNegativeButton("No") { dialog, wich ->
                //Si doy en clic en "No" se cierra la alerta
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()


        }

//        holder.itemView.setOnClickListener {
//            val context = holder.itemView.context
//            val pantalla = Intent(context, DetalleCitaActivity::class.java)
//
//            pantalla.putExtra(
//                "Dui_cliente", cita.cliente
//            )
//            pantalla.putExtra(
//                "Dui_empleado", cita.empleado
//            )
//            pantalla.putExtra(
//                "Fecha_cita", cita.fecha
//            )
//            pantalla.putExtra(
//                "Hora_cita", cita.hora
//            )
//            pantalla.putExtra(
//                "Descripcion", cita.descripcion
//            )
//            context.startActivity(pantalla)
//
//
//        }



    }

}