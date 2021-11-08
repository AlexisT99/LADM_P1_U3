package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Conductores
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CONDUCTOR
import mx.tecnm.tepic.ladm_p1_u3.RecyclerAdapterConductores
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentConductoresSinCamionBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConductoresSinCamion.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConductoresSinCamion : Fragment(), RecyclerAdapterConductores.onClickListener, SearchView.OnQueryTextListener {
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
    private lateinit var _binding: FragmentConductoresSinCamionBinding
    private val b get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConductoresSinCamionBinding.inflate(inflater, container, false)
        return b.root
    }
    lateinit var listaConductores:MutableList<Conductores>
    var listaConductoresOriginal: MutableList<Conductores>? = null
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        setupRecyclerView()
        b.btnexc.setOnClickListener{
            if(tieneSd()){
                try {
                    val externalStorageVolumes: Array<out File> =
                        ContextCompat.getExternalFilesDirs(activity?.applicationContext!!, null)
                    val primaryExternalStorage = externalStorageVolumes[0]
                    val c: Date = Calendar.getInstance().getTime()
                    val df = SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm:ss")
                    val formattedDate: String = df.format(c)
                    var fileContents: String = "Nombre,direccion,No licencia, Vencimiento\n"

                    listaConductores.forEach{
                        fileContents += "${it.nombre},${it.direccion},${it.Nolicencia},${it.vence}\n"
                    }
                    val file = File(primaryExternalStorage, "Conductores sin camion "+formattedDate+".csv")
                    file.writeText(fileContents)
                    file.createNewFile()
                    Toast.makeText(activity?.applicationContext!!, "Se ha exportado el archivo con exito", Toast.LENGTH_SHORT).show()
                }
                catch (io: SecurityException) {
                    Toast.makeText(activity?.applicationContext!!,"Ocurrio un error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(activity?.applicationContext!!,"No cuenta con memoria externa o no se permite escribir en ella",
                    Toast.LENGTH_SHORT).show()
            }
        }
        //(activity as MainActivity?)?.replaceFragment()
        b.search.setOnQueryTextListener(this)
    }
    fun tieneSd(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
    private fun setupRecyclerView(){
        b.listaConductores.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        b.listaConductores.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext!!,
                DividerItemDecoration.VERTICAL)
        )
        val c = CONDUCTOR(activity?.applicationContext!!)
        listaConductores = c.consultaSincamion()
        listaConductoresOriginal = c.consultaSincamion()
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
        builder.setTitle("¿Quieres linkear a ${nombre} con un camión?")
        builder.setPositiveButton("Si"){ d,w->
            val bundle = Bundle()
            bundle.putInt("id",id) // Put anything what you want
            bundle.putString("nombre",nombre)
            val fragment2 = LinkearCamiones()
            fragment2.arguments = bundle
            (activity as MainActivity?)?.replaceFragment(fragment2,"Linkear camión a ${nombre}")
        }
        builder.setNegativeButton("No"){ d,w->
            d.cancel()
        }.show()
       return true
    }

    override fun filtrado(txtBuscar: String) {
        val longitud = txtBuscar.length
        if(longitud == 0){
            listaConductores.clear()
            listaConductores.addAll(listaConductoresOriginal!!)
        }else{
            listaConductores.clear()
            listaConductores.addAll(listaConductoresOriginal!!)
            val collecion = listaConductores.filter {
                it.nombre.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault())).or(it.direccion.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault())))
            }
            listaConductores.clear()
            listaConductores.addAll(collecion)
        }
        RecargarRecycler()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
       return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if (p0 != null) {
            filtrado(p0)
        }
        return false
    }
}