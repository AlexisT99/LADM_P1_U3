package mx.tecnm.tepic.ladm_p1_u3.Modelos

import mx.tecnm.tepic.ladm_p1_u3.R

data class Conductores(val nombre:String,val id:Int,val direccion:String) {
    val img = R.drawable.imagen
    var Nolicencia = ""
    var vence = ""
}