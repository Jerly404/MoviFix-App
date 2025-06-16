package com.hcondor.movifix.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hcondor.movifix.model.Movie

class VideoDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "videos.db", null, 1) {

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
    }

    override fun onUpgrade(db: SQLiteDatabase, o: Int, n: Int) {
        db.execSQL("DROP TABLE IF EXISTS videos")
        onCreate(db)
    }

    fun insertMovie(m: Movie) = writableDatabase.insert("videos", null, ContentValues().apply {
        put("title", m.title)
        put("year", m.year)
        put("description", m.description)
        put("authors", m.authors)
        put("videoUrl", m.videoUrl)
        put("imageUrl", m.imageUrl)
    })

    fun getAllMovies(): List<Movie> {
        val c = readableDatabase.rawQuery("SELECT * FROM videos", null)
        val out = mutableListOf<Movie>()
        if (c.moveToFirst()) {
            do {
                out += Movie(
                    title = c.getString(c.getColumnIndexOrThrow("title")),
                    year = c.getString(c.getColumnIndexOrThrow("year")),
                    description = c.getString(c.getColumnIndexOrThrow("description")),
                    authors = c.getString(c.getColumnIndexOrThrow("authors")),
                    videoUrl = c.getString(c.getColumnIndexOrThrow("videoUrl")),
                    imageUrl = c.getString(c.getColumnIndexOrThrow("imageUrl"))
                )
            } while (c.moveToNext())
        }
        c.close()
        return out
    }
}
