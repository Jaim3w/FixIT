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
import kotlinx.coroutines.CoroutineName
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
 * Use the [FragmentCarros.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCarros : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carros, container, false)

        val root = inflater.inflate(R.layout.fragment_carros, container, false)
        val rcvlistaCarros = root.findViewById<RecyclerView>(R.id.rcvListadoCarros)

        rcvlistaCarros.layoutManager = LinearLayoutManager(requireContext())

        fun ObtenerCarros ():List<tbCarros>{
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resulSet = statement?.executeQuery("SELECT * FROM Carro ")!!

            val listaCarro = mutableListOf<tbCarros>()
            while (resulSet.next()){
                val placaCarro =resulSet.getString("Placa_carro")
                val duiCliente =resulSet.getString("Dui_cliente")
                val uuidModelo =resulSet.getString("UUID_modelo")
                val colorCarro = resulSet.getString("Color")
                val anioCarro = resulSet.getString("Año")
                val imagenCarro = resulSet.getString("ImagenCarro")
                val fechaRegistroCarro = resulSet.getString("FechaRegistro")
                val descripcionCarro = resulSet.getString("Descripcion")

                val valoresJuntos = tbCarros(placaCarro,duiCliente,uuidModelo,colorCarro,anioCarro,imagenCarro,fechaRegistroCarro,descripcionCarro)

                listaCarro.add(valoresJuntos)


            }
            return listaCarro

            CoroutineScope(Dispatchers.IO).launch {
                val CarroData = ObtenerCarros()
                withContext(Dispatchers.Main){
                    val adaptador = AdaptadorCarros(CarroData)
                    rcvlistaCarros.adapter= adaptador
                }
            }

        }

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