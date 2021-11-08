package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentFirstBinding
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CONDUCTOR
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [firstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class firstFragment : Fragment() {
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
    private lateinit var _binding: FragmentFirstBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return b.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        val builder : MaterialDatePicker.Builder<Long> = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Selecciona la fecha de vencimiento")
        val picker: MaterialDatePicker<*> = builder.build()
        var calendar: Calendar
        var format : SimpleDateFormat
        var formattedDate = "2010-01-01"

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
        }
        b.btnRegistrar.setOnClickListener {
            val conductor = CONDUCTOR(activity?.applicationContext!!)
            conductor.domicilio = b.txtDomicilio.text.toString()
            conductor.noLicencia = b.txtNoLicencia.text.toString()
            conductor.nombre = b.txtNombre.text.toString()
            conductor.vence = formattedDate
            if(conductor.insertar()){
                Toast.makeText(activity?.applicationContext!!, "Se ha guardado el conductor", Toast.LENGTH_LONG).show()
                b.txtDomicilio.setText("")
                b.txtNoLicencia.setText("")
                b.txtNombre.setText("")
                b.txtVencimiento.setText("")
            }
            else Toast.makeText(activity?.applicationContext!!, "Error no se ha podido guardar", Toast.LENGTH_LONG).show()
        }

        //(activity as MainActivity?)?.replaceFragment()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment firstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            firstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}