package PtcFixit.fix_it

import CarrosHelpers.AdaptadorCarros
import CarrosHelpers.tbCarros
import Modelo.ClaseConexion
import android.os.Bundle
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentCarros : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    fun obtenerDatosCarro(): List<tbCarros> {
        val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery( "SELECT \n" +
                    "    Carro.Placa_carro AS Placa_del_Carro, \n" +
                    "    Cliente.Nombre AS Nombre_del_Cliente, \n" +
                    "    Cliente.Apellido AS Apellido_del_Cliente, \n" +
                    "    Modelo.Nombre AS Modelo_del_Carro, \n" +
                    "    Carro.Color AS Color, \n" +
                    "    Carro.Año AS Año, \n" +
                    "    Carro.ImagenCarro AS ImagenCarro,\n" +
                    "    Carro.Descripcion AS Descripcion\n" +
                    "FROM Carro \n" +
                    "INNER JOIN Cliente ON Carro.Dui_cliente = Cliente.Dui_cliente \n" +
                    "INNER JOIN Modelo ON Carro.UUID_modelo = Modelo.UUID_modelo"
            )!!
        val listadoCarro = mutableListOf<tbCarros>()

            while (resultSet.next()) {
                val placaCarro = resultSet.getString("Placa_del_Carro")
                val duiCliente = resultSet.getString("Dui_cliente")
                val uuid_modelo = resultSet.getString("UUID_modelo")
                val colorCarro = resultSet.getString("Color")
                val anioCarro = resultSet.getString("Año")
                val imagencarro = resultSet.getString("ImagenCarro")
                val fechaRegistro = resultSet.getString("FechaRegistro")
                val descripcionCarro = resultSet.getString("Descripcion")
                val carro = tbCarros(placaCarro, duiCliente, uuid_modelo, colorCarro, anioCarro, imagencarro, fechaRegistro, descripcionCarro)
                listadoCarro.add(carro)
            }
return listadoCarro
        }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_carros, container, false)
        val rcvCarro = root.findViewById<RecyclerView>(R.id.rcvListadoCarros)
        rcvCarro.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val carrodb = obtenerDatosCarro()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorCarros(carrodb)
                rcvCarro.adapter = adapter
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
         * @return A new instance of fragment FragmentCarros.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCarros().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}