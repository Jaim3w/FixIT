package CarrosHelpers

import PtcFixit.fix_it.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderCarros(view :View) : RecyclerView.ViewHolder(view) {
    val imgCarros = view.findViewById<ImageView>(R.id.imgImagenCardCarros)
    val txtClienteCarrosCard = view.findViewById<TextView>(R.id.txtClienteCarroCard)
    val txtPlacaCarrosCard = view.findViewById<TextView>(R.id.txtPlacaCarroCard)
    val txtFechaRegistroCarrosCard = view.findViewById<TextView>(R.id.txtfechaRegistroCard)
    val txtDescripcionCarrosCard = view.findViewById<TextView>(R.id.txtDescripcionCarroCard)
    val txtColorCarrosCard = view.findViewById<TextView>(R.id.txtColorCarroCard)
    val imgEliminarCarros = view.findViewById<ImageView>(R.id.imgEliminarCarros)
    val imgActualizarCarro = view.findViewById<ImageView>(R.id.imgActualizarCarros)
}
