package mx.tecnm.tepic.ladm_p1_u3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Camiones
import mx.tecnm.tepic.ladm_p1_u3.base.BaseViewHolder2
import mx.tecnm.tepic.ladm_p1_u3.databinding.CamionesRowBinding
import mx.tecnm.tepic.ladm_p1_u3.databinding.ConductoresRowBinding
import java.lang.IllegalArgumentException

class RecyclerAdapterCamiones(private val context: Context, val listaCamiones:List<Camiones>, private val itemClickListener:onClickListener): RecyclerView.Adapter<BaseViewHolder2<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder2<*> {
        return CamionesViewHolder(LayoutInflater.from(context).inflate(R.layout.camiones_row,parent,false))
    }
    interface onClickListener{
        fun onItemLongClick(id: Int, placa: String, itemView: View, position: Int):Boolean
        fun onClick(id: Int, nombre: String, itemView: View, position: Int):Boolean
    }

    //Para cada informacion
    override fun onBindViewHolder(holder: BaseViewHolder2<*>, position: Int) {
        when(holder){
            is RecyclerAdapterCamiones.CamionesViewHolder -> holder.bind(listaCamiones[position],position)
            else -> throw IllegalArgumentException("No se paso el viewholder")
        }
    }

    override fun getItemCount(): Int = listaCamiones.size
    //como tratar la informacion
    inner class CamionesViewHolder(itemView: View): BaseViewHolder2<Camiones>(itemView){
        val b= CamionesRowBinding.bind(itemView)
        override fun bind(item: Camiones, position: Int) {
            itemView.setOnLongClickListener { itemClickListener.onItemLongClick(item.id,item.placa,itemView,position)
            }
            b.txtRowPlaca.text = item.placa
            b.txtRowId.text = item.id.toString()
            b.imagen.setBackgroundResource(item.img)
            b.txtRowMarca.text = item.marca
            b.txtRowModelo.text = item.modelo
            b.txtRowAO.text = item.año.toString()
        }

    }
}