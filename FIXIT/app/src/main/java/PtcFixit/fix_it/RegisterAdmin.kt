package PtcFixit.fix_it

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

class RegisterAdmin : AppCompatActivity() {

    private lateinit var uuidRolAdministrador: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_admin_taller)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val correoAdmin = findViewById<EditText>(R.id.txtCorreoRegister)
        val contrasenaAdmin = findViewById<EditText>(R.id.txtContrasenaAdmin)
        val registrar = findViewById<Button>(R.id.btnSiguienteRegister)
        val verContrasena = findViewById<ImageView>(R.id.imgVerContraRegister)

        fun hashSHA256(contrasenaEscrita: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(contrasenaEscrita.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                uuidRolAdministrador = obtenerRol("Administrador")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterAdmin, "Error al obtener el rol: ${e.message}", Toast.LENGTH_LONG).show()
                }
                return@launch
            }

            withContext(Dispatchers.Main) {
                registrar.setOnClickListener {
                    val email = correoAdmin.text.toString().trim()
                    val password = contrasenaAdmin.text.toString().trim()

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this@RegisterAdmin, "Correo y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val pantallamenu = Intent(this@RegisterAdmin, Menu1Activity::class.java)

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val objConexion = ClaseConexion().cadenaConexion()
                            val contrasenaEncriptada = hashSHA256(password)

                            val crearUsuario = objConexion?.prepareStatement("INSERT INTO Usuario(UUID_usuario, UUID_rol, CorreoElectronico, Contrasena) VALUES(?, ?, ?, ?)")!!
                            crearUsuario.setString(1, UUID.randomUUID().toString())
                            crearUsuario.setString(2, uuidRolAdministrador)
                            crearUsuario.setString(3, email)
                            crearUsuario.setString(4, contrasenaEncriptada)

                            crearUsuario.executeUpdate()

                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@RegisterAdmin, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                                correoAdmin.setText("")
                                contrasenaAdmin.setText("")
                                startActivity(pantallamenu)
                                finish()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@RegisterAdmin, "Error al crear el usuario: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }

        verContrasena.setOnClickListener {
            if (contrasenaAdmin.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                contrasenaAdmin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                contrasenaAdmin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private suspend fun obtenerRol(nombreRol: String): String {
        return withContext(Dispatchers.IO) {
            val objconexion = ClaseConexion().cadenaConexion()
            val statement = objconexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT UUID_rol FROM Rol WHERE Nombre = '$nombreRol'")!!

            var uuidRol: String? = null
            if (resultSet.next()) {
                uuidRol = resultSet.getString("UUID_rol")
            }
            uuidRol ?: throw IllegalStateException("No se encontró el UUID para el rol '$nombreRol'")
        }
    }
}
