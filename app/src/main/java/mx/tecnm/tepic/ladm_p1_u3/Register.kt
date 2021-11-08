package mx.tecnm.tepic.ladm_p1_u3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.USUARIO
import mx.tecnm.tepic.ladm_p1_u3.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.btnRegistrarRegistro.setOnClickListener {
            Log.d("Entre","Entre aca")
            var usuario = USUARIO(this)
            usuario.usuario = b.txtRegistarUsu.text.toString()
            usuario.contraseña = b.txtRegistarPass.text.toString()

            if(usuario.consulta(b.txtRegistarUsu.text.toString())) Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_LONG).show()
            else {
                if(usuario.insertar()){
                    Toast.makeText(this, "El usuario ha sido guardado", Toast.LENGTH_LONG).show()
                    finish()
                }
                else Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
            }
        }
    }
}