package CarrosHelpers

import PtcFixit.fix_it.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderCarros(view :View) : RecyclerView.ViewHolder(view) {
    val imgCarros = view.findViewById<ImageView>(R.id.imgImagenCardCarros)
    val txtfechaRegistroCard = view.findViewById<TextView>(R.id.txtRegistroCarrosCard)
    val txtColorCarroCard = view.findViewById<TextView>(R.id.txtColorCarroCard)
    val txtanioCarro = view.findViewById<TextView>(R.id.txtanioCarro)
    val txtServicioCardCarros = view.findViewById<TextView>(R.id.txtServicioCardCarros)
    val imgEliminarCarros = view.findViewById<ImageView>(R.id.imgEliminarCarros)
    val imgActualizarCarro = view.findViewById<ImageView>(R.id.imgActualizarCarros)
}