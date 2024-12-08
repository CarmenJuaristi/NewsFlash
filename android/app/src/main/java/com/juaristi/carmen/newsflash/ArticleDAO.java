package com.juaristi.carmen.newsflash;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDAO {

    // Inserta un artículo en la base de datos. Reemplaza si ya existe.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(Article article); // Nota: No es suspend, se ejecuta en un hilo separado si es necesario.

    // Obtiene todos los artículos de la base de datos.
    @Query("SELECT * FROM articles")
    LiveData<List<Article>> getAllArticles();

    // Elimina un artículo de la base de datos.
    @Delete
    void deleteArticle(Article article);
}

