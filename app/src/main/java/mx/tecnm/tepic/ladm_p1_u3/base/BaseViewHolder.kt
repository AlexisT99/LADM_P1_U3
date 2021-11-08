package mx.tecnm.tepic.ladm_p1_u3.base

import android.view.View
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Camiones
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Conductores

abstract class BaseViewHolder<T>(itemView: View):RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Conductores, position:Int)
}