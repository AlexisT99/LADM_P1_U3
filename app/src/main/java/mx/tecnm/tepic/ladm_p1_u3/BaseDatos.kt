package mx.tecnm.tepic.ladm_p1_u3

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL("CREATE TABLE USUARIO(ID INTEGER PRIMARY KEY AUTOINCREMENT, USUARIO VARCHAR(200),CONTRASENA VARCHAR(200))")
        p0.execSQL("CREATE TABLE CONDUCTOR(IDCONDUCTOR INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR(200),DOMICILIO VARCHAR(200), NOLICENCIA VARCHAR(200),VENCE DATE)")
        p0.execSQL("CREATE TABLE VEHICULO(IDVEHICULO INTEGER PRIMARY KEY AUTOINCREMENT, PLACA VARCHAR(200),MARCA VARCHAR(200),MODELO VARCHAR(200), AÑO INTEGER,IDCONDUCTOR INTEGER, FOREIGN KEY(IDCONDUCTOR) REFERENCES CONDUCTOR(IDCONDUCTOR))")
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}