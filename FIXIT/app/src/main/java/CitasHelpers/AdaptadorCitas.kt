package CitasHelpers

import Modelo.ClaseConexion
import PtcFixit.fix_it.R
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdaptadorCitas(private var Datos: List<tbCita>) : RecyclerView.Adapter<ViewHolderCitas>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCitas {
         val vista =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_citas_fragment2, parent, false)

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

    }

    holder.imgeditar.setOnClickListener{

    }

}