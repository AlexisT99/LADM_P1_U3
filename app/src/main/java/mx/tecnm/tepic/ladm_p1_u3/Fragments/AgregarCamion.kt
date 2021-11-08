package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CAMION
import mx.tecnm.tepic.ladm_p1_u3.R
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentAgregarCamionBinding
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentEditarConductorBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregarCamion.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgregarCamion : Fragment() {
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
    private lateinit var _binding: FragmentAgregarCamionBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgregarCamionBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        b.btnRegistrar.setOnClickListener {
            val c = CAMION(activity?.applicationContext!!)
            c.año = Integer.parseInt( b.txtAO.text.toString())
            c.marca = b.txtMarca.text.toString()
            c.modelo = b.txtModelo.text.toString()
            c.placa = b.txtPlaca.text.toString()
            if(c.insertar()){
                Toast.makeText(activity?.applicationContext!!, "Se ha guardado el camion", Toast.LENGTH_LONG).show()
                b.txtAO.setText("")
                b.txtMarca.setText("")
                b.txtModelo.setText("")
                b.txtPlaca.setText("")
            }
            else Toast.makeText(activity?.applicationContext!!, "Ha ocurrido un error al guardar", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AgregarCamion.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregarCamion().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}