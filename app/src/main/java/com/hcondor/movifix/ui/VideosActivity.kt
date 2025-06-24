package com.hcondor.movifix.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hcondor.movifix.R
import com.hcondor.movifix.VideoPlayerActivity
import com.hcondor.movifix.database.VideoDatabaseHelper
import com.hcondor.movifix.model.Movie
import java.util.Locale

class VideosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: VideoAdapter
    private lateinit var dbHelper: VideoDatabaseHelper
    private lateinit var searchView: SearchView
    private lateinit var spinnerCategory: Spinner

    private var allMovies: List<Movie> = emptyList()
    private var currentCategory: String = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        recyclerView = findViewById(R.id.rvVideoMovies)
        searchView = findViewById(R.id.searchView)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        dbHelper = VideoDatabaseHelper(this)

        setupBottomNavigation()
        setupVoiceSearch()

        // 1. Inserta las películas si es necesario
        insertInitialMoviesIfNeeded()

        // 2. Carga todas las películas desde la base de datos
        allMovies = dbHelper.getAllMovies()

        // 3. Configura el adapter
        setUpAdapter(allMovies)

        // 4. Configura búsqueda y filtro por categoría (debe ir después de tener datos)
        setupSearchView()
        setupSpinner()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_video

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_video -> true
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun insertInitialMoviesIfNeeded() {
        if (dbHelper.getAllMovies().isEmpty()) {
            val initialMovies = listOf(
                Movie(
                    title = "Inception",
                    year = "2010",
                    description = "Un ladrón roba secretos corporativos mediante tecnología de sueños compartidos.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    imageUrl = "https://i.pinimg.com/736x/b0/ae/a4/b0aea49646879a043ad9f6ec3002e99f.jpg",
                    category = "Películas"
                ),
                Movie(
                    title = "Interstellar",
                    year = "2014",
                    description = "Un grupo de astronautas viaja por un agujero de gusano para salvar la humanidad.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg",
                    category = "Películas"
                ),
                Movie(
                    title = "The Dark Knight",
                    year = "2008",
                    description = "Batman lucha contra el Joker, un criminal que siembra el caos en Gotham.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                    category = "Películas"
                ),
                Movie(
                    title = "The Matrix",
                    year = "1999",
                    description = "Un hacker descubre que la realidad es una simulación controlada por máquinas.",
                    authors = "Lana Wachowski, Lilly Wachowski",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
                    category = "Películas"
                ),
                Movie(
                    title = "Avengers: Endgame",
                    year = "2019",
                    description = "Los Vengadores se reúnen para derrotar a Thanos y restaurar el universo.",
                    authors = "Anthony Russo, Joe Russo",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/ulzhLuWrPK07P1YkdWQLZnQh1JL.jpg",
                    category = "Películas"
                ),
                Movie(
                    title = "Joker",
                    year = "2019",
                    description = "La historia del origen de Joker, el icónico villano de Batman.",
                    authors = "Todd Phillips",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
                    category = "Películas"
                ),
                Movie(
                    title = "Parasite",
                    year = "2019",
                    description = "Una familia pobre se infiltra en la vida de una familia rica, con consecuencias inesperadas.",
                    authors = "Bong Joon-ho",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
                    category = "Películas"
                ),
                Movie(
                    title = "Spider-Man: No Way Home",
                    year = "2021",
                    description = "Peter Parker pide ayuda a Doctor Strange, pero algo sale mal.",
                    authors = "Jon Watts",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                    category = "Películas"
                ),

                // Documentales
                Movie(
                    title = "Planeta Tierra II",
                    year = "2016",
                    description = "Explora la belleza del mundo natural con tecnología de filmación innovadora.",
                    authors = "BBC",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                    imageUrl = "https://plus.unsplash.com/premium_photo-1713175931010-fea2465cf4ff?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    category = "Documentales"
                ),
                Movie(
                    title = "Nuestro Planeta: Selvas",
                    year = "2019",
                    description = "Una mirada profunda a los ecosistemas más ricos y vulnerables del planeta.",
                    authors = "Netflix",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    imageUrl = "https://plus.unsplash.com/premium_photo-1749538558340-51e118e0bd55?q=80&w=1664&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    category = "Documentales"
                ),
                Movie(
                    title = "Inside Bill’s Brain",
                    year = "2019",
                    description = "Un retrato íntimo del fundador de Microsoft, Bill Gates.",
                    authors = "Netflix",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    imageUrl = "https://images.unsplash.com/photo-1602046274544-5f52668372dc?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    category = "Documentales"
                ),

// Podcasts de motivación
                Movie(
                    title = "Vive Mejor",
                    year = "2023",
                    description = "Consejos diarios para alcanzar tu máximo potencial.",
                    authors = "MotivaPodcast",
                    videoUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
                    imageUrl = "https://is1-ssl.mzstatic.com/image/thumb/Podcasts211/v4/45/58/34/4558340f-2382-ffcb-d340-328c070ae5fe/mza_16848730076334539440.jpg/540x540bb.webp",
                    category = "Podcasts"
                ),
                Movie(
                    title = "Actitud Positiva",
                    year = "2022",
                    description = "Historias de superación, disciplina y crecimiento personal.",
                    authors = "Vida Inspiradora",
                    videoUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
                    imageUrl = "https://static.wixstatic.com/media/a30cb0_f68f199d5959411386f24e1ea62faea0~mv2.png/v1/fill/w_503,h_503,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/a30cb0_f68f199d5959411386f24e1ea62faea0~mv2.png",
                    category = "Podcasts"
                ),
                Movie(
                    title = "Éxito Diario",
                    year = "2021",
                    description = "Episodios breves para mantener la motivación encendida cada día.",
                    authors = "Éxito360",
                    videoUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
                    imageUrl = "https://is1-ssl.mzstatic.com/image/thumb/Podcasts115/v4/69/f7/c1/69f7c1e8-fcdc-9ead-977a-9cc32b380688/mza_15760735204413161512.jpg/540x540bb.webp",
                    category = "Podcasts"
                ),

// Series
                Movie(
                    title = "Breaking Bad",
                    year = "2008",
                    description = "Un profesor de química se convierte en fabricante de metanfetamina.",
                    authors = "Vince Gilligan",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                    imageUrl = "https://www.themoviedb.org/t/p/w1280/ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg",
                    category = "Series"
                ),
                Movie(
                    title = "Dark",
                    year = "2017",
                    description = "Misterios oscuros y viajes en el tiempo en un pueblo alemán.",
                    authors = "Baran bo Odar",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w1280/1DLjjvSWMYo17B7wuz6YikB96hH.jpg",
                    category = "Series"
                ),
                Movie(
                    title = "Black Mirror",
                    year = "2011",
                    description = "Historias independientes que exploran la tecnología y su lado oscuro.",
                    authors = "Charlie Brooker",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w1280/seN6rRfN0I6n8iDXjlSMk1QjNcq.jpg",
                    category = "Series"
                )

            )
            for (movie in initialMovies) {
                dbHelper.insertMovie(movie)
            }
        }
    }
    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        val searchEditText = searchView.findViewById<android.widget.EditText>(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant))
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterMovies()
                return true
            }
        })
    }

    private fun setupSpinner() {
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                currentCategory = parent.getItemAtPosition(position).toString()
                setupVoiceSearch()
                filterMovies()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun filterMovies() {
        val query = searchView.query.toString().lowercase().trim()
        val filtered = allMovies.filter { movie ->
            val matchesTitle = movie.title.lowercase().contains(query)
            val matchesCategory = currentCategory == "Todos" || movie.category == currentCategory
            matchesTitle && matchesCategory
        }
        movieAdapter.updateList(filtered)
    }

    private fun setUpAdapter(movieList: List<Movie>) {
        movieAdapter = VideoAdapter(
            movieList,
            onMoreInfoClick = { movie ->
                val intent = Intent(this, MovieDetailActivity::class.java).apply {
                    putExtra("title", movie.title)
                    putExtra("year", movie.year)
                    putExtra("description", movie.description)
                    putExtra("authors", movie.authors)
                    putExtra("videoUrl", movie.videoUrl)
                    putExtra("imageUrl", movie.imageUrl)
                }
                startActivity(intent)
            },
            onFavoriteClick = { movie ->
                dbHelper.toggleFavorite(movie)
                movieAdapter.notifyDataSetChanged()
                Toast.makeText(this, "${movie.title} favorito actualizado", Toast.LENGTH_SHORT).show()
            },
            isFavoriteChecker = { movie ->
                dbHelper.isMovieFavorite(movie.title)
            },
            onVideoClick = { movie ->
                val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                    putExtra("videoUrl", movie.videoUrl)
                }
                startActivity(intent)
            }
        )
        recyclerView.adapter = movieAdapter
    }
    private val REQUEST_CODE_VOICE = 1

    private fun setupVoiceSearch() {
        val btnVoice = findViewById<ImageButton>(R.id.btnVoiceSearch)
        btnVoice.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")

            try {
                startActivityForResult(intent, REQUEST_CODE_VOICE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Tu dispositivo no admite búsqueda por voz", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VOICE && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = result?.get(0) ?: ""
            val searchView = findViewById<SearchView>(R.id.searchView)
            searchView.setQuery(recognizedText, true) // Activa la búsqueda con el texto reconocido
        }
    }

}
