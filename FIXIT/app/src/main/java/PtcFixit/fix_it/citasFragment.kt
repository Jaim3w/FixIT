package PtcFixit.fix_it

import Modelo.ClaseConexion
import PtcFixit.fix_it.CitasHelpers.tbCita
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [citasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class citasFragment : Fragment() {

    private lateinit var txtFecha: EditText

    private lateinit var txtClienteCita: Spinner

    private lateinit var txtEmpleadoCita: Spinner

    fun showTimePickerDialog(textView: EditText) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val formattedTime = format.format(cal.time)
                textView.setText(formattedTime)
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    fun getClientes(): List<String>{
        val cliente = mutableListOf<String>()
        val conexion = ClaseConexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT Nombre FROM Cliente")

        while (resultSet?.next() == true) {
            cliente.add(resultSet.getString("Nombre"))
        }

        resultSet?.close()
        statement?.close()
        conexion?.close()

        return cliente
    }

    fun getEmpleados(): List<String>{
        val empleado = mutableListOf<String>()
        val conexion = ClaseConexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT Nombre FROM Empleado")

        while (resultSet?.next() == true) {
            empleado.add(resultSet.getString("Nombre"))
        }

        resultSet?.close()
        statement?.close()
        conexion?.close()

        return empleado
    }

    fun obtenerDatos(): List<tbCita>{
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("select * from Citas")!!

        val listadoCitas = mutableListOf<tbCita>()

        while (resultSet.next()){
            val uuid = resultSet.getString("uuid")
            val cliente = resultSet.getString("cliente")
            val empleado = resultSet.getString("empleado")
            val fecha = resultSet.getString("fecha")
            val hora = resultSet.getString("hora")
            val descripcion = resultSet.getString("descripcion")
        }
        return listadoCitas
    }





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
        val root = inflater.inflate(R.layout.fragment_citas, container, false)

        val txtClienteCita = root.findViewById<Spinner>(R.id.txtClienteCita)
        val txtEmpleadoCita = root.findViewById<Spinner>(R.id.txtEmpleadoCita)
        val txtFecha = root.findViewById<EditText>(R.id.txtFecha)
        val txtHora = root.findViewById<EditText>(R.id.txtHora)
        val txtDescripcion = root.findViewById<EditText>(R.id.txtDescripcion)
        val btnCrearCita = root.findViewById<Button>(R.id.btnCrearCita)

        GlobalScope.launch(Dispatchers.IO) {
            val clientes = getClientes()
            val empleados = getEmpleados()

            withContext(Dispatchers.Main) {
                val clienteAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clientes)
                clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                txtClienteCita.adapter = clienteAdapter

                val empleadoAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, empleados)
                empleadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                txtEmpleadoCita.adapter = empleadoAdapter
            }
        }

        txtFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            // Calcular la fecha máxima (hace 18 años a partir de hoy)
            val fechaMaxima = Calendar.getInstance()
            fechaMaxima.set(anio, mes, dia + 1)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFecha.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )

            datePickerDialog.datePicker.maxDate = fechaMaxima.timeInMillis

            datePickerDialog.show()
        }

        txtHora.setOnClickListener {
            showTimePickerDialog(txtHora)
        }

        btnCrearCita.setOnClickListener {
            if (txtFecha.text.toString().isEmpty() || txtHora.text.toString().isEmpty()
                || txtDescripcion.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {

                    //1- Crear un objeto de la clase de conexion
                    val objConexion = ClaseConexion().cadenaConexion()

                    val addCita =
                        objConexion?.prepareStatement("INSERT INTO Cita (UUID_cita,Dui_cliente,Dui_empleado,Fecha_cita,Hora_cita,Descripcion) VALUES (!,!,!,!,!,!)")!!
                    addCita.setString(1, UUID.randomUUID().toString())
                    addCita.setString(2, txtClienteCita.selectedItem.toString())
                    addCita.setInt(3, txtEmpleadoCita.selectedItem.toString().toInt())
                    addCita.setInt(4, txtFecha.text.toString().toInt())
                    addCita.setInt(5, txtHora.text.toString().toInt())
                    addCita.setInt(6, txtDescripcion.text.toString().toInt())

                    addCita.executeUpdate()

                    val nuevasCitas = obtenerDatos()

                    withContext(Dispatchers.Main){
                        (rcvDatos.adapter as? Adaptador)?.actualizarRecyclerView(nuevosProductos)
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
             * @return A new instance of fragment citasFragment.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                citasFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }

    }


