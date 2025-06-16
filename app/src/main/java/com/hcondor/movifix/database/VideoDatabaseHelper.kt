package com.hcondor.movifix.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hcondor.movifix.model.Movie

class VideoDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "videos.db", null, 3) { // Incrementa versión si haces cambios estructurales

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE videos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                year TEXT,
                description TEXT,
                authors TEXT,
                videoUrl TEXT,
                imageUrl TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE movies_fav (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                year TEXT,
                description TEXT,
                authors TEXT,
                videoUrl TEXT,
                imageUrl TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS videos")
        db.execSQL("DROP TABLE IF EXISTS movies_fav")
        onCreate(db)
    }

    fun insertMovie(m: Movie): Long {
        val cv = ContentValues().apply {
            put("title", m.title)
            put("year", m.year)
            put("description", m.description)
            put("authors", m.authors)
            put("videoUrl", m.videoUrl)
            put("imageUrl", m.imageUrl)
        }
        return writableDatabase.insert("videos", null, cv)
    }

    // ✅ Corregido: ahora consulta la tabla correcta
    fun isMovieFavorite(title: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM movies_fav WHERE title = ?", arrayOf(title))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    // ✅ Corregido: usa la tabla correcta para favoritos
    fun toggleFavorite(movie: Movie) {
        if (isMovieFavorite(movie.title)) {
            writableDatabase.delete("movies_fav", "title = ?", arrayOf(movie.title))
        } else {
            addFavorite(movie)
        }
    }

    fun getAllMovies(): List<Movie> {
        val cursor = readableDatabase.rawQuery("SELECT * FROM videos", null)
        val out = mutableListOf<Movie>()
        if (cursor.moveToFirst()) {
            do {
                out += Movie(
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    year = cursor.getString(cursor.getColumnIndexOrThrow("year")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    authors = cursor.getString(cursor.getColumnIndexOrThrow("authors")),
                    videoUrl = cursor.getString(cursor.getColumnIndexOrThrow("videoUrl")),
                    imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"))
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return out
    }

    fun deleteAllMovies() {
        writableDatabase.delete("videos", null, null)
    }

    fun getAllFavorites(): List<Movie> {
        val cursor = readableDatabase.rawQuery("SELECT * FROM movies_fav", null)
        val out = mutableListOf<Movie>()
        if (cursor.moveToFirst()) {
            do {
                out += Movie(
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    year = cursor.getString(cursor.getColumnIndexOrThrow("year")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    authors = cursor.getString(cursor.getColumnIndexOrThrow("authors")),
                    videoUrl = cursor.getString(cursor.getColumnIndexOrThrow("videoUrl")),
                    imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"))
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return out
    }

    fun addFavorite(movie: Movie) {
        if (!isFavorite(movie)) {
            val cv = ContentValues().apply {
                put("title", movie.title)
                put("year", movie.year)
                put("description", movie.description)
                put("authors", movie.authors)
                put("videoUrl", movie.videoUrl)
                put("imageUrl", movie.imageUrl)
            }
            writableDatabase.insert("movies_fav", null, cv)
        }
    }

    fun removeFavorite(movie: Movie) {
        writableDatabase.delete("movies_fav", "title=? AND year=?", arrayOf(movie.title, movie.year))
    }

    fun isFavorite(movie: Movie): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT 1 FROM movies_fav WHERE title=? AND year=?",
            arrayOf(movie.title, movie.year)
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}
