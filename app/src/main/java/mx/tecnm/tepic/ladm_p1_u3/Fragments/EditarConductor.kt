package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CONDUCTOR
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentEditarConductorBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditarConductor.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarConductor : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var _binding: FragmentEditarConductorBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarConductorBinding.inflate(inflater, container, false)
        return b.root
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        val bundle = this.arguments
        val builder : MaterialDatePicker.Builder<Long> = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Selecciona la fecha de vencimiento")
        val picker: MaterialDatePicker<*> = builder.build()
        var calendar: Calendar
        var format : SimpleDateFormat
        var formattedDate = ""
        var cambio = false

        b.btnFecha.setOnClickListener {
            picker.show(activity?.supportFragmentManager!!,picker.toString())
        }
        picker.addOnPositiveButtonClickListener {
            b.txtVencimiento.setText(picker.headerText)
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.setTimeInMillis(it as Long)
            format = SimpleDateFormat("yyyy-MM-dd")
            calendar.add(Calendar.DATE, 1)
            formattedDate = format.format(calendar.getTime())
            cambio = true
        }

        if (bundle != null) {
            var id = bundle.getInt("id")
            var donde = bundle.getInt("donde")
            val c = CONDUCTOR(activity?.applicationContext!!)
            val dato = c.consultaI(id)
            b.txtDomicilio.setText(dato.direccion)
            b.txtNombre.setText(dato.nombre)
            b.txtNoLicencia.setText(dato.Nolicencia)
            b.txtVencimiento.setText(dato.vence)
            b.btnEditar.setOnClickListener {
                if(cambio)c.vence = formattedDate else c.vence = ""
                c.noLicencia = b.txtNoLicencia.text.toString()
                c.nombre = b.txtNombre.text.toString()
                c.domicilio = b.txtDomicilio.text.toString()
                if(c.actualizar(id.toString())){
                    val fragment2 = ListaConductores()
                    Toast.makeText(activity?.applicationContext!!, "Se ha actualizado el conductor", Toast.LENGTH_LONG).show()
                    if(donde==0)
                    (activity as MainActivity?)?.replaceFragment(fragment2,"Ver Conductores")
                    else if(donde==1)
                        (activity as MainActivity?)?.replaceFragment(ListaConductoresVencidas(),"Licencias vencidas")
                }
                else Toast.makeText(activity?.applicationContext!!, "Ha habido un error al actualizar", Toast.LENGTH_LONG).show()
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarConductor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarConductor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}