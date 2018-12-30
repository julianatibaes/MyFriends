package tibaes.com.myfriends.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_new_friend.*
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import tibaes.com.myfriends.R
import tibaes.com.myfriends.entity.Friend
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NewFriendActivity : AppCompatActivity() {

    private var mCurrentPhotoPath: String = ""
    lateinit var friend: Friend
    var menu: Menu? = null

    companion object {
        private val REQUEST_IMAGE_GARELLY = 1000
        private val REQUEST_IMAGE_CAPTURE = 2000

        const val EXTRA_REPLY = "view.REPLY"
        const val EXTRA_DELETE = "view.Delete"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_friend)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fAddFriend.setOnClickListener {
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
                etFone.setText(friend.phone)
                etEmail.setText(friend.email)
                etSite.setText(friend.site)
                etAddress.setText(friend.address)
                imgNewFriend.setImageURI(Uri.parse(friend.photoPath))
                mCurrentPhotoPath = friend.photoPath
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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storeDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storeDir
        ).apply {
            mCurrentPhotoPath = absolutePath
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                }catch (ex: IOException){
                    null
                }
                photoFile?.also{
                    val photoUri: Uri = FileProvider.getUriForFile(
                            this,
                            "tibaes.com.myfriends.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
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
        }
    }

    private fun getImageRealPath(uri: Uri?) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            val movieToFirst = cursor.moveToFirst()
            if (movieToFirst) {
                var columnName = MediaStore.Images.Media.DATA
                uri.let {
                    if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) columnName = MediaStore.Images.Media.DATA
                    else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) columnName = MediaStore.Audio.Media.DATA
                    else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) columnName = MediaStore.Video.Media.DATA
                }
                val imageColumnIndex = cursor.getColumnIndex(columnName)
                mCurrentPhotoPath = cursor.getString(imageColumnIndex)
            }
        }
    }

    // ============ [ END WORK WITH IMAGES ] ===============================

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_IMAGE_GARELLY -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                else pickImageFromGallery()
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                else takePicture()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GARELLY) {
            imgNewFriend.setImageURI(data?.data)
            getImageRealPath(data?.data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            imgNewFriend.setImageURI(Uri.parse(mCurrentPhotoPath))
        }
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
            }  else {
                if ((::friend.isInitialized) && (friend.fId > 0)) {
                    friend.fName = etNome.text.toString()
                    friend.email = etEmail.text.toString()
                    friend.address = etAddress.text.toString()
                    friend.phone = etFone.text.toString()
                    friend.site = etSite.text.toString()

                    friend.photoPath = mCurrentPhotoPath

                    replyIntent.putExtra(EXTRA_REPLY, friend)
                } else {
                    friend = Friend(
                            fName = etNome.text.toString(),
                            email = etEmail.text.toString(),
                            address = etAddress.text.toString(),
                            phone = etFone.text.toString(),
                            site = etSite.text.toString(),
                            photoPath = mCurrentPhotoPath
                    )
                    replyIntent.putExtra(EXTRA_REPLY, friend)
                }

                setResult(Activity.RESULT_OK, replyIntent)
                finish()
                true
            }
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

}

