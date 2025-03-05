package SqliteDatabase

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
import com.example.myapp.database.DatabaseHelper
import com.example.myapp.database.User

class UserAdapter(val context: Context, var userList: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var bitmap: Bitmap? = null
    val dbHelper = DatabaseHelper(context)

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
            // Create the popup menu
            val popupMenu = PopupMenu(it.context, it)
            val inflater = popupMenu.menuInflater
            inflater.inflate(
                R.menu.openmenu,
                popupMenu.menu
            ) // Replace 'popup_menu' with your menu XML

            // Set click listener for the menu items
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
                        val intent = Intent(holder.itemView.context, SqlActivity::class.java)
                        intent.putExtra("userId", userList.get(position).id)
                        intent.putExtra("firstname", userList.get(position).firstName)
                        intent.putExtra("middlename", userList.get(position).middleName)
                        intent.putExtra("lastname", userList.get(position).lastName)
                        intent.putExtra("phone", userList.get(position).phone)
                        intent.putExtra("email", userList.get(position).email)
//                        intent.putExtra("image",userList.get(position).image)
                        intent.putExtra("btntext", "Update")
                        intent.putExtra("updatevalue", 1)
                        val img11 = byteArrayToBitmap(userList.get(position).image)
                        val compressedBitmap = Bitmap.createScaledBitmap(img11, 300, 300, true)
                        intent.putExtra("image", compressedBitmap)

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

            true // Return true to indicate that the long click was handled
        }

    }

    override fun getItemCount(): Int = userList.size
    private fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}

