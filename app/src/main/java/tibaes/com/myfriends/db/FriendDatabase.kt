package tibaes.com.myfriends.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import tibaes.com.myfriends.entity.Friend
import android.arch.persistence.room.Room
import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch


/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [Friend::class], version = 7)
abstract class FriendDatabase: RoomDatabase() {

    abstract fun friendDAO(): FriendDAO

    companion object {
        @Volatile
        private var INSTANCE: FriendDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): FriendDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        FriendDatabase::class.java,
                        "friend-database"
                )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

       
    }
}