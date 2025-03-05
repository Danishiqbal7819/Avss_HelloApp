import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountryAdapter(
    private var countryList: List<String>,
    private val onCountrySelected: (String) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCountryName: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val countryName = countryList[position]
        holder.tvCountryName.text = countryName
        holder.itemView.setOnClickListener {
            onCountrySelected(countryName)
        }
    }

    override fun getItemCount(): Int = countryList.size

    fun updateCountryList(newList: List<String>) {
        countryList = newList
        notifyDataSetChanged()
    }
}
