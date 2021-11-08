package mx.tecnm.tepic.ladm_p1_u3.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Asignacion

abstract class BaseViewHolder3<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Asignacion, position:Int)
}