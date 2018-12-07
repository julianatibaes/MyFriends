package tibaes.com.myfriends.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_friend.*
import tibaes.com.myfriends.R
import tibaes.com.myfriends.adapter.FriendListAdapter
import tibaes.com.myfriends.entity.Friend
import tibaes.com.myfriends.viewmodel.FriendViewModel

// FONT: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0

class ListFriendActivity : AppCompatActivity() {

    private lateinit var friendViewModel: FriendViewModel
    private val newActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_friend)

        fbtnAddFriend.setOnClickListener {
            startActivity(Intent(this, NewFriendActivity::class.java))
        }

        val recyclerView = rvFriend
        val adapter = FriendListAdapter(this)

        adapter.onItemClick = {it ->
            val intent = Intent(this@ListFriendActivity, NewFriendActivity::class.java)
            intent.putExtra(NewFriendActivity.EXTRA_REPLY , it)
            startActivityForResult(intent, newActivityRequestCode)
        }

        recyclerView.adapter = adapter
        //val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider
        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        friendViewModel.allFriends.observe(this, Observer { friends ->
            // Update the cached copy of the words in the adapter.
            friends?.let { adapter.setFriendList(it) }
        })

        fbtnAddFriend.setOnClickListener {
            val intent = Intent(this@ListFriendActivity, NewFriendActivity::class.java)
            startActivityForResult(intent, newActivityRequestCode)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let { data ->
                try {
                    val friend: Friend?
                    friend = data.getSerializableExtra(NewFriendActivity.EXTRA_REPLY) as Friend
                    friend.let {
                        if(friend.fId > 0) friendViewModel.update(friend)
                        else friendViewModel.insert(friend)

                    }
                } catch (e: Exception){
                    val friend: Friend?  = data.getSerializableExtra(NewFriendActivity.EXTRA_DELETE) as Friend
                    friend.let {
                        friendViewModel.delete(friend!!)
                    }
                }
            }
        }
    }

}
