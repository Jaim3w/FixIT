package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.RCVproveedor
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oracle.net.aso.e

class  crear_proveedores : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_proveedores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtDuiProv = findViewById<EditText>(R.id.txtDuiProveedor)
        val txtNombreProv = findViewById<EditText>(R.id.txtNombreProveedor)
        val txtApellidosProv = findViewById<EditText>(R.id.txtApellidosProveedor)
        val txtTelefonoProv = findViewById<EditText>(R.id.txtTelefonoProveedor)
        val txtCorreoProv = findViewById<EditText>(R.id.txtCorreoElectronicoProveedor)
        val txtDireccionProv = findViewById<EditText>(R.id.txtDirecccionProveedor)
        val btnIngresarProv = findViewById<Button>(R.id.btnIngresarProveedor)

        btnIngresarProv.setOnClickListener {
            if (txtDuiProv.text.toString().isEmpty() || txtNombreProv.text.toString().isEmpty()
                || txtApellidosProv.text.toString().isEmpty() || txtTelefonoProv.text.toString().isEmpty()
                || txtCorreoProv.text.toString().isEmpty() || txtDireccionProv.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion() // Ajusta según tu implementación

                        // Verificar si la conexión es válida
                        if (objConexion != null) {
                            val addNombreProv = objConexion.prepareStatement(
                                "INSERT INTO Proveedor (Dui_proveedor, Nombre, Apellido, Telefono, Correo_Electronico, Direccion) VALUES (?, ?, ?, ?, ?, ?)"
                            )
                            addNombreProv.setString(1, txtDuiProv.text.toString())
                            addNombreProv.setString(2, txtNombreProv.text.toString())
                            addNombreProv.setString(3, txtApellidosProv.text.toString())
                            addNombreProv.setString(4, txtTelefonoProv.text.toString())
                            addNombreProv.setString(5, txtCorreoProv.text.toString())
                            addNombreProv.setString(6, txtDireccionProv.text.toString())

                            addNombreProv.executeUpdate()

                            // Confirmar la transacción (commit)
                            objConexion.commit()

                            // Cerrar la conexión después de usarla
                            objConexion.close()

                            // Mostrar mensaje en el hilo principal
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    applicationContext,
                                    "Proveedor agregado exitosamente.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    applicationContext,
                                    "Error de conexión a la base de datos.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "Error al agregar el proveedor: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        //--------------------------------NAV-----------------------------------------------------------------------------

        setupNavClickListeners()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@crear_proveedores, Menu1Activity::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@crear_proveedores,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                startActivity(intent, options.toBundle())
                finish()
            }
        })
    }

    private fun setupNavClickListeners() {
        val navView = findViewById<View>(R.id.include_nav)

        val imgHomenav = navView.findViewById<ImageView>(R.id.imgHomenav)
        val imgRepuestosnav = navView.findViewById<ImageView>(R.id.imgRepuestosnav)
        val imgProveedoresnav = navView.findViewById<ImageView>(R.id.imgProveedoresnav)
        val imgCarrosnav = navView.findViewById<ImageView>(R.id.imgCarrosnav)
        val imgCitasnav = navView.findViewById<ImageView>(R.id.imgCitasnav)

        val clickListener = View.OnClickListener { v ->
            val currentActivity = this::class.java
            val targetActivity = when (v.id) {
                R.id.imgHomenav -> Menu1Activity::class.java
                R.id.imgRepuestosnav -> repuestos_admin::class.java
                R.id.imgProveedoresnav -> proveedores_admin::class.java
                R.id.imgCarrosnav -> carros_admin::class.java
                R.id.imgCitasnav -> citas::class.java
                else -> null
            }
            if (targetActivity != null && currentActivity != targetActivity) {
                val intent = Intent(this, targetActivity)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                startActivity(intent, options.toBundle())
                finish()
            }
        }

        imgHomenav.setOnClickListener(clickListener)
        imgRepuestosnav.setOnClickListener(clickListener)
        imgProveedoresnav.setOnClickListener(clickListener)
        imgCarrosnav.setOnClickListener(clickListener)
        imgCitasnav.setOnClickListener(clickListener)
    }
}




