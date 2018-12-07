package tibaes.com.myfriends.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import tibaes.com.myfriends.db.FriendDatabase
import tibaes.com.myfriends.entity.Friend
import tibaes.com.myfriends.repository.FriendRepository
import kotlin.coroutines.experimental.CoroutineContext

class FriendViewModel(application: Application): AndroidViewModel(application) {


    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: FriendRepository
    val allFriends: LiveData<List<Friend>>

    init {
        val friendDAO = FriendDatabase.getDatabase(application, scope).friendDAO()
        repository = FriendRepository(friendDAO)
        allFriends = repository.allFriends
    }

    fun insert(friend: Friend) = scope.launch(Dispatchers.IO) {
        repository.insert(friend)
    }

    fun update(friend: Friend) = scope.launch(Dispatchers.IO) {
        repository.update(friend)
    }

    fun delete(friend: Friend) = scope.launch(Dispatchers.IO) {
        repository.delete(friend)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}