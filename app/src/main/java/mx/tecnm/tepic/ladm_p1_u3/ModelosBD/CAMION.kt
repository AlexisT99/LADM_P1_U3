package mx.tecnm.tepic.ladm_p1_u3.ModelosBD

import android.content.ContentValues
import android.content.Context
import android.util.Log
import mx.tecnm.tepic.ladm_p1_u3.BaseDatos
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Asignacion
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Camiones
import java.util.*

class CAMION(p:Context) {
    val p = p
    var placa = ""
    var marca = ""
    var modelo = ""
    var año = 0
    var idConductor: Int = -1

    //PLACA VARCHAR(200),MARCA VARCHAR(200),MODELO VARCHAR(200), AÑO INTEGER,IDCONDUCTOR INTEGER
    fun insertar(): Boolean {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val dato = ContentValues()
        dato.put("PLACA", placa)
        dato.put("MARCA",marca)
        dato.put("MODELO", modelo)
        dato.put("AÑO",año)
        dato.put("IDCONDUCTOR",idConductor)
        val resultado = tabla.insert("VEHICULO", null, dato)
        tabla.close()
        if (resultado == -1L) return false
        return true
    }
    fun consulta(): MutableList<Camiones> {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val listaCamiones = mutableListOf<Camiones>()
        val cursor = tabla.query("VEHICULO", arrayOf("*"), null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                listaCamiones.add(Camiones(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(0)))
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaCamiones
    }

    fun consultaSinconductor(): MutableList<Camiones> {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val listaCamiones = mutableListOf<Camiones>()

        val cursor = tabla.rawQuery("SELECT * FROM VEHICULO WHERE IDCONDUCTOR= -1",null)
        if (cursor.moveToFirst()) {
            do {
                Log.d("Dato que busco",cursor.getInt(5).toString())
                listaCamiones.add(Camiones(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(0)))
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaCamiones
    }
    fun consultaI(id:Int): Camiones {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        lateinit var camiones:Camiones
        val cursor = tabla.query("VEHICULO", arrayOf("*"), "IDVEHICULO=?", arrayOf(id.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            camiones = Camiones(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(0))
        }
        tabla.close()
        return camiones
    }

    fun consultaAnt(año:Int): MutableList<Camiones> {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val listaConductores = mutableListOf<Camiones>()
        this.año = Calendar.getInstance().get(Calendar.YEAR)-año
        val cursor = tabla.query("VEHICULO", arrayOf("*"), "AÑO=?", arrayOf(this.año.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            do {
                listaConductores.add(Camiones(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(0)))
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaConductores
    }


    fun eliminar(id: Int): Boolean {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val resultado = tabla.delete("VEHICULO", "IDVEHICULO=?", arrayOf(id.toString()))
        if (resultado == 0) return false
        return true
    }

    fun actualizar(idActualizar:String): Boolean{
        val tabla = BaseDatos (p, "empresaTransporte", null,  1).writableDatabase
        val datos = ContentValues()
        datos.put("PLACA", placa)
        datos.put("MARCA",marca)
        datos.put("MODELO", modelo)
        datos.put("AÑO",año)
        val resultado = tabla.update( "VEHICULO", datos,  "IDVEHICULO=?", arrayOf(idActualizar))
        if(resultado == 0) return false
        return true
    }

    fun linkear(idActualizar:String,conductor:Int): Boolean{
        val tabla = BaseDatos (p, "empresaTransporte", null,  1).writableDatabase
        val datos = ContentValues()
        datos.put("IDCONDUCTOR", conductor)
        Log.d("dato1",idActualizar)
        Log.d("dato2",conductor.toString())
        val resultado = tabla.update( "VEHICULO", datos,  "IDVEHICULO=?", arrayOf(idActualizar))
        if(resultado == 0) return false
        return true
    }

    fun deslinkear(conductor:Int): Boolean{
        val tabla = BaseDatos (p, "empresaTransporte", null,  1).writableDatabase
        val datos = ContentValues()
        datos.put("IDCONDUCTOR", -1)
        val resultado = tabla.update( "VEHICULO", datos,  "IDCONDUCTOR=?", arrayOf(conductor.toString()))
        if(resultado == 0) return false
        return true
    }
    fun Asignacion(): MutableList<Asignacion> {
        val tabla = BaseDatos(p, "empresaTransporte", null, 1).writableDatabase
        val listaAsignacion = mutableListOf<Asignacion>()
        val cursor = tabla.rawQuery("SELECT c.IDCONDUCTOR,v.IDVEHICULO,c.NOMBRE,v.MARCA,v.MODELO,v.PLACA FROM CONDUCTOR c INNER JOIN VEHICULO v ON c.IDCONDUCTOR = v.IDCONDUCTOR ",null)
        if (cursor.moveToFirst()) {
            do {
                val nomCarro = "${cursor.getString(3)} ${cursor.getString(4)} con placa ${cursor.getString(5)}"
                listaAsignacion.add(Asignacion(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),nomCarro))
            } while (cursor.moveToNext())
        }
        tabla.close()
        return listaAsignacion
    }

}