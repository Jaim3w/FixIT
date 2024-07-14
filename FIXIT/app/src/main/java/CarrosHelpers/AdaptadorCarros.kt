package CarrosHelpers

import Modelo.ClaseConexion
import PtcFixit.fix_it.CitasHelpers.ViewHolderCitas
import PtcFixit.fix_it.CitasHelpers.tbCita
import PtcFixit.fix_it.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import recyclerViewHelper_CarrosAdmin.viewHolder



class AdaptadorCarros (private var Datos: List<tbCarros>): RecyclerView.Adapter<ViewHolderCarros>(){

    fun actualizarItem(Placa_carro: String, nuevadescripcionCarro: String ){
        val index = Datos.indexOfFirst { it.Placa_carro == Placa_carro  }
        Datos[index].DescripcionCarro=nuevadescripcionCarro
        notifyItemChanged(index)
    }
    fun actualizarListado(nuevaListaCarro:List<tbCarros>)
    {
        Datos=nuevaListaCarro
        notifyDataSetChanged()

    }

    fun EliminarCarro(Placa_carro:String,position: Int){
        val listaDeCarros = Datos.toMutableList()
        listaDeCarros.removeAt(position)
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deleteCaro =objConexion?.prepareStatement("delete Carro where Placa_carro = ?")!!
            deleteCaro.setString(1,Placa_carro)
            deleteCaro.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos=listaDeCarros.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
    fun ActualizarCarro(nuevadescripcionCarro:String ,placa:String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updateTicket =objConexion?.prepareStatement("update Carro set Descripcion = ? where Placa_carro = ?")!!
            updateTicket.setString(1,nuevadescripcionCarro)
            updateTicket.setString(2,placa)
            updateTicket.executeUpdate()


            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()


            withContext(Dispatchers.Main){
                actualizarItem(placa,nuevadescripcionCarro)
            }

        }




    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarros {
        val vista =
            LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_carros_card, parent, false)

        return ViewHolderCarros(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderCarros, position: Int) {

        val item = Datos[position]
        holder.imgCarros.tag = item.ImagenCarro
        holder.txtnombreClienteCarros.text = item.Dui_cliente
        holder.txtPlacaCardCarros.text = item.Placa_carro
        holder.txtModeloCardCarros.text = item.UUID_modelo
        holder.txtServicioCardCarros.text = item.DescripcionCarro

        holder.imgEliminarCarros.setOnClickListener {
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el Carro?")

            builder.setPositiveButton("Si")
            { dialog, which ->
                EliminarCarro(item.Placa_carro, position)
            }
            builder.setNegativeButton("No")
            { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()



        }
        holder.imgActualizarCarro.setOnClickListener {
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar la descripcion del carro")
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(item.DescripcionCarro)
            builder.setView(cuadroTexto)
            builder.setPositiveButton("Actualizar")
            { dialog, which ->
                ActualizarCarro(cuadroTexto.text.toString(), item.Placa_carro)
            }
            builder.setNegativeButton("Cancelar")
            { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()


        }

    }


}