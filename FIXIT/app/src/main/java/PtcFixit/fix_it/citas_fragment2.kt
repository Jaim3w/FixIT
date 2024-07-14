package PtcFixit.fix_it

import CitasHelpers.AdaptadorCitas
import CitasHelpers.ViewModelCita
import CitasHelpers.tbCita
import Modelo.ClaseConexion
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import reccyclerviewherlperProveedores.Adaptador

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [citas_fragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class citas_fragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    fun obtenerDatos(): List<tbCita>{
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("select * from Cita")!!

        val listadoCitas = mutableListOf<tbCita>()

        while (resultSet.next()){
            val uuid = resultSet.getString("UUID_cita")
            val cliente = resultSet.getString("Dui_cliente")
            val empleado = resultSet.getString("Dui_empleado")
            val fecha = resultSet.getString("Fecha_cita")
            val hora = resultSet.getString("Hora_cita")
            val descripcion = resultSet.getString("Descripcion")
            val cita = tbCita(uuid,cliente, empleado, fecha, hora, descripcion)
            listadoCitas.add(cita)
        }
        return listadoCitas
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_citas_fragment2, container, false)

        val rcvCitas = root.findViewById<RecyclerView>(R.id.rcvCitas)

        rcvCitas.layoutManager = LinearLayoutManager(context)



        CoroutineScope(Dispatchers.IO).launch {
            val citasBd = obtenerDatos()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorCitas(citasBd)
                rcvCitas.adapter = adapter
            }
        }

        return root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment citas_fragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            citas_fragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}