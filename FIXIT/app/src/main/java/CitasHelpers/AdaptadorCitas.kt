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

    override fun onBindViewHolder(holder: ViewHolderCitas, position: Int) {
        val item =Datos[position]
        holder.lblcliente.text =item.cliente
        holder.lblFecha.text = item.fecha
        holder.lblHora.text = item.hora

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            //val pantallaDetalleCitas = Intent(context, detalle_citas::class.java)



        }
    }

    fun actualizarCitaRecyclerView(nuevaLista: List<tbCita>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarCita(fecha: String, posicion: Int){
        val listadoDatos = Datos.toMutableList()
        listadoDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){

            val objConexion = ClaseConexion.cade()

            //2- Creo una variable que contenga un PrepareStatement
            val deleteProducto = objConexion?.prepareStatement("delete tbProductos1 where nombreProducto = ?")!!
            deleteProducto.setString(1, nombreProducto)
            deleteProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
    }


}