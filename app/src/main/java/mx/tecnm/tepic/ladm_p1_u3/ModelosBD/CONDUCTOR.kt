package mx.tecnm.tepic.ladm_p1_u3.ModelosBD

import android.content.ContentValues
import android.content.Context
import android.util.Log
import mx.tecnm.tepic.ladm_p1_u3.BaseDatos
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Conductores
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CONDUCTOR(p: Context) {
    private val p = p
    var nombre = ""
    var domicilio = ""
    var noLicencia = ""
    var vence= "2010-01-01"

    fun insertar(): Boolean {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase

        val dato = ContentValues()
        dato.put("NOMBRE", nombre)
        dato.put("DOMICILIO",domicilio )
        dato.put("NOLICENCIA", noLicencia)
        dato.put("VENCE",vence)
        val resultado = tabla.insert("CONDUCTOR", null, dato)
        tabla.close()
        if (resultado == -1L) return false
        return true
    }

    fun consulta(): MutableList<Conductores> {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val listaConductores = mutableListOf<Conductores>()

        val cursor = tabla.query("CONDUCTOR", arrayOf("*"), null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val conductor = Conductores(cursor.getString(1),cursor.getInt(0),cursor.getString(2))
                conductor.Nolicencia = cursor.getString(3)
                conductor.vence = cursor.getString(4)
                listaConductores.add(conductor)
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaConductores
    }

    fun consultaSincamion(): MutableList<Conductores> {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val listaConductores = mutableListOf<Conductores>()

        val cursor = tabla.rawQuery("SELECT IDCONDUCTOR FROM CONDUCTOR EXCEPT SELECT IDCONDUCTOR FROM VEHICULO",null)
        
        if (cursor.moveToFirst()) {
            do {
                listaConductores.add(consultaI(cursor.getInt(0)))
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaConductores
    }

    fun consultaI(id:Int): Conductores {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        lateinit var conductor:Conductores

        val cursor = tabla.query("CONDUCTOR", arrayOf("*"), "IDCONDUCTOR=?", arrayOf(id.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            conductor = Conductores(cursor.getString(1), cursor.getInt(0), cursor.getString(2))
            conductor.Nolicencia = cursor.getString(3)
            val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = inputFormat.parse(cursor.getString(4))
            val outputText: String = outputFormat.format(date)
            conductor.vence = outputText
        }
        tabla.close()
        return conductor
    }

    fun consultaVencidas(): MutableList<Conductores> {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val listaConductores = mutableListOf<Conductores>()

        val cursor = tabla.query("CONDUCTOR", arrayOf("*"), "VENCE < date('now','-1 day')", null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val conductor = Conductores(cursor.getString(1),cursor.getInt(0),cursor.getString(2))
                conductor.Nolicencia = cursor.getString(3)
                conductor.vence = cursor.getString(4)
                listaConductores.add(conductor)
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaConductores
    }

    fun eliminar(id: Int): Boolean {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val resultado = tabla.delete("CONDUCTOR", "IDCONDUCTOR=?", arrayOf(id.toString()))
        if (resultado == 0) return false
        return true
    }

    fun actualizar(idActualizar:String): Boolean{
        val tabla = BaseDatos (p, "empresaTransporte", null,  1).writableDatabase
        val datos = ContentValues()
        datos.put("NOMBRE", nombre)
        datos.put("DOMICILIO",domicilio)
        datos.put("NOLICENCIA", noLicencia)
        if(vence!="") {
            datos.put("VENCE", vence)
        }
        val resultado = tabla.update( "CONDUCTOR", datos,  "IDCONDUCTOR=?", arrayOf(idActualizar))
        if(resultado == 0) return false
        return true
    }



}