package com.example.drawyourdream

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView?=null
    private var mImageButtonCurrentPaint:ImageButton?=null
    var customProgressDialog: Dialog? = null

   /* val openGalleryLuncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
      result ->
        if (result.resultCode== RESULT_OK && result.data !=null){
            val imageBackground:ImageView = findViewById(R.id.iv_background)
            imageBackground.setImageURI(result.data?.data)
        }
    }*/

     val  requestPermission:ActivityResultLauncher<Array<String>> = registerForActivityResult(
         ActivityResultContracts.RequestMultiplePermissions()){

         permissions ->permissions.entries.forEach{
             val permissionName=it.key
             val isGranted=it.value

         if (isGranted){
             Toast.makeText(this,
                 "Permission Granted now you can read the storage files",
             Toast.LENGTH_LONG)
                 .show()

             val pickIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


         }else{
             if (permissionName==Manifest.permission.READ_EXTERNAL_STORAGE){
                 Toast.makeText(this,
                     "Oops you just denied the permission",
                     Toast.LENGTH_LONG)
                     .show()
             }
         }
     }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView=findViewById(R.id.drawing_view)
        val ibBrush: ImageButton = findViewById(R.id.ib_brush)
        drawingView?.setSizeForBrush(20.toFloat())

        val  linearLayoutPaintColors=findViewById<LinearLayout>(R.id.ll_paint_colors)
        mImageButtonCurrentPaint=linearLayoutPaintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
        )

        //val ib_brush: ImageButton =findViewById(R.id.ib_brush)
        ibBrush.setOnClickListener(){
            showBrushSizeChooserDialog()
        }
        val ibGallery: ImageView =findViewById(R.id.ib_galary)
        ibGallery.setOnClickListener {
            requestStoragePermission()
        }


    }
    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush Size: ")
        val smallBtn:ImageButton=brushDialog.findViewById(R.id.ib_small_brush)
        smallBtn.setOnClickListener(){
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val mediumBtn:ImageButton=brushDialog.findViewById(R.id.ib_medium_brush)
        mediumBtn.setOnClickListener(){
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }
        val largeBtn:ImageButton=brushDialog.findViewById(R.id.ib_large_brush)
        largeBtn.setOnClickListener(){
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()


    }
    fun paintClicked(view: View){
       // Toast.makeText(this,"Clicked paint",Toast.LENGTH_LONG).show()
        if (view!== mImageButtonCurrentPaint){
            val imageButton=view as ImageButton
            val colorTag= imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
            )
            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)
            )
            mImageButtonCurrentPaint=view
        }
    }
    private fun isReadStorageAllowed(): Boolean {
        //Getting the permission status
        // Here the checkSelfPermission is
        /**
         * Determine whether <em>you</em> have been granted a particular permission.
         *
         * @param permission The name of the permission being checked.
         *
         */
        val result = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        )

        /**
         *
         * @return {@link android.content.pm.PackageManager#PERMISSION_GRANTED} if you have the
         * permission, or {@link android.content.pm.PackageManager#PERMISSION_DENIED} if not.
         *
         */
        //If permission is granted returning true and If permission is not granted returning false
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestStoragePermission(){
        //Todo 6: Check if the permission was denied and show rationale
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            //Todo 9: call the rationale dialog to tell the user why they need to allow permission request
            showRationalDialog("Kids Drawing App","Kids Drawing App " +
                    "needs to Access Your External Storage")
        }
        else {
            // You can directly ask for the permission.
            // Todo 7: if it has not been denied then request for permission
            //  The registered ActivityResultCallback gets the result of this request.
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

    }

    private fun showRationalDialog(
        title: String,
        message: String,

    ){
        val builder:AlertDialog.Builder=AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){
                dialog,_ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}