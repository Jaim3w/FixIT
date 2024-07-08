package PtcFixit.fix_it

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class carros_admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carros_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupNavClickListeners()
    }

    private fun setupNavClickListeners() {
        val navView = findViewById<View>(R.id.include_nav)

        val imageView6 = navView.findViewById<ImageView>(R.id.imageView6)
        val imageView8 = navView.findViewById<ImageView>(R.id.imageView8)
        val imageView7 = navView.findViewById<ImageView>(R.id.imageView7)
        val imageView9 = navView.findViewById<ImageView>(R.id.imageView9)
        val imageView10 = navView.findViewById<ImageView>(R.id.imageView10)

        val clickListener = View.OnClickListener { v ->
            val intent = when (v.id) {
                R.id.imageView6 -> Intent(this, MenuAdmin::class.java)
                R.id.imageView8 -> Intent(this, repuestos_admin::class.java)
                R.id.imageView7 -> Intent(this, proveedores_admin::class.java)
                R.id.imageView9 -> Intent(this, carros_admin::class.java)
                R.id.imageView10 -> Intent(this, citas::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }

        imageView6.setOnClickListener(clickListener)
        imageView8.setOnClickListener(clickListener)
        imageView7.setOnClickListener(clickListener)
        imageView9.setOnClickListener(clickListener)
        imageView10.setOnClickListener(clickListener)
    }
}