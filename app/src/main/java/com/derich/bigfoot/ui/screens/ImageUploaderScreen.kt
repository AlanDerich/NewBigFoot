package com.derich.bigfoot.ui.screens
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

private const val FILE_PROVIDER_AUTHORITY = "com.derich.bigfoot.fileprovider"
private lateinit var transactionsVM: TransactionsViewModel
private lateinit var fullMemberDetails: MemberDetails

@Composable
fun ImageUploaderScreen(memberDetails: MemberDetails, transactionsViewModel: TransactionsViewModel) {
    transactionsVM = transactionsViewModel
    fullMemberDetails = memberDetails
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val context = LocalContext.current
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the selected image URI
            if (uri != null) {
                // Call your image upload logic here
                imageUploader(uri,context)
            } else {
                // Handle the case where the user did not select an image
                Toast.makeText(context, "Error Selecting the Image", Toast.LENGTH_SHORT).show()
            }
        }

    val takePicture =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                // Handle the captured image
                // Call your image upload logic here
//                imageUploader(uri, context)
            } else {
                // Handle the case where image capture failed
                Toast.makeText(context, "Image capture failed!", Toast.LENGTH_SHORT).show()
            }
        }

    Column {
        // UI elements here
        Button(
            onClick = {
                // Launch the image selection activity
                getContent.launch("image/*")
            }
        ) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            Text("Select Image")
        }

        Button(
            onClick = {
                // Launch the camera to capture an image
                takePicture.launch(createImageCaptureUri(context))
            }
        ) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            Text("Take Picture")
        }
    }
}

@Throws(IOException::class)
private fun createImageCaptureUri(context: Context): Uri {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_$timeStamp"
    val storageDir: File? = ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_PICTURES).firstOrNull()

    return if (storageDir != null) {
        val imageFile = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir      /* directory */
        )
        FileProvider.getUriForFile(
            context,
            FILE_PROVIDER_AUTHORITY,
            imageFile
        )
    } else {
        throw IOException("Could not create image file.")
    }
}

fun imageUploader(
    uri: Uri,
    context: Context
) {
    FirebaseApp.initializeApp(context)
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val imageUri: Uri = uri
    val imagesRef = storageRef
        .child("profilepics/${fullMemberDetails.firstName+ "_" +fullMemberDetails.secondName+ "_"+fullMemberDetails.surname}")

    val uploadTask = imagesRef.putFile(imageUri)
    uploadTask.addOnSuccessListener {
        // Image uploaded successfully
        // You can get the download URL if needed
        Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
        imagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
            val downloadUrl = downloadUri.toString()
            // Handle the download URL
            updateMemberDetails(context, downloadUrl)
        }
    }.addOnFailureListener { exception ->
        // Handle unsuccessful uploads
        Toast.makeText(context, "Error occurred while uploading the Image", Toast.LENGTH_SHORT).show()
    }
}

fun updateMemberDetails(context: Context, newUserPic: String) {
    runBlocking {
        val task = transactionsVM.updateProfilePic(fullMemberDetails.phoneNumber,
            fullMemberDetails.fullNames,newUserPic)
        task.addOnSuccessListener {
            //do this if the upload was successful
            Toast.makeText(context, "The profile picture was updated successfully!", Toast.LENGTH_SHORT).show()

        }
        task.addOnFailureListener{
            //do this if the upload failed due to some reason
            Toast.makeText(context, "An error occurred during upload ${it.message.toString()}!", Toast.LENGTH_SHORT).show()
        }
    }
}
