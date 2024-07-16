package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.dataClassItem
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.SQLException
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_AgregarRep.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_AgregarRep : Fragment() {

    fun getItems(): List<dataClassItem> {
        val conexion = ClaseConexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM CategoriaItem")!!

        val listadoItems = mutableListOf<dataClassItem>()

        while (resultSet.next()) {
            val uuid_item = resultSet.getString("UUID_item")
            val nombre = resultSet.getString("Nombre")

            val itemCompleto = dataClassItem(uuid_item, nombre)

            listadoItems.add(itemCompleto)
        }

        return listadoItems
    }

    val codigo_opcion_galeria = 102
    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0
    val STORAGE_REQUEST_CODE = 1

    lateinit var imageView: ImageView
    private var miPath: String? = null
    lateinit var txtNombreRep: EditText


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
        val root = inflater.inflate(R.layout.fragment__agregar_rep, container, false)

        txtNombreRep = root.findViewById<EditText>(R.id.txtNombreRep)
        imageView = root.findViewById<ImageView>(R.id.imgRep)
        val txtPrecio = root.findViewById<EditText>(R.id.editTextNumberDecimal2)
        val txtCategoria = root.findViewById<Spinner>(R.id.spinner)
        val btnCrear = root.findViewById<Button>(R.id.btnCrear)
        val btnSeleccionarGaleria = root.findViewById<Button>(R.id.btnSelect)
        val btnTomarFoto = root.findViewById<Button>(R.id.btnCamara)

        GlobalScope.launch(Dispatchers.IO){
            val listadoItems = getItems()
            val nombreItems = listadoItems.map { it.Nombre }

            withContext(Dispatchers.Main) {
                val itemAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreItems)
                txtCategoria.adapter = itemAdapter
            }
        }

        btnSeleccionarGaleria.setOnClickListener {
            checkStoragePermission()
        }
        btnTomarFoto.setOnClickListener {
            checkCameraPermission()
        }

        btnCrear.setOnClickListener {
            if (txtNombreRep.text.toString().isEmpty() || txtPrecio.text.toString()
                    .isEmpty() || txtCategoria.selectedItem == null
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
                        val item = getItems()

                        val addRep =
                            objConexion?.prepareStatement("INSERT INTO ProductoRepuesto (UUID_productoRepuesto,  UUID_item, Nombre, Precio) VALUES (?,?,?,?)")!!
                        addRep.setString(1, UUID.randomUUID().toString())
                        addRep.setString(2, item[txtCategoria.selectedItemPosition].UUID_item)
                        addRep.setString(3, txtNombreRep.text.toString())
                        addRep.setString(4, txtPrecio.text.toString())
                        addRep.executeUpdate()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Repuesto agregado exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            txtNombreRep.text.clear()
                            txtPrecio.text.clear()
                            imageView.setImageResource(0)
                            imageView.tag = null
                        }
                    } catch (e: SQLException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Error al agregar el repuesto: ${e.message}",
                                Toast.LENGTH_SHORT
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
        val imageRef = storageRef.child("images/${txtNombreRep.text}.jpg")
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