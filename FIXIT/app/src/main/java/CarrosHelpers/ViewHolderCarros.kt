package CarrosHelpers

import PtcFixit.fix_it.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderCarros(view :View) : RecyclerView.ViewHolder(view) {
    val imgCarros = view.findViewById<ImageView>(R.id.imgImagenCardCarros)
    val txtnombreClienteCarros = view.findViewById<TextView>(R.id.txtnombreClienteCarros)
    val txtPlacaCardCarros = view.findViewById<TextView>(R.id.txtPlacaCardCarros)
    val txtModeloCardCarros = view.findViewById<TextView>(R.id.txtModeloCardCarros)
    val txtServicioCardCarros = view.findViewById<TextView>(R.id.txtServicioCardCarros)
}