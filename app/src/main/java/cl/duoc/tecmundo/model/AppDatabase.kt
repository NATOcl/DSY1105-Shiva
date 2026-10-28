package cl.duoc.tecmundo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// la base de datos del telefono (archivo "shiva.db")
@Database(entities = [UserEntity::class, ParcelEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun parcelDao(): ParcelDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        // una sola base de datos para toda la app
        fun get(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "shiva.db"
            )
                // OJO: al cambiar la version se borra la base (solo para desarrollo)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build().also { instance = it }
        }
    }
}
