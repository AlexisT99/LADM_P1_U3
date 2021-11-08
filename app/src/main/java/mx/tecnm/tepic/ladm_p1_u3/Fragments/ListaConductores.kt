package mx.tecnm.tepic.ladm_p1_u3.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import mx.tecnm.tepic.ladm_p1_u3.MainActivity
import mx.tecnm.tepic.ladm_p1_u3.Modelos.Conductores
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CAMION
import mx.tecnm.tepic.ladm_p1_u3.databinding.FragmentListaConductoresBinding
import mx.tecnm.tepic.ladm_p1_u3.ModelosBD.CONDUCTOR
import mx.tecnm.tepic.ladm_p1_u3.RecyclerAdapterConductores
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Stream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListaConductores.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaConductores : Fragment(), RecyclerAdapterConductores.onClickListener, SearchView.OnQueryTextListener {
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
    private lateinit var _binding: FragmentListaConductoresBinding
    private val b get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListaConductoresBinding.inflate(inflater, container, false)
        return b.root
    }
    lateinit var listaConductores:MutableList<Conductores>
    var listaConductoresOriginal: MutableList<Conductores>? = null

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        setupRecyclerView()
        b.btnadd.setOnClickListener {
            (activity as MainActivity?)?.replaceFragment(firstFragment(),"Agregar Conductores")
        }
        b.btnexc.setOnClickListener{
            b.menubotones.collapse()
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
                    val file = File(primaryExternalStorage, "Conductores "+formattedDate+".csv")
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
    private fun setupRecyclerView(){
        b.listaConductores.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        b.listaConductores.addItemDecoration(DividerItemDecoration(activity?.applicationContext!!,DividerItemDecoration.VERTICAL))
        val c = CONDUCTOR(activity?.applicationContext!!)
        listaConductores = c.consulta()
        listaConductoresOriginal = c.consulta()
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
        val menu = PopupMenu(context,itemView)
        val builder = AlertDialog.Builder(context)
        val c = CONDUCTOR(activity?.applicationContext!!)
        val c1 = CAMION(activity?.applicationContext!!)
        var cambio = true
        menu.menu.add("Borrar")
        menu.menu.add("Editar")
        menu.setOnMenuItemClickListener {
            menu.dismiss()
            if (it.title.equals("Borrar")) {
                builder.setTitle("¿Estas Seguro de borrar el conductor ${nombre}?")
                builder.setMessage("Se borrara su relación con su camión en caso de existir")
                builder.setPositiveButton("Si"){ d,w->
                    c.eliminar(id)
                    c1.deslinkear(id)
                    listaConductores.removeAt(position)
                    listaConductoresOriginal?.removeAt(position)
                    RecargarRecycler()
                }
                builder.setNegativeButton("No"){ d,w->
                    d.cancel()
                }.show()
            }
            else if (it.title.equals("Editar")) {
                val bundle = Bundle()
                bundle.putInt("id", id) // Put anything what you want
                bundle.putInt("donde",0)
                val fragment2 = EditarConductor()
                fragment2.arguments = bundle
                (activity as MainActivity?)?.replaceFragment(fragment2,"Editar Conductor")
            }
            true
        }
        menu.show()
        return cambio
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

    fun tieneSd(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
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