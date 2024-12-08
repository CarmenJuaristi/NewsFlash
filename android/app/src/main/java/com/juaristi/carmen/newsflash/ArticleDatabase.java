package com.juaristi.carmen.newsflash;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Article.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class ArticleDatabase extends RoomDatabase {

    private static volatile ArticleDatabase instance;
    private static final Object LOCK = new Object();

    // Método abstracto para obtener el DAO
    public abstract ArticleDAO getArticleDao();

    // Método para obtener la instancia de la base de datos
    public static ArticleDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = createDatabase(context);
                }
            }
        }
        return instance;
    }

    // Método para crear la base de datos utilizando Room
    private static ArticleDatabase createDatabase(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                ArticleDatabase.class,
                "article_db.db"
        ).build();
    }
}
