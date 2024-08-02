package PtcFixit.fix_it

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class inicioanimated : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicioanimated)

        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottieAnimationView)
        val imgSiguienteInicio: ImageView = findViewById(R.id.imgSiguienteInicio)

        // Configurar la animación de fade-in
        imgSiguienteInicio.alpha = 0f
        imgSiguienteInicio.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(imgSiguienteInicio, "alpha", 0f, 1f).apply {
            duration = 3000 // Duración en milisegundos
            start()
        }

        // Asignar un listener para detectar clics en el ImageView
        imgSiguienteInicio.setOnClickListener {
            // Navegar a otra actividad
            val intent = Intent(this, Inicio2::class.java)
            startActivity(intent)
        }
    }
}