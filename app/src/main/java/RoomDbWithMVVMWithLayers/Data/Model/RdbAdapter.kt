package RoomDbWithMVVMWithLayers.Data.Model

import RoomDbWithMVVMWithLayers.Data.Local.MyRoomDb
import RoomDbWithMVVMWithLayers.Data.Local.RDbEntity
import RoomDbWithMVVMWithLayers.UI.View.SaveDataActivity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.helloapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RdbAdapter(val context: Context, private var list: MutableList<RDbEntity>) :
    RecyclerView.Adapter<RdbAdapter.UserViewHolder>() {

    val db = MyRoomDb.getInstance(context).UserDao()

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dpImage: ImageView = itemView.findViewById(R.id.imageProfile)
        val fullName: TextView = itemView.findViewById(R.id.textFullName)
        val phone: TextView = itemView.findViewById(R.id.textPhone)
        val email: TextView = itemView.findViewById(R.id.textEmail)
        val address: TextView = itemView.findViewById(R.id.textAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemsrecyclerviewfor_rdb, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val rDbEntity = list[position]
        holder.fullName.text =
            "${rDbEntity.first_name} ${rDbEntity.middle_name} ${rDbEntity.last_name}"
        holder.address.text = "${rDbEntity.address_name}"
        holder.phone.text = rDbEntity.phone_name
        holder.email.text = rDbEntity.email_name

        if (rDbEntity.image.isNotEmpty()) {
            holder.dpImage.setImageBitmap(byteArrayToBitmap(rDbEntity.image))
        }

        holder.itemView.setOnClickListener {
            showUserDetailsDialog(rDbEntity)
        }

        holder.itemView.findViewById<ImageView>(R.id.btnEdit).setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menuInflater.inflate(R.menu.openmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete1 -> {
                        GlobalScope.launch(Dispatchers.IO) {
                            val isDeleted = db.DeleteUsers(rDbEntity)
                            withContext(Dispatchers.Main) {
                                if (isDeleted > 0) {
                                    list.removeAt(position)
                                    notifyItemRemoved(position)
                                    Toast.makeText(
                                        context,
                                        "User deleted successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "User deletion failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        true
                    }

                    R.id.update1 -> {
                        val intent = Intent(holder.itemView.context, SaveDataActivity::class.java)
                        intent.putExtra("userId", rDbEntity.id)
                        intent.putExtra("updatevalue", 1)
                        intent.putExtra("btntext", "Update")
                        holder.itemView.context.startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
            true
        }
    }

    override fun getItemCount(): Int = list.size


    private fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun showUserDetailsDialog(user: RDbEntity) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.user_details_popup, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).create()

        val imageProfile = dialogView.findViewById<ImageView>(R.id.imageProfileDialog)
        val textName = dialogView.findViewById<TextView>(R.id.textUserNameDialog)
        val textPhone = dialogView.findViewById<TextView>(R.id.textPhoneDialog)
        val textEmail = dialogView.findViewById<TextView>(R.id.textEmailDialog)
        val textaddress = dialogView.findViewById<TextView>(R.id.textAddresss)
        val btnClose = dialogView.findViewById<ImageView>(R.id.btnClose)

        textName.text = "${user.first_name} ${user.middle_name} ${user.last_name}"
        textaddress.text = "${user.address_name}"
        textPhone.text = user.phone_name
        textEmail.text = user.email_name

        val image1 = user.image
        imageProfile.setImageBitmap(byteArrayToBitmap(image1))
        btnClose.setOnClickListener { dialog.dismiss() }


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
