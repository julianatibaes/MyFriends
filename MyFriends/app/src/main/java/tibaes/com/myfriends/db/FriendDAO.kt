package tibaes.com.myfriends.db

import tibaes.com.myfriends.entity.Friend
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface FriendDAO {

    @Insert
    fun insert(friend: Friend)

    @Query("DELETE FROM friend_table")
    fun deleteAll()

    @Delete
    fun delete(friend: Friend)

    @Query("SELECT * from friend_table ORDER BY fName ASC")
    fun getAll(): LiveData<List<Friend>>

    @Query("SELECT * from friend_table WHERE fId = :id")
    fun getFriend(id: Long): LiveData<Friend>

    @Update
    fun update(friend: Friend)

}