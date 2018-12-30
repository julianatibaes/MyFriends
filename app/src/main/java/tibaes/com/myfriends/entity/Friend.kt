package tibaes.com.myfriends.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.util.*

@Entity(tableName = "friend_table")
data class Friend (
        @ColumnInfo(name = "fName")
        @NotNull
        var fName: String,
        @ColumnInfo(name = "phone")
        var phone: String= "",
        @ColumnInfo(name = "email")
        var email: String = "",
        @ColumnInfo(name = "site")
        var site: String = "",
        @ColumnInfo(name = "address")
        var address: String = "",
        @ColumnInfo(name = "photoPath")
        var photoPath: String = ""
):Serializable{
        @PrimaryKey(autoGenerate = true)
        var fId: Long = 0

}
