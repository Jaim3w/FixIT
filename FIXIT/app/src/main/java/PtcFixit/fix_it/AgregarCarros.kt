package PtcFixit.fix_it

import android.app.Activity
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
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregarCarros.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgregarCarros : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val codigo_opcion_galeria = 102
    val codigo_opcion_tomar_foto = 103
    val CAMERA_REQUEST_CODE = 0
    val STORAGE_REQUEST_CODE = 1

    lateinit var imageView: ImageView
    lateinit var miPath: String
    lateinit var txtnumPlaca: EditText


    val uuid = UUID.randomUUID().toString()

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
        return inflater.inflate(R.layout.fragment_agregar_carros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar las vistas
        imageView = view.findViewById(R.id.imgCarro)
        val btnGaleria = view.findViewById<Button>(R.id.btnSubirImagen)
        val btnFoto = view.findViewById<Button>(R.id.btnTomarFoto)
        txtnumPlaca = view.findViewById(R.id.txtNumplaca)
        val spduiCliente = view.findViewById<Spinner>(R.id.spDuiCliente)
        val spModelo = view.findViewById<Spinner>(R.id.spModelo)
        val txtColorVehiculo = view.findViewById<TextView>(R.id.txtColorVehiculo)
       val txtAniovehiculo = view.findViewById<TextView>(R.id.txtanioVehiculo)
      val  txtFechaDeIngreso = view.findViewById<TextView>(R.id.txtFechaIngreso)
        val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcionVehiculo)

        btnGaleria.setOnClickListener {
            checkStoragePermission()
        }
        btnFoto.setOnClickListener {
            checkCameraPermission()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //El permiso no está aceptado, entonces se lo pedimos
            pedirPermisoCamara()
        } else {
            //El permiso ya está aceptado
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, codigo_opcion_tomar_foto)
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //El permiso no está aceptado, entonces se lo pedimos
            pedirPermisoAlmacenamiento()
        } else {
            //El permiso ya está aceptado
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria)
        }
    }

    private fun pedirPermisoCamara() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.CAMERA)) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //El permiso está aceptado, entonces Abrimos la camara:
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, codigo_opcion_tomar_foto)
                } else {
                    //El usuario ha rechazado el permiso, podemos desactivar la funcionalidad o mostrar una alerta/Toast.
                    Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                }
            }
            STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //El permiso está aceptado, entonces Abrimos la galeria
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria)
                } else {
                    //El usuario ha rechazado el permiso, podemos desactivar la funcionalidad o mostrar una alerta/Toast.
                    Toast.makeText(requireContext(), "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Este else lo dejamos por si sale un permiso que no teníamos controlado.
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
    private fun guardarCarros(){

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AgregarCarros.
         */
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