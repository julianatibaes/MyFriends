package tibaes.com.myfriends.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_list_friend.view.*
import tibaes.com.myfriends.R
import tibaes.com.myfriends.entity.Friend

class FriendListAdapter internal constructor(context: Context) :
        RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

    var onItemClick: ((Friend) -> Unit)? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var friends  = emptyList<Friend>() // Cached copy of friends

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoFriend: CircleImageView = itemView.imgFriendListPhoto
        val nameFriend: TextView = itemView.txtFriendListName

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(friends[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(holder: ViewGroup, position: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_list_friend, holder, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = friends[position]
        holder.nameFriend.text = current.fName
        //TODO: get image friend
    }

    override fun getItemCount() = friends.size

    fun setFriendList(frindList: List<Friend>){
        this.friends = frindList
        notifyDataSetChanged()
    }

}