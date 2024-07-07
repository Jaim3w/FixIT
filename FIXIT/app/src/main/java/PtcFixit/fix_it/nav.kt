package PtcFixit.fix_it

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class nav : AppCompatActivity() {

    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation

    private var lastClickedImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_header)
        setupAnimations()
        setupImageClickListeners()
    }

    private fun setupAnimations() {
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
    }

    private fun setupImageClickListeners() {
        val imageView6 = findViewById<ImageView>(R.id.imageView6)
        val imageView8 = findViewById<ImageView>(R.id.imageView8)
        val imageView7 = findViewById<ImageView>(R.id.imageView7)
        val imageView9 = findViewById<ImageView>(R.id.imageView9)
        val imageView10 = findViewById<ImageView>(R.id.imageView10)

        val clickListener = View.OnClickListener { v ->
            val imageView = v as ImageView
            if (imageView != lastClickedImageView) {
                lastClickedImageView?.setImageResource(getOriginalImageResource(lastClickedImageView))
                imageView.startAnimation(fadeOut)
                fadeOut.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        imageView.setImageResource(getYellowImageResource(imageView))
                        imageView.startAnimation(fadeIn)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
                lastClickedImageView = imageView
            }
        }

        imageView6.setOnClickListener(clickListener)
        imageView8.setOnClickListener(clickListener)
        imageView7.setOnClickListener(clickListener)
        imageView9.setOnClickListener(clickListener)
        imageView10.setOnClickListener(clickListener)
    }

    private fun getYellowImageResource(imageView: ImageView): Int {
        return when (imageView.id) {
            R.id.imageView6 -> R.drawable.inicio_yellow
            R.id.imageView8 -> R.drawable.repuestos_yellow
            R.id.imageView7 -> R.drawable.proveedores_yellow
            R.id.imageView9 -> R.drawable.carros_yellow
            R.id.imageView10 -> R.drawable.citas_yellow
            else -> throw IllegalArgumentException("Unknown ImageView ID")
        }
    }

    private fun getOriginalImageResource(imageView: ImageView?): Int {
        return when (imageView?.id) {
            R.id.imageView6 -> R.drawable.inicio
            R.id.imageView8 -> R.drawable.repuestosicon
            R.id.imageView7 -> R.drawable.proveedoresicon
            R.id.imageView9 -> R.drawable.carrosicon
            R.id.imageView10 -> R.drawable.citasicon
            else -> throw IllegalArgumentException("Unknown ImageView ID")
        }
    }
}