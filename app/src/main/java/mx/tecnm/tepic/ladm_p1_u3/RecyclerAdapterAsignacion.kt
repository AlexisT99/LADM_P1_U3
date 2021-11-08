package mx.tecnm.tepic.ladm_p1_u3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Asignacion
import mx.tecnm.tepic.ladm_p1_u3.base.BaseViewHolder3
import mx.tecnm.tepic.ladm_p1_u3.databinding.AsignacionRowBinding
import java.lang.IllegalArgumentException

class RecyclerAdapterAsignacion (private val context: Context, val listaAsinacion:List<Asignacion>, private val itemClickListener:onClickListener): RecyclerView.Adapter<BaseViewHolder3<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder3<*> {
        return AsignacionViewHolder(LayoutInflater.from(context).inflate(R.layout.asignacion_row,parent,false))
    }
    interface onClickListener{
        fun onItemLongClick(
            id: Int,
            nomCamion: String,
            nomConductor: String,
            itemView: View,
            position: Int
        ):Boolean
        fun onClick(id: Int, nombre: String, itemView: View, position: Int):Boolean
    }

    //Para cada informacion
    override fun onBindViewHolder(holder: BaseViewHolder3<*>, position: Int) {
        when(holder){
            is RecyclerAdapterAsignacion.AsignacionViewHolder -> holder.bind(listaAsinacion[position],position)
            else -> throw IllegalArgumentException("No se paso el viewholder")
        }
    }

    override fun getItemCount(): Int = listaAsinacion.size
    //como tratar la informacion
    inner class AsignacionViewHolder(itemView: View): BaseViewHolder3<Asignacion>(itemView){
        val b = AsignacionRowBinding.bind(itemView)
        override fun bind(item: Asignacion, position: Int) {
            itemView.setOnLongClickListener { itemClickListener.onItemLongClick(item.idConductor,item.nomCamion,item.nomConductor,itemView,position)
            }
            b.imagen.setBackgroundResource(item.img)
            b.txtIdCamion.text = item.idConductor.toString()
            b.txtIdconductor.text = item.idCamion.toString()
            b.txtRowCamion.text = item.nomCamion
            b.txtRowConductor.text = item.nomConductor
        }
    }
}