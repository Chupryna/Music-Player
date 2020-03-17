package com.globallogic.musicplayer

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.globallogic.musicplayer.data.AudioRepository
import com.globallogic.musicplayer.data.Database
import com.globallogic.musicplayer.manager.SharedPreferenceManager
import com.globallogic.musicplayer.ui.home.HomeViewModel
import com.globallogic.musicplayer.ui.home.favouriteTracks.FavouriteTracksViewModel
import com.globallogic.musicplayer.ui.home.tracksList.TrackListViewModel
import com.globallogic.musicplayer.ui.player.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

/**
 * @author Anatolii Chupryna.
 */

fun initInjection(application: Application) {

	val viewModels = module {
		viewModel { PlayerViewModel(get()) }
		viewModel { HomeViewModel(get()) }
		viewModel { TrackListViewModel(get(), get<Context>().contentResolver) }
		viewModel { FavouriteTracksViewModel(get()) }
	}

	val repository = module {
		single {
			Room.databaseBuilder(get(), Database::class.java, "database")
				.build()
		}
		single { AudioRepository(get()) }
	}

	val service = module {
		single { SharedPreferenceManager(get()) }
	}

	startKoin {
		androidLogger(Level.ERROR)
		androidContext(application)
		modules(viewModels, repository, service)
	}
}