import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.truck_mixer.R
import com.example.truck_mixer.TruckModel
import android.util.Log as log



class TruckAdapter(
    private val list: List<TruckModel>,
    private val onEdit: (String) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<TruckAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val txtNoTm: TextView = v.findViewById(R.id.txtNoTm)
        val txtDriver: TextView = v.findViewById(R.id.txtDriver)
        val txtKaroseri: TextView = v.findViewById(R.id.txtKaroseri)
        val btnEdit: Button = v.findViewById(R.id.btnEdit)
        val btnDelete: Button = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_truck, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val d = list[pos]

        h.txtNoTm.text = d.noTm
        h.txtDriver.text = "${d.nama_driver1} / ${d.nama_driver2}"
        h.txtKaroseri.text = d.karoseri

        h.btnEdit.setOnClickListener {
            onEdit(d.noTm)
        }

        h.btnDelete.setOnClickListener {
            onDelete(d.noTm)
        }
    }

    override fun getItemCount() = list.size
}

