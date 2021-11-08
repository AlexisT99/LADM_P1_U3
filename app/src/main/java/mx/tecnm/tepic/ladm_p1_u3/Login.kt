package mx.tecnm.tepic.ladm_p1_u3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.USUARIO
import mx.tecnm.tepic.ladm_p1_u3.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.btnRegistar.setOnClickListener {
            val intento = Intent( this, Register::class.java)
            startActivity(intento)
        }
        b.btnLogin.setOnClickListener {
            var usuario = USUARIO(this)
            usuario.usuario = b.txtLoginUsu.text.toString()
            usuario.contraseña = b.txtLogPass.text.toString()

            if(usuario.consultaUsu(usuario.usuario,usuario.contraseña)) {
                val intento = Intent( this, MainActivity::class.java)
                startActivity(intento)
                finish()
            }
            else {
                    Toast.makeText(this, "Hay un error en los datos revisa", Toast.LENGTH_LONG).show()
            }
        }
    }
}