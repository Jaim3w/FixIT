
package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.dataClassClientes
import Modelo.dataClassModelo
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AgregarCarros : Fragment() {
    private var param1: String? = null
    private var param2: String? = null





    fun showYearPickerDialog(textView: EditText) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, _, _ ->
                textView.setText(selectedYear.toString())
            },
            year,
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        // Ocultar el selector de mes y día
        val dayPickerId = resources.getIdentifier("android:id/day", null, null)
        val monthPickerId = resources.getIdentifier("android:id/month", null, null)

        if (dayPickerId != 0 && monthPickerId != 0) {
            val dayPicker = datePickerDialog.datePicker.findViewById<View>(dayPickerId)
            val monthPicker = datePickerDialog.datePicker.findViewById<View>(monthPickerId)

            dayPicker?.visibility = View.GONE
            monthPicker?.visibility = View.GONE
        }

        datePickerDialog.show()
    }



    fun getClientes(): List<dataClassClientes>{
        val conexion = ClaseConexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Cliente")!!

        val listadoClientes = mutableListOf<dataClassClientes>()

        while (resultSet.next()) {
            val dui = resultSet.getString("Dui_cliente")
            val nombre = resultSet.getString("Nombre")
            val apellido = resultSet.getString("Apellido")
            val usuario = resultSet.getString("Usuario")
            val contra = resultSet.getString("Contrasena")
            val correo = resultSet.getString("Correo_Electronico")
            val telefono = resultSet.getString("Telefono")

            val clienteCompleto = dataClassClientes(dui,nombre,apellido,usuario,contra,correo,telefono)

            listadoClientes.add(clienteCompleto)

        }
        return listadoClientes
    }
    fun getModelo(): List<dataClassModelo> {
        val conexion = ClaseConexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM modelo")!!

        val listadoModelo = mutableListOf<dataClassModelo>()

        while (resultSet.next()) {
            val uuid_modelo = resultSet.getString("UUID_modelo")
            val uuid_Marca = resultSet.getString("UUID_marca")
            val nombreModelo = resultSet.getString("Nombre")

            val modeloCompleto = dataClassModelo(uuid_Marca, uuid_modelo, nombreModelo)
            listadoModelo.add(modeloCompleto)
        }
        return listadoModelo
    }

    val codigo_opcion_galeria = 102
    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0
    val STORAGE_REQUEST_CODE = 1

    lateinit var imageView: ImageView
    private var miPath: String? = null
    lateinit var txtnumPlaca: EditText


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
        val root = inflater.inflate(R.layout.fragment_agregar_carros, container, false)

        // Inicializar las vistas
        imageView = root.findViewById(R.id.imgCarro)
        val btnGaleria = root.findViewById<Button>(R.id.btnSubirImagen)
        val btnFoto = root.findViewById<Button>(R.id.btnTomarFoto)
        val btnGuardarVehiculo = root.findViewById<Button>(R.id.btnGuardarVehiculo)
        txtnumPlaca = root.findViewById(R.id.txtNumplaca)
        val txtduiCliente = root.findViewById<Spinner>(R.id.txtDuiClienteCarro)
        val txtModelo = root.findViewById<Spinner>(R.id.txtModelo)
        val txtColorVehiculo = root.findViewById<EditText>(R.id.txtColorVehiculo)
        val txtAniovehiculo = root.findViewById<EditText>(R.id.txtanioVehiculo)
        val  txtFechaDeIngreso = root.findViewById<EditText>(R.id.txtFechaIngreso)
        val  txtDescripcion = root.findViewById<EditText>(R.id.txtDescripcionVehiculo)


        GlobalScope.launch(Dispatchers.IO) {
            val listadoClientes = getClientes()
            val listadoModelo = getModelo()
            val nombreCliente = listadoClientes.map { it.Nombre }
            val nombreModelo = listadoModelo.map { it.NombreModelo }


            withContext(Dispatchers.Main) {
                val clienteAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreCliente)
                txtduiCliente.adapter = clienteAdapter

                val modeloAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreModelo)
                txtModelo.adapter = modeloAdapter
            }
        }


        btnGaleria.setOnClickListener {
            checkStoragePermission()
        }
        btnFoto.setOnClickListener {
            checkCameraPermission()
        }
        txtAniovehiculo.inputType = InputType.TYPE_NULL
        txtAniovehiculo.setOnClickListener {
            showYearPickerDialog(txtAniovehiculo)
        }

        txtFechaDeIngreso.inputType = InputType.TYPE_NULL
        txtFechaDeIngreso.setOnClickListener {
                    val calendario = Calendar.getInstance()
                    val anio = calendario.get(Calendar.YEAR)
                    val mes = calendario.get(Calendar.MONTH)
                    val dia = calendario.get(Calendar.DAY_OF_MONTH)

                    val datePickerDialog = DatePickerDialog(
                        requireContext(),
                        { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                            val fechaSeleccionada = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                            txtFechaDeIngreso.setText(fechaSeleccionada)
                        },
                        anio, mes, dia
                    )

                    datePickerDialog.datePicker.maxDate = calendario.timeInMillis

                    datePickerDialog.show()
                }

        btnGuardarVehiculo.setOnClickListener {
            val placas = txtnumPlaca.text.toString().trim()

            if (txtFechaDeIngreso.text.toString().isEmpty() || txtAniovehiculo.text.toString().isEmpty()
                || txtDescripcion.text.toString().isEmpty() || txtColorVehiculo.text.toString().isEmpty() || txtnumPlaca.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion()
                        val cliente = getClientes()
                        val modelo = getModelo()
                        println("esto es lo que estoy intentando ingresar para el modelo ${modelo[txtModelo.selectedItemPosition].UUID_modelo}")

                        val addCarro = objConexion?.prepareStatement("INSERT INTO Carro (Placa_carro, Dui_cliente, UUID_modelo, Color, Año, FechaRegistro, Descripcion) VALUES (?,?,?,?,?,?,?)")!!
                        addCarro.setString(1, placas)
                        addCarro.setString(2, cliente[txtduiCliente.selectedItemPosition].Dui_cliente)
                        addCarro.setString(3, modelo[txtModelo.selectedItemPosition].UUID_modelo)
                        addCarro.setString(4, txtColorVehiculo.text.toString())
                        addCarro.setString(5, txtAniovehiculo.text.toString())
                        addCarro.setString(6, txtFechaDeIngreso.text.toString())
                        addCarro.setString(7, txtDescripcion.text.toString())
                        addCarro.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Carro agregado exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            txtDescripcion.text.clear()
                            txtnumPlaca.text.clear()
                            txtAniovehiculo.text.clear()
                            txtColorVehiculo.text.clear()
                            txtFechaDeIngreso.text.clear()
                            imageView.setImageResource(0)
                            imageView.tag = null
                        }
                    } catch (e: SQLException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Error al agendar el carro: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        return root
    }




    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisoCamara()
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, codigo_opcion_tomar_foto)
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisoAlmacenamiento()
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria)
        }
    }

    private fun pedirPermisoCamara() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.CAMERA)) {
            // El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, codigo_opcion_tomar_foto)
                } else {
                    Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                }
            }
            STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria)
                } else {
                    Toast.makeText(requireContext(), "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                codigo_opcion_galeria -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                        subirimagenFirebase(imageBitmap) { url ->
                            miPath = url // Set miPath here
                            imageView.setImageURI(it)
                        }
                    }
                }
                codigo_opcion_tomar_foto -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        subirimagenFirebase(it) { url ->
                            miPath = url // Set miPath here
                            imageView.setImageBitmap(it)
                        }
                    }
                }
            }
        }
    }

    private fun subirimagenFirebase(bitmap: Bitmap, onSuccess: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${txtnumPlaca.text}.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnFailureListener {
            Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
    }





    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregarCarros().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}