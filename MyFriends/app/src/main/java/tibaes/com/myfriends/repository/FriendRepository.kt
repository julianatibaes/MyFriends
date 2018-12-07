package tibaes.com.myfriends.repository


import android.arch.lifecycle.LiveData
import tibaes.com.myfriends.db.FriendDAO
import tibaes.com.myfriends.entity.Friend
import android.support.annotation.WorkerThread

class FriendRepository(private val friendDAO: FriendDAO) {

    val allFriends: LiveData<List<Friend>> = friendDAO.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(friend: Friend) {
        friendDAO.insert(friend)
    }

    fun delete(friend: Friend) {
        friendDAO.delete(friend)
    }

    fun update(friend: Friend) {
        friendDAO.update(friend)
    }

}