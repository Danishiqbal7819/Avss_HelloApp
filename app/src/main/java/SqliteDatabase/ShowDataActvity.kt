package SqliteDatabase

//import androidx.compose.ui.graphics.BlendMode.Companion.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloapp.R
import com.example.myapp.database.DatabaseHelper
import com.example.myapp.database.User

class ShowDataActvity : AppCompatActivity() {
    private lateinit var menu1: Menu

    //    private lateinit var binding: ActivityShowdataBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userDatalist: MutableList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showdata)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userDatalist = mutableListOf()
        recyclerView = findViewById(R.id.recyclerviews)

        val dbHelper = DatabaseHelper(this)

        userDatalist.clear()
        userDatalist.addAll(dbHelper.getAllUsers())

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(applicationContext, userDatalist)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }


}


