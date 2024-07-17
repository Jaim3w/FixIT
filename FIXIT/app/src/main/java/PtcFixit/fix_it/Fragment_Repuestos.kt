package PtcFixit.fix_it

import Modelo.ClaseConexion
import RepuestosHelpers.AdaptadorRepuestos
import RepuestosHelpers.tbRepuesto
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Repuestos.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Repuestos : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    fun obtenerDatosRep(): List<tbRepuesto> {
        val listadoRepuestos = mutableListOf<tbRepuesto>()
        val filtroUUIDItem = "710E1E1319F6450D8B3278B4B2FDC95D"
        try {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery(
                """
                SELECT ProductoRepuesto.UUID_productoRepuesto, ProductoRepuesto.Nombre, ProductoRepuesto.ImagenProductoRepuesto, CategoriaItem.Nombre, ProductoRepuesto.Precio 
                FROM ProductoRepuesto 
                INNER JOIN CategoriaItem ON ProductoRepuesto.UUID_item = CategoriaItem.UUID_item 
                WHERE CategoriaItem.UUID_item = '$filtroUUIDItem'
            """
            )!!

            while (resultSet.next()) {
                val uuid = resultSet.getString("UUID_productoRepuesto")
                val nombre = resultSet.getString("Nombre")
                val imagen = resultSet.getString("ImagenProductoRepuesto")
                val nombreitem = resultSet.getString("Nombre")
                val precio = resultSet.getDouble("Precio")
                val repuesto = tbRepuesto(uuid, nombre, imagen, nombreitem, precio)
                listadoRepuestos.add(repuesto)
            }
        } catch (e: Exception) {
            Log.e("Fragment_Repuestos", "Error fetching Repuestos data", e)
        }
        return listadoRepuestos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment__repuestos, container, false)
        val rcvRepuesto = root.findViewById<RecyclerView>(R.id.rcvRep)
        rcvRepuesto.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val RepuestosBd = obtenerDatosRep()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorRepuestos(RepuestosBd)
                rcvRepuesto.adapter = adapter
            }
        }
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Repuestos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Repuestos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}