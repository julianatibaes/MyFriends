package tibaes.com.myfriends.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "friend_table")
data class Friend (
        @ColumnInfo(name = "fName")
        @NotNull
        val fName: String,
        @ColumnInfo(name = "fPhone")
        var fPhone: String= "",
        @ColumnInfo(name = "fEmail")
        var fEmail: String = "",
        @ColumnInfo(name = "fSite")
        var fSite: String = "",
        @ColumnInfo(name = "fAddress")
        var fAddress: String = "",
        @ColumnInfo(name = "fphoto")
        var fphoto: String = ""
):Serializable{
        @PrimaryKey(autoGenerate = true)
        var fId: Long = 0
}
