package com.hcondor.movifix.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hcondor.movifix.model.Movie

class VideoDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "videos.db", null, 2) {

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


    override fun onUpgrade(db: SQLiteDatabase, oldV: Int, newV: Int) {
        db.execSQL("DROP TABLE IF EXISTS videos")
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

    fun getAllMovies(): List<Movie> {
        val cursor = readableDatabase.rawQuery("SELECT * FROM videos", null)
        val out = mutableListOf<Movie>()
        if (cursor.moveToFirst()) {
            do {
                out += Movie(
                    title       = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    year        = cursor.getString(cursor.getColumnIndexOrThrow("year")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    authors     = cursor.getString(cursor.getColumnIndexOrThrow("authors")),
                    videoUrl    = cursor.getString(cursor.getColumnIndexOrThrow("videoUrl")),
                    imageUrl    = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"))
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return out
    }
    fun deleteAllMovies() {
        val db = writableDatabase
        db.delete("videos", null, null)
        db.close()
    }

}
