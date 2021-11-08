package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Camiones
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CAMION
import mx.tecnm.tepic.ladm_p1_u3.R
import mx.tecnm.tepic.ladm_p1_u3.RecyclerAdapterCamiones
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentCamionesSinConductorBinding
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentLinkearCamionesBinding
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LinkearCamiones.newInstance] factory method to
 * create an instance of this fragment.
 */
class LinkearCamiones : Fragment(), RecyclerAdapterCamiones.onClickListener{
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
    private lateinit var _binding: FragmentLinkearCamionesBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLinkearCamionesBinding.inflate(inflater, container, false)
        return b.root
    }

    lateinit var listaCamion:MutableList<Camiones>
    var ids by Delegates.notNull<Int>()
    lateinit var nombre: String
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        setupRecyclerView()
        val bundle = this.arguments
        if (bundle != null) {
             ids = bundle.getInt("id")
             nombre = bundle.getString("nombre")!!
        }
        //(activity as MainActivity?)?.replaceFragment()
    }

    private fun setupRecyclerView(){
        b.listaCamiones.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        b.listaCamiones.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext!!,
                DividerItemDecoration.VERTICAL)
        )
        val c = CAMION(activity?.applicationContext!!)
        listaCamion = c.consultaSinconductor()
        b.listaCamiones.adapter = RecyclerAdapterCamiones(activity?.applicationContext!!,listaCamion,this)
    }

    private fun RecargarRecycler(){
        b.listaCamiones.adapter = RecyclerAdapterCamiones(activity?.applicationContext!!,listaCamion,this)
    }

    override fun onItemLongClick(id: Int, placa: String, itemView: View, position: Int): Boolean {
        val builder = AlertDialog.Builder(context)
        val c = CAMION(activity?.applicationContext!!)
        builder.setTitle("¿Quieres linkear a $nombre con el camion $placa")
        builder.setPositiveButton("Si"){ d,w->
            if(c.linkear(id.toString(),ids)){
                Toast.makeText(activity?.applicationContext!!, "Cambio de dueño exitoso", Toast.LENGTH_LONG).show()
                (activity as MainActivity?)?.replaceFragment(ConductoresSinCamion(),"Conductores sin camión")
            }

        }
        builder.setNegativeButton("No"){ d,w->
            d.cancel()
        }.show()
        return true
    }

    override fun onClick(id: Int, nombre: String, itemView: View, position: Int): Boolean {
        return true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LinkearCamiones.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LinkearCamiones().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}