package RoomDbWithMVVMWithLayers.UI.View

import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
import RoomDbWithMVVMWithLayers.Data.Model.RdbAdapter
import RoomDbWithMVVMWithLayers.UI.ViewModel.ShowDataRDbViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloapp.R
import com.example.helloapp.databinding.ActivityShowRdbDataBinding

class ShowRDbDataActivity : AppCompatActivity() {
    private lateinit var showRDbDataViewModel: ShowDataRDbViewModel
    private lateinit var binding: ActivityShowRdbDataBinding
    private lateinit var datalist: MutableList<RDbEntity>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RdbAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowRdbDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datalist = mutableListOf()

        showRDbDataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(ShowDataRDbViewModel::class.java)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerviews1)

        recyclerView.layoutManager = LinearLayoutManager(this)
        datalist = mutableListOf<RDbEntity>()
        adapter = RdbAdapter(this, datalist)
        recyclerView.adapter = adapter

        showRDbDataViewModel.fetchAllUsers()

        showRDbDataViewModel.usersLiveData.observe(this) { users ->
            datalist.clear()
            datalist.addAll(users)
            adapter.notifyDataSetChanged()
            Log.d("Adapter", "Adapter list updated: $datalist")
        }
        binding.fabAddUser.setOnClickListener {
            val intent = Intent(this, SaveDataActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}