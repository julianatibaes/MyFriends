package tibaes.com.myfriends.view

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_new_friend.*
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import tibaes.com.myfriends.R
import tibaes.com.myfriends.entity.Friend

class NewFriendActivity : AppCompatActivity() {

    private var image_uri: Uri? = null
    private var mCurrentPhotoPath: String = ""
    lateinit var friend: Friend
    var menu: Menu? = null

    private var notificationManager: NotificationManager? = null

    companion object {
        // image pick code
        private val REQUEST_IMAGE_GARELLY = 1000
        private val REQUEST_IMAGE_CAPTURE = 2000

        const val EXTRA_REPLY = "view.REPLY"
        const val EXTRA_DELETE = "view.Delete"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_friend)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // ============ [ BEGIN NOTIFICATION ] ===============================

        notificationManager =
                getSystemService(
                        Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
                "tibaes.com.myfriends.news",
                "Notify example",
                "Example Description Channel")

        // ============ [ END NOTIFICATION ] ===============================

        fAddFriend?.setOnClickListener {
            fAddFriend.setOnCreateContextMenuListener { menu, v, menuInfo ->
                menu.add(Menu.NONE, 1, Menu.NONE, "Escolher foto")
                menu.add(Menu.NONE, 2, Menu.NONE, "Tirar foto")
            }
        }

        // ============ [ BEGIN RECEIVE OBJECT IF IT EXISTS ] ===============================

        val intent = intent
        try {
            friend = intent.getSerializableExtra(EXTRA_REPLY) as Friend
            friend.let {
                etNome.setText(friend.fName)
                etFone.setText(friend.fPhone)
                etEmail.setText(friend.fEmail)
                etSite.setText(friend.fSite)
                etAddress.setText(friend.fAddress)
            }
            val menuItem = menu?.findItem(R.id.menu_friend_delte)
            menuItem?.isVisible = true

        } catch (exception: Exception) {
            val menuItem = menu?.findItem(R.id.menu_friend_delte)
            menuItem?.isVisible = false
        }
        // ============ [ END RECEIVE OBJECT IF IT EXISTS ] ===============================
    }

    // ============ [ BEGIN WORK WITH IMAGES ] ===============================

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GARELLY)
    }

    private fun getPermissionImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_GARELLY)
            } else {
                // permission granted
                pickImageFromGallery()
            }
        } else {
            // system < M
            pickImageFromGallery()
        }
    }

    private fun takePicture() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "nova imagem")
        values.put(MediaStore.Images.Media.DESCRIPTION, "imagem da camera")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

        image_uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            mCurrentPhotoPath = image_uri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun getPermissionTakePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_CAPTURE)
            } else {
                // permission granted
                takePicture()
            }
        } else {
            // system < M
            //takePicture()
        }
    }

    // ============ [ END WORK WITH IMAGES ] ===============================

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_IMAGE_GARELLY -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) pickImageFromGallery()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePicture()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GARELLY) imgNewFriend.setImageURI(data?.data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) imgNewFriend.setImageURI(image_uri)

    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            1 -> getPermissionImageFromGallery()
            2 -> getPermissionTakePicture()
        }
        return super.onContextItemSelected(item)
    }

// ============ [ BEGIN MENU ] ===============================

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_friend, menu)
        try {
            friend.let {
                val menuItem = menu?.findItem(R.id.menu_friend_delte)
                menuItem?.isVisible = true
            }
        } catch (e: Exception) {
            Log.e("TAG", e.message.toString())
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else if (item?.itemId == R.id.menu_friend_save) {
            val replyIntent = Intent()

            if (etNome.text.isNullOrEmpty()) {
                Toast.makeText(this, "Insira ao menos o nome", Toast.LENGTH_SHORT).show()
                etNome.requestFocus()
            } else if ((::friend.isInitialized) && (friend.fId > 0)) {
                friend.fEmail = etEmail.text.toString()
                friend.fAddress = etAddress.text.toString()
                friend.fPhone = etFone.text.toString()
                friend.fSite = etSite.text.toString()

            } else {
                friend = Friend(
                        fName = etNome.text.toString(),
                        fEmail = etEmail.text.toString(),
                        fAddress = etAddress.text.toString(),
                        fPhone = etFone.text.toString(),
                        fSite = etSite.text.toString()
                )
            }
            //val friendViewModel = ViewModelProviders.of(this).get(FriendViewModel::class.java)
            //friendViewModel.insert(friend)
            //
            replyIntent.putExtra(EXTRA_REPLY, friend)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
            true
        } else if (item?.itemId == R.id.menu_friend_delte) {
            val replyIntent = Intent()
            replyIntent.putExtra(EXTRA_DELETE, friend)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    // ============ [ END MENU ] ===============================

    // ============ [ BEGIN NOTIFICATION CHANNEL ] ===============================
    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    // ============ [ END NOTIFICATION CHANNEL ] ===============================

}

