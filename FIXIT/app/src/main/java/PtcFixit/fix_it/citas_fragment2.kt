package PtcFixit.fix_it

import CitasHelpers.AdaptadorCitas
import CitasHelpers.ViewModelCita
import CitasHelpers.tbCita
import Modelo.ClaseConexion
import android.os.Bundle
import android.util.Log
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class citas_fragment2 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    fun obtenerDatos(): List<tbCita> {
        val listadoCitas = mutableListOf<tbCita>()
        try {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT Cita.UUID_cita, Cliente.Nombre, Cita.Dui_empleado, Cita.Fecha_cita, Cita.Hora_cita, Cita.Descripcion FROM Cita INNER JOIN Cliente ON Cita.Dui_cliente = Cliente.Dui_cliente")!!

            while (resultSet.next()) {
                val uuid = resultSet.getString("UUID_cita")
                val cliente = resultSet.getString("Nombre")
                val empleado = resultSet.getString("Dui_empleado")
                val fecha = resultSet.getString("Fecha_cita")
                val hora = resultSet.getString("Hora_cita")
                val descripcion = resultSet.getString("Descripcion")
                val cita = tbCita(uuid, cliente, empleado, fecha, hora, descripcion)
                listadoCitas.add(cita)
            }
        } catch (e: Exception) {
            Log.e("citas_fragment2", "Error fetching citas data", e)
        }
        return listadoCitas
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_citas_fragment2, container, false)
        val rcvCitas = root.findViewById<RecyclerView>(R.id.rcvCitas)
        rcvCitas.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val citasBd = obtenerDatos()
            withContext(Dispatchers.Main) {
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