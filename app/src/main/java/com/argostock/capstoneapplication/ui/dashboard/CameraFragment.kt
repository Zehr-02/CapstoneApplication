package com.argostock.capstoneapplication.ui.dashboard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.argostock.capstoneapplication.databinding.FragmentCameraBinding
import java.io.File

private const val REQUEST_CODE = 26
private const val FILE_NAME = "photo.jpg"

class CameraFragment : Fragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private var _binding: FragmentCameraBinding? = null
    private lateinit var photoFile: File

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraViewModel =
            ViewModelProvider(this).get(CameraViewModel::class.java)

        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        binding.buttonCapture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            val fileProvider = FileProvider.getUriForFile(requireContext(),"com.argostock.capstoneapplication.fileprovider",photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        }

        return binding.root
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg",storageDirectory)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            //val takenImage = data?.extras?.get("data") as Bitmap
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.cameraPreview.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}