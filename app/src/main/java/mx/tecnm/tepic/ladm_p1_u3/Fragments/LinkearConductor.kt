package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Conductores
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CAMION
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CONDUCTOR
import mx.tecnm.tepic.ladm_p1_u3.R
import mx.tecnm.tepic.ladm_p1_u3.RecyclerAdapterConductores
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentConductoresSinCamionBinding
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentLinkearConductorBinding
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LinkearConductor.newInstance] factory method to
 * create an instance of this fragment.
 */
class LinkearConductor : Fragment(), RecyclerAdapterConductores.onClickListener {
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
    private lateinit var _binding: FragmentLinkearConductorBinding
    private val b get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLinkearConductorBinding.inflate(inflater, container, false)
        return b.root
    }
    lateinit var listaConductores:MutableList<Conductores>
    var ids by Delegates.notNull<Int>()
    lateinit var placa: String

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        setupRecyclerView()
        val bundle = this.arguments
        if (bundle != null) {
            ids = bundle.getInt("id")
            placa = bundle.getString("placa")!!
        }
        //(activity as MainActivity?)?.replaceFragment()
    }
    private fun setupRecyclerView(){
        b.listaConductores.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        b.listaConductores.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext!!,
                DividerItemDecoration.VERTICAL)
        )
        val c = CONDUCTOR(activity?.applicationContext!!)
        listaConductores = c.consultaSincamion()
        b.listaConductores.adapter = RecyclerAdapterConductores(activity?.applicationContext!!,listaConductores,this)
    }

    private fun RecargarRecycler(){
        b.listaConductores.adapter = RecyclerAdapterConductores(activity?.applicationContext!!,listaConductores,this)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListaConductores.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListaConductores().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemLongClick(id: Int, nombre: String, itemView: View, position: Int): Boolean{
        val builder = AlertDialog.Builder(context)
        val c = CAMION(activity?.applicationContext!!)
        builder.setTitle("??Quieres linkear a $nombre con el camion $placa?")
        builder.setPositiveButton("Si"){ d,w->
            if(c.linkear(ids.toString(),id)){
                Toast.makeText(activity?.applicationContext!!, "Cambio de due??o exitoso", Toast.LENGTH_LONG).show()
                (activity as MainActivity?)?.replaceFragment(CamionesSinConductor(),"Camiones sin conductor")
            }
        }
        builder.setNegativeButton("No"){ d,w->
            d.cancel()
        }.show()
        return true
    }

    override fun filtrado(txtBuscar: String) {
        TODO("Not yet implemented")
    }
}