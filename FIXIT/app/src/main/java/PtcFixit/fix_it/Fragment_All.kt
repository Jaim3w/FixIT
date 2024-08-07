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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment_All : Fragment() {

    fun obtenerDatos(): List<tbRepuesto> {
        val listadoRepuestos = mutableListOf<tbRepuesto>()
        var objConexion: java.sql.Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            objConexion = ClaseConexion().cadenaConexion()
            if (objConexion != null) {
                statement = objConexion.createStatement()
                resultSet = statement.executeQuery("SELECT ProductoRepuesto.UUID_productoRepuesto, ProductoRepuesto.Nombre, ProductoRepuesto.ImagenProductoRepuesto, CategoriaItem.Nombre AS CategoriaNombre, ProductoRepuesto.Precio FROM ProductoRepuesto INNER JOIN CategoriaItem ON ProductoRepuesto.UUID_item = CategoriaItem.UUID_item ORDER BY ProductoRepuesto.Nombre")

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
                Log.e("Fragment_All", "objConexion es null")
            }
        } catch (e: SQLException) {
            Log.e("Fragment_All", "Error SQL al obtener datos de repuestos", e)
        } catch (e: Exception) {
            Log.e("Fragment_All", "Error inesperado al obtener datos de repuestos", e)
        } finally {
            resultSet?.close()
            statement?.close()
            objConexion?.close()
        }

        return listadoRepuestos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment__all, container, false)
        val rcvAll = root.findViewById<RecyclerView>(R.id.rcvAll)
        rcvAll.layoutManager = LinearLayoutManager(context)

        val adapter = AdaptadorRepuestos(emptyList())
        rcvAll.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val RepuestosBd = obtenerDatos()
            withContext(Dispatchers.Main) {
                adapter.actualizarListado(RepuestosBd)
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
         * @return A new instance of fragment Fragment_AgregarRep.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_AgregarRep().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
