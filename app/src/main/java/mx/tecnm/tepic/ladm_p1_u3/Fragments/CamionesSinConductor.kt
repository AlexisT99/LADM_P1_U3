package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Camiones
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CAMION
import mx.tecnm.tepic.ladm_p1_u3.R
import mx.tecnm.tepic.ladm_p1_u3.RecyclerAdapterCamiones
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentCamionesSinConductorBinding
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentListaCamionesBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CamionesSinConductor.newInstance] factory method to
 * create an instance of this fragment.
 */
class CamionesSinConductor :  Fragment(), RecyclerAdapterCamiones.onClickListener, SearchView.OnQueryTextListener{
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
    private lateinit var _binding: FragmentCamionesSinConductorBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCamionesSinConductorBinding.inflate(inflater, container, false)
        return b.root
    }

    lateinit var listaCamion:MutableList<Camiones>
    var listaCamionOriginal:MutableList<Camiones>? = null
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
                    var fileContents: String = "Modelo,Marca,Año,Placa\n"

                    listaCamion.forEach{
                        fileContents += "${it.modelo},${it.marca},${it.año},${it.placa}\n"
                    }
                    val file = File(primaryExternalStorage, "Lista de camiones sin conductor "+formattedDate+".csv")
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
        b.search.setOnQueryTextListener(this)
        //(activity as MainActivity?)?.replaceFragment()
    }
    fun tieneSd(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun setupRecyclerView(){
        b.listaCamiones.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        b.listaCamiones.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext!!,
                DividerItemDecoration.VERTICAL)
        )
        val c = CAMION(activity?.applicationContext!!)
        listaCamion = c.consultaSinconductor()
        listaCamionOriginal = c.consultaSinconductor()
        b.listaCamiones.adapter = RecyclerAdapterCamiones(activity?.applicationContext!!,listaCamion,this)
    }

    private fun RecargarRecycler(){
        b.listaCamiones.adapter = RecyclerAdapterCamiones(activity?.applicationContext!!,listaCamion,this)
    }

    override fun onItemLongClick(id: Int, placa: String, itemView: View, position: Int): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("¿Quieres linkear al camión con placa ${placa} con un conductor?")
        builder.setPositiveButton("Si"){ d,w->
            val bundle = Bundle()
            bundle.putInt("id",id) // Put anything what you want
            bundle.putString("placa",placa)
            val fragment2 = LinkearConductor()
            fragment2.arguments = bundle
            (activity as MainActivity?)?.replaceFragment(fragment2,"Linkear camión ${placa}")
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
         * @return A new instance of fragment CamionesSinConductor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CamionesSinConductor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun filtrado(txtBuscar: String) {
        val longitud = txtBuscar.length
        if(longitud == 0){
            listaCamion.clear()
            listaCamion.addAll(listaCamionOriginal!!)
        }else{
            listaCamion.clear()
            listaCamion.addAll(listaCamionOriginal!!)
            val collecion = listaCamion.filter {
                it.placa.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault()))
                    .or(it.marca.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault())))
                    .or(it.modelo.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault())))
                    .or(it.año.toString().lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault())))
            }
            listaCamion.clear()
            listaCamion.addAll(collecion)
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