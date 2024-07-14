package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.RCVproveedor
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import reccyclerviewherlperProveedores.Adaptador
import java.sql.SQLException

class proveedores_admin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_proveedores_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregarNuevoProveedor = findViewById<Button>(R.id.btnAgregarProveedor)
        val rcvProveedores = findViewById<RecyclerView>(R.id.rcvProveedores)

        btnAgregarNuevoProveedor.setOnClickListener {
            val intent = Intent(this, crear_proveedores::class.java)
            startActivity(intent)
        }

        rcvProveedores.layoutManager = LinearLayoutManager(this)

        fun obtenerProveedores(): List<RCVproveedor> {
            val objConexion = ClaseConexion().cadenaConexion()
            if (objConexion == null) {
                Log.e("obtenerProveedores", "Fallo al obtener la conexión")
                return emptyList()
            }

            val statement = objConexion.createStatement()
            if (statement == null) {
                Log.e("obtenerProveedores", "Fallo al crear el statement")
                objConexion.close()
                return emptyList()
            }

            val resultSet = statement.executeQuery("select * from Proveedor")
            val listaProveedores = mutableListOf<RCVproveedor>()

            try {
                while (resultSet.next()) {
                    val duiProv = resultSet.getString("Dui_proveedor")
                    val nombreProv = resultSet.getString("Nombre")
                    val apellidoProv = resultSet.getString("Apellido")
                    val telefonoProv = resultSet.getString("Telefono")
                    val correoProv = resultSet.getString("Correo_Proveedor")
                    val direccionProv = resultSet.getString("Direccion")
                    val valoresCard = RCVproveedor(duiProv, nombreProv, apellidoProv, telefonoProv, correoProv, direccionProv)

                    listaProveedores.add(valoresCard)
                }
            } catch (e: SQLException) {
                Log.e("obtenerProveedores", "Error al obtener proveedores: ${e.message}")
            } finally {
                resultSet.close()
                statement.close()
                objConexion.close()
            }

            return listaProveedores
        }

        CoroutineScope(Dispatchers.IO).launch {
            val proveedoresDB = obtenerProveedores()
            withContext(Dispatchers.Main) {
                val adapterProv = Adaptador(proveedoresDB)
                rcvProveedores.adapter = adapterProv

                // Actualiza el RecyclerView con los mismos datos obtenidos anteriormente
                (rcvProveedores.adapter as? Adaptador)?.actualizarRecyclerView(proveedoresDB)
            }
        }



        //---------------------------NAV-------------------------------------------------------------------------

        setupNavClickListeners()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@proveedores_admin, Menu1Activity::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@proveedores_admin,
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


