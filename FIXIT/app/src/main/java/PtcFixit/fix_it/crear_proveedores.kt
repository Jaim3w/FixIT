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
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class crear_proveedores : AppCompatActivity() {
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
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()
                val addNombreProv = objConexion?.prepareStatement(
                    "INSERT INTO Proveedores (Dui_proveedor, Nombre, Apellido, Telefono, Correo_Electronico, Direccion) VALUES (?, ?, ?, ?, ?, ?)"
                )!!
                addNombreProv.setString(1, txtDuiProv.text.toString())
                addNombreProv.setString(2, txtNombreProv.text.toString())
                addNombreProv.setString(3, txtApellidosProv.text.toString())
                addNombreProv.setString(4, txtTelefonoProv.text.toString())
                addNombreProv.setString(5, txtCorreoProv.text.toString())
                addNombreProv.setString(6, txtDireccionProv.text.toString())
                addNombreProv.executeUpdate()

                // Return to previous activity with result
                withContext(Dispatchers.Main) {
                    setResult(Activity.RESULT_OK)
                    finish()
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