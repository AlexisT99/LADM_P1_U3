package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CAMION
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentEditarCamionBinding
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditarCamion.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarCamion : Fragment() {
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
    private lateinit var _binding: FragmentEditarCamionBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarCamionBinding.inflate(inflater, container, false)
        return b.root
    }
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        val bundle = this.arguments
        if (bundle != null) {
            var id = bundle.getInt("id")
            val c = CAMION(activity?.applicationContext!!)
            val dato = c.consultaI(id)
            b.txtAO.setText(dato.año.toString())
            b.txtMarca.setText(dato.marca)
            b.txtModelo.setText(dato.modelo)
            b.txtPlaca.setText(dato.placa)
            b.btnEditar.setOnClickListener {
                c.placa = b.txtPlaca.text.toString()
                c.modelo = b.txtModelo.text.toString()
                c.marca = b.txtMarca.text.toString()
                c.año = Integer.parseInt(b.txtAO.text.toString())
                if(c.actualizar(id.toString())){
                    Toast.makeText(activity?.applicationContext!!, "Se ha actualizado el camión", Toast.LENGTH_LONG).show()
                    (activity as MainActivity?)?.replaceFragment(ListaCamiones(),"Ver Camiones")
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
         * @return A new instance of fragment EditarCamion.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarCamion().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}