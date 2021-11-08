package mx.tecnm.tepic.ladm_p1_u3.ModelosBD

import android.content.ContentValues
import android.content.Context
import android.util.Log
import mx.tecnm.tepic.ladm_p1_u3.BaseDatos

class USUARIO(p: Context) {

    var usuario = ""
    var contraseña = ""
    val p = p

    fun insertar(): Boolean {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val dato = ContentValues()
        dato.put("USUARIO", usuario)
        dato.put("CONTRASENA",contraseña )

        val resultado = tabla.insert("USUARIO", null, dato)
        tabla.close()
        if (resultado == -1L) return false
        return true
    }

    fun consulta(usu:String): Boolean {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).readableDatabase
        Log.d("tag",usu)
        val cursor = tabla.query("USUARIO", arrayOf("*"), "USUARIO=?", arrayOf(usu), null, null, null)
        if (cursor.moveToFirst()) {
            tabla.close()
            return true
        }
        tabla.close()
        return false
    }

    fun consultaUsu(usu:String,con:String): Boolean {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).readableDatabase
        val cursor = tabla.query("USUARIO", arrayOf("*"), "USUARIO=? AND CONTRASENA=?", arrayOf(usu,con), null, null, null)
        if (cursor.moveToFirst()) {
            tabla.close()
            return true
        }
        tabla.close()
        return false
    }
}