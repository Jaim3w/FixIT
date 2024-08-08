package PtcFixit.fix_it

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Emergencia_usuario : AppCompatActivity() {

private val locationService :LocationService = LocationService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emergencia_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tvLocaation = findViewById<TextView>(R.id.tvLocation)
        val btnLocation = findViewById<Button>(R.id.btnLocation)

        btnLocation.setOnClickListener {
            lifecycleScope.launch {
                val resultado = locationService.getuserLocation(this@Emergencia_usuario)
                if (resultado!=null){
                    tvLocaation.text = "Latitud${resultado.latitude} y logituf ${resultado.longitude}"
                }
            }

        }
    }
}