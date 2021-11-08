package mx.tecnm.tepic.ladm_p1_u3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Camiones
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Conductores
import mx.tecnm.tepic.ladm_p1_u3.base.BaseViewHolder
import mx.tecnm.tepic.ladm_p1_u3.databinding.ConductoresRowBinding
import java.lang.IllegalArgumentException

class RecyclerAdapterConductores(private val context: Context, val listaConductores:List<Conductores>,private val itemClickListener:onClickListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return ConductoresViewHolder(LayoutInflater.from(context).inflate(R.layout.conductores_row,parent,false))
    }
    interface onClickListener{
        fun onItemLongClick(id: Int, nombre: String, itemView: View, position: Int):Boolean
        fun filtrado(txtBuscar: String)
    }

    //Para cada informacion
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is RecyclerAdapterConductores.ConductoresViewHolder -> holder.bind(listaConductores[position],position)
            else -> throw IllegalArgumentException("No se paso el viewholder")
        }
    }

    override fun getItemCount(): Int = listaConductores.size


    //como tratar la informacion
    inner class ConductoresViewHolder(itemView: View):BaseViewHolder<Conductores>(itemView){
        val b= ConductoresRowBinding.bind(itemView)
        override fun bind(item: Conductores, position: Int) {
            itemView.setOnLongClickListener { itemClickListener.onItemLongClick(item.id,item.nombre,itemView,position)
            }
            b.txtRowNom.text = item.nombre
            b.txtRowId.text = item.id.toString()
            b.imagen.setBackgroundResource(item.img)
            b.txtRowDir.text = item.direccion
        }
    }
}