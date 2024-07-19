package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.dataClassRoles
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID


class RegistrarseAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCorreo = findViewById<EditText>(R.id.txtCorreoRegister)
        val txtContrasenaRegister = findViewById<EditText>(R.id.txtContrasenaAdmin)
        val imgVer = findViewById<ImageView>(R.id.imgVerContraRegister)
        val spinner = findViewById<Spinner>(R.id.spRoles)
        val btnRegistrar = findViewById<Button>(R.id.btnSiguienteRegister)

        fun getRol(): List<dataClassRoles> {
            val conexion = ClaseConexion().cadenaConexion()
            val statement = conexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Rol")!!

            val listaRol = mutableListOf<dataClassRoles>()

            while (resultSet.next()) {
                val uuidRol = resultSet.getString("UUID_rol")
                val nombreRol = resultSet.getString("Nombre")

                val rol = dataClassRoles(uuidRol, nombreRol)
                listaRol.add(rol)
            }
            return listaRol
        }

        GlobalScope.launch(Dispatchers.IO) {
            val listaRol = getRol()
            val nombreRol = listaRol.map { it.Nombre }

            withContext(Dispatchers.Main) {
                val rolAdapter = ArrayAdapter(this@RegistrarseAdmin, android.R.layout.simple_spinner_dropdown_item, nombreRol)
                spinner.adapter = rolAdapter
            }
        }

        fun hashSHA256(contraseniaEncriptada: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(contraseniaEncriptada.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        btnRegistrar.setOnClickListener {
            val intentnext = Intent(this, MainActivity::class.java)
            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()



                val contraseniaEncriptada = hashSHA256(txtContrasenaRegister.text.toString())
                val rol = getRol()

                val crearUsuario = objConexion?.prepareStatement("INSERT INTO Usuario(UUID_usuario,UUID_rol,CorreoElectronico,Contrasena) values(?,?,?,?)")!!
                crearUsuario.setString(1, UUID.randomUUID().toString())
                crearUsuario.setString(2, rol[spinner.selectedItemPosition].UUID_rol)
                crearUsuario.setString(3, txtCorreo.text.toString())
                crearUsuario.setString(4, contraseniaEncriptada)

                try {
                    crearUsuario.executeQuery()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegistrarseAdmin, "Usuario creado", Toast.LENGTH_SHORT).show()
                        txtCorreo.setText("")
                        txtContrasenaRegister.setText("")
                        val intent = Intent(this@RegistrarseAdmin, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegistrarseAdmin, "Error al crear usuario: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        imgVer.setOnClickListener{
            if(txtContrasenaRegister.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                txtContrasenaRegister.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else{
                txtContrasenaRegister.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }
}
