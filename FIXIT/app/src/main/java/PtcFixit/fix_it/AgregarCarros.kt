package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.tbCarros
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AgregarCarros : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    val codigo_opcion_galeria = 102
    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0
    val STORAGE_REQUEST_CODE = 1

    lateinit var imageView: ImageView
    lateinit var miPath: String
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
        val spduiCliente = root.findViewById<Spinner>(R.id.spDuiCliente)
        val spModelo = root.findViewById<Spinner>(R.id.spModelo)
        val txtColorVehiculo = root.findViewById<EditText>(R.id.txtColorVehiculo)
        val txtAniovehiculo = root.findViewById<EditText>(R.id.txtanioVehiculo)
       val  txtFechaDeIngreso = root.findViewById<EditText>(R.id.txtFechaIngreso)
       val  txtDescripcion = root.findViewById<EditText>(R.id.txtDescripcionVehiculo)

        btnGaleria.setOnClickListener {
            checkStoragePermission()
        }
        btnFoto.setOnClickListener {
            checkCameraPermission()
        }
        txtAniovehiculo.setOnClickListener {
            showYearPickerDialog(txtAniovehiculo)
        }
        btnGuardarVehiculo.setOnClickListener {

        }

        return root
    }
    fun showYearPickerDialog(textView: EditText) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, _, _ ->
                cal.set(Calendar.YEAR, selectedYear)
                val format = SimpleDateFormat("yyyy", Locale.getDefault())
                val formattedYear = format.format(cal.time)
                textView.setText(formattedYear)
            },
            year,
            0,
            1
        )

        // Ocultar el selector de mes y día
        datePickerDialog.datePicker.findViewById<View>(resources.getIdentifier("android:id/day", null, null)).visibility = View.GONE
        datePickerDialog.datePicker.findViewById<View>(resources.getIdentifier("android:id/month", null, null)).visibility = View.GONE

        datePickerDialog.show()
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
                            miPath = url
                            imageView.setImageURI(it)
                        }
                    }
                }
                codigo_opcion_tomar_foto -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        subirimagenFirebase(it) { url ->
                            miPath = url
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