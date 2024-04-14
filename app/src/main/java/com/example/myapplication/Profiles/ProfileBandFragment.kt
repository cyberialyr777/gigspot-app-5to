package com.example.myapplication.Profiles

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.FollowBandActivity
import com.example.myapplication.LoginActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.databinding.FragmentProfileBandBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.UUID


class ProfileBandFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var _binding: FragmentProfileBandBinding
    private val binding get() = _binding
    val storage = FirebaseStorage.getInstance()

    val TAG = "ProfileBandFragment"
    private var selectedImageBUri: Uri? = Uri.parse("")
    private var selectedImageBUrl: String? = ""
    private var selectedImagePUri: Uri? = Uri.parse("")
    private var selectedImagePUrl: String? = ""

    companion object {
        const val IMAGE_REQUEST_CODE_1 = 1
        const val IMAGE_REQUEST_CODE_2 = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentProfileBandBinding.inflate(inflater, container, false)
        val view = binding.root
        datosUsuario()
        datosFollow()
        datosConciertos()

        binding.button7.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileBandActivity::class.java))
        }

        binding.button6.setOnClickListener{
            closeSession()
        }

        binding.button12.setOnClickListener {
            pickImageGallery2()
        }

        binding.button13.setOnClickListener {
            pickImageGallery1()
        }

        binding.textView5.setOnClickListener{
            startActivity(Intent(context, FollowBandActivity::class.java))
        }
        return view
    }

    private fun datosUsuario(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
        databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(BandaModelo::class.java)
                    binding.nombre.text = usuario?.bandName
                    val imageBackUrl = usuario?.imageBack
                    if (!imageBackUrl.isNullOrEmpty()) {
                        Picasso.get().load(imageBackUrl).into(binding.imageView)
                    }

                    val imageProfileUrl = usuario?.imageProfile
                    if (!imageProfileUrl.isNullOrEmpty()) {
                        Picasso.get().load(imageProfileUrl).into(binding.imageButton3)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun datosConciertos(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("eventos")
        databaseReference.orderByChild("emailBand").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(BandaModelo::class.java)
                    val count = dataSnapshot.childrenCount

                    binding.textView13.text = count.toString()

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun datosFollow(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("follows")
        databaseReference.orderByChild("emailBand").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(BandaModelo::class.java)
                    val count = dataSnapshot.childrenCount

                    binding.textView14.text = count.toString()

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun pickImageGallery1() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE_1)
    }



    private fun pickImageGallery2() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE_2)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val storageRef = storage.reference.child("imagesBack").child("${UUID.randomUUID()}")
        val storageRef2 = storage.reference.child("imagesProfile").child("${UUID.randomUUID()}")

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                EditProfileBandActivity.IMAGE_REQUEST_CODE_1 -> {
                    // Manejar el resultado de la primera imagen
                    selectedImageBUri = data?.data
                    binding.imageView.setImageURI(selectedImageBUri)

                    storageRef.putFile(selectedImageBUri!!).addOnSuccessListener { taskSnapshot ->
                            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                val downloadUrl = downloadUri.toString()
                                selectedImageBUrl = downloadUrl
                                Log.d("UriImage1", "URI de la imagen 1: $selectedImageBUri")

                                val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
                                databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        for (snapshot in dataSnapshot.children) {
                                            val user = snapshot.getValue(BandaModelo::class.java)
                                            if (user != null && user.email == auth.currentUser?.email) {
                                                val actualizacionMap = hashMapOf<String, Any?>(
                                                    "imageBack" to selectedImageBUrl,
                                                )
                                                snapshot.ref.updateChildren(actualizacionMap)
                                                    .addOnSuccessListener {
                                                        Log.d("UriImage1", "URI de la imagen 1: $selectedImageBUrl")
                                                        Toast.makeText(requireContext(), "Successfully update", Toast.LENGTH_SHORT).show()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                        }
                }
                EditProfileBandActivity.IMAGE_REQUEST_CODE_2 -> {
                    // Manejar el resultado de la segunda imagen
                    selectedImagePUri = data?.data
                    binding.imageButton3.setImageURI(selectedImagePUri)
                    Log.d("UriImage2", "URI de la imagen 2: $selectedImagePUri")

                    storageRef2.putFile(selectedImagePUri!!).addOnSuccessListener { taskSnapshot ->
                        storageRef2.downloadUrl.addOnSuccessListener { downloadUri ->
                            val downloadUrl = downloadUri.toString()
                            selectedImagePUrl = downloadUrl
                            Log.d("UriImage1", "URI de la imagen 1: $selectedImagePUri")

                            val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
                            databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (snapshot in dataSnapshot.children) {
                                        val user = snapshot.getValue(BandaModelo::class.java)
                                        if (user != null && user.email == auth.currentUser?.email) {
                                            val actualizacionMap = hashMapOf<String, Any?>(
                                                "imageProfile" to selectedImagePUrl,
                                            )
                                            snapshot.ref.updateChildren(actualizacionMap)
                                                .addOnSuccessListener {
                                                    Log.d("UriImage1", "URI de la imagen 1: $selectedImagePUrl")
                                                    Toast.makeText(requireContext(), "Successfully update", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                    }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun closeSession(){
        auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        Toast.makeText(requireContext(), "Logout Successfully", Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart: ")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG,"onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop: ")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy :")
    }
}


