package com.derich.bigfoot.ui.screens
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.ui.bottomnavigation.memberDetails
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

private const val FILE_PROVIDER_AUTHORITY = "com.derich.bigfoot.fileprovider"
private lateinit var transactionsVM: TransactionsViewModel
private lateinit var fullMemberDetails: MemberDetails

@Composable
fun ImageUploaderScreen(transactionsViewModel: TransactionsViewModel) {
    transactionsVM = transactionsViewModel
    fullMemberDetails = memberDetails
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the selected image URI
        if (uri != null) {
            // Call your image size reduction and upload logic here
            imageUploader(reduceAndUploadImage(uri, context), context)
        } else {
            // Handle the case where the user did not select an image
            Toast.makeText(context, "Error Selecting the Image", Toast.LENGTH_SHORT).show()
        }
    }

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
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
    }
}
fun reduceAndUploadImage(originalUri: Uri, context: Context): Uri {
    // Load the selected image into a Bitmap using an alternative method for lower API levels
    val originalBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, originalUri))
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, originalUri)
    }

    // Specify the desired maximum width and height for the reduced image
    val maxWidth = 800
    val maxHeight = 800

    // Calculate the new dimensions while maintaining the aspect ratio
    val scale = calculateScaleFactor(originalBitmap.width, originalBitmap.height, maxWidth, maxHeight)
    val newWidth = (originalBitmap.width * scale).toInt()
    val newHeight = (originalBitmap.height * scale).toInt()

    // Create a new Bitmap with reduced dimensions
    val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

    // Create a temporary file using File.createTempFile
    val reducedFile = File.createTempFile("reduced_image", ".jpg", context.cacheDir).apply {
        setReadable(true, false) // Make the file world-readable
    }

    // Save the resized Bitmap to the temporary file
    val outputStream = FileOutputStream(reducedFile)
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    outputStream.close()

    // Return the URI of the reduced image file
    return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, reducedFile)
}

fun calculateScaleFactor(originalWidth: Int, originalHeight: Int, maxWidth: Int, maxHeight: Int): Float {
    val widthScale = maxWidth.toFloat() / originalWidth.toFloat()
    val heightScale = maxHeight.toFloat() / originalHeight.toFloat()
    return if (widthScale < heightScale) widthScale else heightScale
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
    }.addOnFailureListener {
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
