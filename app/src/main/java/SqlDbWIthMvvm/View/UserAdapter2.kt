package SqlDbWIthMvvm.View

import SqlDbWIthMvvm.Model.Users
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.helloapp.R
import com.example.myapp.database.DbHelper

class UserAdapter2(val context: Context, var userList: MutableList<Users>) :
    RecyclerView.Adapter<UserAdapter2.UserViewHolder>() {
    private var bitmap: Bitmap? = null
    val dbHelper = DbHelper(context)

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dpImage: ImageView = itemView.findViewById(R.id.imageProfile)
        val firstName: TextView = itemView.findViewById(R.id.textFirstName)
        val middleName: TextView = itemView.findViewById(R.id.textMiddleName)
        val lastName: TextView = itemView.findViewById(R.id.textLastName)
        val phone: TextView = itemView.findViewById(R.id.textPhone)
        val email: TextView = itemView.findViewById(R.id.textEmail)
        val imagebtn: ImageView = itemView.findViewById(R.id.imageProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.itemsrecyclerview, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        val id = userList[position].id
        holder.firstName.text = user.firstName
        holder.middleName.text = user.middleName
        holder.lastName.text = user.lastName
        holder.phone.text = user.phone
        holder.email.text = user.email
        holder.dpImage.setImageBitmap(byteArrayToBitmap(user.image))
        holder.itemView.setOnLongClickListener {
            animateZoomIn(holder.itemView)
            true
        }
        holder.itemView.findViewById<ImageView>(R.id.btnEdit).setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.openmenu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete1 -> {
                        val isDeleted = dbHelper.deleteById(id)
                        notifyItemRemoved(position)
                        if (isDeleted) {
                            userList.removeAt(position)
                            Toast.makeText(context, "User deleted Succesfully", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "User deletion Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                        true
                    }

                    R.id.update1 -> {
                        val imageView = holder.dpImage
                        val drawable = imageView.drawable
                        if (drawable is BitmapDrawable) {
                            bitmap = drawable.bitmap
                        }
                        val intent = Intent(holder.itemView.context, SqlActivity2::class.java)
                        intent.putExtra("userId", userList[position].id)
                        intent.putExtra("updatevalue", 1)
                        intent.putExtra("btntext", "Update")
                        holder.itemView.context.startActivity(intent)
                        if (holder.itemView.context is Activity) {
                            (holder.itemView.context as Activity).finish()
                        }
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
            true
        }
    }

    override fun getItemCount(): Int = userList.size
    private fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun animateZoomIn(view: View) {
        view.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(200)
            .start()

        view.postDelayed({
            animateZoomOut(view)
        }, 1000)
    }

    private fun animateZoomOut(view: View) {
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(200)
            .start()
    }
}
