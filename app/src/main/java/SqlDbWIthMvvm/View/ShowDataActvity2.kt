package SqlDbWIthMvvm.View

import RoomDbWithMVVM.View.SaveDataActivity
import SqlDbWIthMvvm.Model.Users
import SqlDbWIthMvvm.ViewModel.ShowdataViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShowDataActvity2 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var showdataViewModel: ShowdataViewModel
    private lateinit var adapter: UserAdapter2
    private lateinit var userDatalist: MutableList<Users>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showdata)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nousertext = findViewById<TextView>(R.id.No_user)
        nousertext.visibility = View.GONE

//////////////////////////////////////////////////////////////////////////////
        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            val intent = Intent(this, SaveDataActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }

        userDatalist = mutableListOf()

//////////////////////////////////////////////////////////////////////////////
        showdataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShowdataViewModel::class.java)

//////////////////////////////////////////////////////////////////////////////
        recyclerView = findViewById(R.id.recyclerviews)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter2(applicationContext, userDatalist)
        recyclerView.adapter = adapter

        showdataViewModel.usersLiveData.observe(this) { users ->
            userDatalist.clear()
            userDatalist.addAll(users)
            adapter.notifyDataSetChanged()
            if (users.isEmpty()) {
                nousertext.visibility = View.VISIBLE
            } else {
                nousertext.visibility = View.GONE
            }
        }

    }

}
