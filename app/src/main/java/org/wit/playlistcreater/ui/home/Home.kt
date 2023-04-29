package org.wit.playlistcreater.ui.home


import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.playlistcreater.R
import org.wit.playlistcreater.databinding.HomeBinding
import org.wit.playlistcreater.databinding.NavHeaderBinding
import org.wit.playlistcreater.firebase.FirebaseImageManager
import org.wit.playlistcreater.firebaseui.FirebaseUIAuthManager
import org.wit.playlistcreater.ui.auth.LoggedInViewModel
import org.wit.playlistcreater.ui.auth.Login
import org.wit.playlistcreater.ui.maps.MapsViewModel
import org.wit.playlistcreater.utils.checkLocationPermissions
import org.wit.playlistcreater.utils.isPermissionGranted
import org.wit.playlistcreater.utils.readImageUri
import org.wit.playlistcreater.utils.showImagePicker
import timber.log.Timber


class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding: HomeBinding
    private lateinit var navHeaderBinding: NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var headerView: View
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>
    private val mapsViewModel: MapsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        FirebaseUIAuthManager.auth = FirebaseAuth.getInstance()
        loggedInViewModel = ViewModelProvider(this)[LoggedInViewModel::class.java]
        loggedInViewModel.liveFirebaseUser.value = FirebaseUIAuthManager.auth.currentUser

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.createPlaylistFragment,
                R.id.playlistFragment,
                R.id.profileFragment,
                R.id.publicPlaylistsFragment,
                R.id.mapsFragment,
                R.id.songMenuFragment,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
        initNavHeader()

        navHeaderBinding.navHeaderImage.setOnClickListener {
            showImagePicker(intentLauncher)
        }
        registerImagePickerCallback()

        if (checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }

        FirebaseUIAuthManager.auth.currentUser?.let {
            FirebaseImageManager.checkStorageForExistingProfilePic(
                it.uid
            )
        }

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }


    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i(
                                "DX registerPickerCallback() ${
                                    readImageUri(
                                        result.resultCode,
                                        result.data
                                    ).toString()
                                }"
                            )
                            FirebaseImageManager
                                .updateUserImage(
                                    loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    navHeaderBinding.navHeaderImage,
                                    true
                                )
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun initNavHeader() {
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)

    }

    public override fun onStart() {
        super.onStart()

        Timber.i("EMAIL IS : %s", loggedInViewModel.liveFirebaseUser.value?.email)
        loggedInViewModel.liveFirebaseUser.observe(this) { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
            loggedInViewModel.loadAllSongs()
            Handler().postDelayed({
                loggedInViewModel.getAllPlaylists()
            }, 1000)
        }

        loggedInViewModel.loggedOut.observe(this) { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        }
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.signOut()
        FirebaseUIAuthManager.auth.signOut()
        finish()
    }


    private fun updateNavHeader(currentUser: FirebaseUser) {

        FirebaseImageManager.imageUri.observe(this) { result ->
            if (result == Uri.EMPTY) {
                if (currentUser.photoUrl != null) {
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.navHeaderImage,
                        false
                    )

                } else {
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.ic_baseline_library_music_24,
                        navHeaderBinding.navHeaderImage
                    )
                }
            } else {
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    navHeaderBinding.navHeaderImage, false
                )
            }
        }


        navHeaderBinding.navHeaderEmail.text = currentUser.email
        if (currentUser.displayName != null)
            navHeaderBinding.navHeaderName.text = currentUser.displayName


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}