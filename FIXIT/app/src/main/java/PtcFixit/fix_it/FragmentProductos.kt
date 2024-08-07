package PtcFixit.fix_it

import Modelo.ClaseConexion
import RepuestosHelpers.AdaptadorRepuestos
import RepuestosHelpers.tbRepuesto
import android.database.SQLException
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
import java.sql.ResultSet
import java.sql.Statement

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProductos.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProductos : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    fun obtenerDatosRep(): List<tbRepuesto> {
        val listadoRepuestos = mutableListOf<tbRepuesto>()
        val filtroUUIDItem = "B56858A5442B453E9959F50E236ED9D1"
        var objConexion: java.sql.Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            objConexion = ClaseConexion().cadenaConexion()
            if (objConexion != null) {
                statement = objConexion.createStatement()
                resultSet = statement.executeQuery(
                    """
                SELECT ProductoRepuesto.UUID_productoRepuesto, ProductoRepuesto.Nombre, ProductoRepuesto.ImagenProductoRepuesto, 
                       CategoriaItem.Nombre AS CategoriaNombre, ProductoRepuesto.Precio 
                FROM ProductoRepuesto 
                INNER JOIN CategoriaItem ON ProductoRepuesto.UUID_item = CategoriaItem.UUID_item 
                WHERE CategoriaItem.UUID_item = '$filtroUUIDItem'
                ORDER BY ProductoRepuesto.Nombre
                """
                )

                while (resultSet.next()) {
                    val uuid = resultSet.getString("UUID_productoRepuesto")
                    val nombre = resultSet.getString("Nombre")
                    val imagen = resultSet.getString("ImagenProductoRepuesto")
                    val categoriaNombre = resultSet.getString("CategoriaNombre")
                    val precio = resultSet.getDouble("Precio")
                    val repuesto = tbRepuesto(uuid, categoriaNombre, nombre, imagen, precio)
                    listadoRepuestos.add(repuesto)
                }
            } else {
                Log.e("Fragment_Repuestos", "objConexion es null")
            }
        } catch (e: SQLException) {
            Log.e("Fragment_Repuestos", "Error SQL al obtener datos de repuestos", e)
        } catch (e: Exception) {
            Log.e("Fragment_Repuestos", "Error inesperado al obtener datos de repuestos", e)
        } finally {
            resultSet?.close()
            statement?.close()
            objConexion?.close()
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
        val root = inflater.inflate(R.layout.fragment_productos, container, false)
        val rcvRepuesto = root.findViewById<RecyclerView>(R.id.rcvProd)
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
         * @return A new instance of fragment FragmentProductos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProductos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}