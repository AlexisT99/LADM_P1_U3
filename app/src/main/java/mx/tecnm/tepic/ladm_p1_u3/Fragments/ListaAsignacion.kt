package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Asignacion
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Camiones
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CAMION
import mx.tecnm.tepic.ladm_p1_u3.RecyclerAdapterAsignacion
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentListaAsignacionBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListaAsignacion.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaAsignacion : Fragment(), RecyclerAdapterAsignacion.onClickListener, SearchView.OnQueryTextListener{
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
    private lateinit var _binding: FragmentListaAsignacionBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaAsignacionBinding.inflate(inflater, container, false)
        return b.root
    }

    lateinit var listaAsignacion:MutableList<Asignacion>
    var listaAsignacionOriginal:MutableList<Asignacion>? = null
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
                    var fileContents: String = "Camion,Conductor\n"

                    listaAsignacion.forEach{
                        fileContents += "${it.nomCamion},${it.nomConductor}\n"
                    }
                    val file = File(primaryExternalStorage, "Asignacion "+formattedDate+".csv")
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
        b.listaAsignacion.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        b.listaAsignacion.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext!!,
                DividerItemDecoration.VERTICAL)
        )
        val c = CAMION(activity?.applicationContext!!)
        listaAsignacion = c.Asignacion()
        listaAsignacionOriginal = c.Asignacion()
        b.listaAsignacion.adapter = RecyclerAdapterAsignacion(activity?.applicationContext!!,listaAsignacion,this)
    }

    private fun RecargarRecycler(){
        b.listaAsignacion.adapter = RecyclerAdapterAsignacion(activity?.applicationContext!!,listaAsignacion,this)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListaAsignacion.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListaAsignacion().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemLongClick(id: Int, nomCamion: String, nomConductor: String, itemView: View, position: Int): Boolean {
        val builder = AlertDialog.Builder(context)
        val c = CAMION(activity?.applicationContext!!)
        builder.setTitle("ATENCION")
        builder.setMessage("¿Quieres deslinkear a $nomConductor del camion $nomCamion?")
        builder.setPositiveButton("Si"){ d,w->
            if(c.deslinkear(id)){
                Toast.makeText(activity?.applicationContext!!, "El camion ha quedado sin dueño", Toast.LENGTH_LONG).show()
                listaAsignacion.removeAt(position)
                RecargarRecycler()
            }
        }
        builder.setNegativeButton("No"){ d,w->
            d.cancel()
        }.show()
        return true
    }

    override fun onClick(id: Int, nombre: String, itemView: View, position: Int): Boolean {
        TODO("Not yet implemented")
    }

    fun filtrado(txtBuscar: String) {
        val longitud = txtBuscar.length
        if(longitud == 0){
            listaAsignacion.clear()
            listaAsignacion.addAll( listaAsignacionOriginal!!)
        }else{
            listaAsignacion.clear()
            listaAsignacion.addAll( listaAsignacionOriginal!!)
            val collecion =  listaAsignacion.filter {
                it.nomConductor.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault()))
                    .or(it.nomCamion.lowercase(Locale.getDefault()).contains(txtBuscar.lowercase(Locale.getDefault())))
            }
            listaAsignacion.clear()
            listaAsignacion.addAll(collecion)
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