package com.example.f1app

import androidx.lifecycle.ViewModel
import com.example.f1app.news.NewsAdapter
import com.example.f1app.ui.favorites.FavoritesAdapter
import com.example.f1app.ui.favAndShake.ShareAdapter

class SharedViewModel : ViewModel() {
    var accountName: String = ""
    var googleIdToken: String = ""
    var googleEmail: String = ""
    var formulaOneNewsAdapter: NewsAdapter? = null
    var tennisNewsAdapter: NewsAdapter? = null
    var cyclingNewsAdapter: NewsAdapter? = null
    var favoritesAdapter: FavoritesAdapter? = null
    var shareAdapter: ShareAdapter? = null
}