package com.leeday.capstone_1

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class WritePhoto : ComponentActivity() {

    private lateinit var imageView: ImageView
    private lateinit var pickImageButton: AppCompatButton
    private lateinit var uploadButton: AppCompatButton
    private var selectedImageUri: Uri? = null

    private lateinit var apiService: ApiService
    private lateinit var globalVariable: GlobalVariable

    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_photodiary)

        imageView = findViewById(R.id.uploaded_image)
        pickImageButton = findViewById(R.id.btn_pick_image)
        uploadButton = findViewById(R.id.save_diary)

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                imageView.setImageURI(selectedImageUri)
            }
        }

        pickImageButton.setOnClickListener {
            requestPermissions()
        }

        uploadButton.setOnClickListener {
            selectedImageUri?.let { uri ->
                uploadImageToServer(uri)
            } ?: run {
                Toast.makeText(this, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        globalVariable = application as GlobalVariable
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun uploadImageToServer(imageUri: Uri) {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri, "r", null)
        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(imageUri))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()

            // 여기에서 MultipartBody.Part를 생성합니다
            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestBody)

            // Retrofit을 사용하여 이미지를 업로드합니다.
            apiService.getphoto("Bearer ${globalVariable.accesstoken}", body).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        // Handle success
                        val responseBody = response.body()
                        // TODO: Do something with the response body
                    } else {
                        // Handle request error
                        Toast.makeText(applicationContext, "Upload failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Handle failure
                    Toast.makeText(applicationContext, "Upload error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } ?: run {
            Toast.makeText(this, "File descriptor could not be opened", Toast.LENGTH_LONG).show()
        }
    }


    // ContentResolver를 사용하여 파일 이름을 얻는 함수
    fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }


    private fun getRealPathFromURI(uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                return if ("primary".equals(split[0], ignoreCase = true)) {
                    "${Environment.getExternalStorageDirectory()}/${split[1]}"
                } else {
                    null
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.parseLong(id)
                )
                return getDataColumn(this, contentUri, null, null)
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(this, contentUri, selection, selectionArgs)
            }
        }
        // MediaStore (and general)
        else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) return uri.lastPathSegment
            return getDataColumn(this, uri, null, null)
        }
        // File
        else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


    //권한 요청
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            openGalleryForImage()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                Toast.makeText(this, "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
            requestPermission()
        }
    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setMessage("이 앱에서 사진을 업로드하려면 갤러리 접근 권한이 필요합니다. 권한을 허용해 주세요.")
            .setPositiveButton("확인") { dialog, which ->
                requestPermission()
            }
            .setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), PERMISSION_REQUEST_CODE)
    }

    private fun openGalleryForImage() {
        pickImageLauncher.launch("image/*")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryForImage()
            } else {
                Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }
}
