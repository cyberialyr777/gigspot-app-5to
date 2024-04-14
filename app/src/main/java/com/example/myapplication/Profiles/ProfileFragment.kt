package com.example.myapplication.Profiles


import android.app.Activity
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.myapplication.AddBandFragment
import com.example.myapplication.BoughtTicketsActivity
import com.example.myapplication.FollowUserActivity
import com.example.myapplication.LoginActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.UsuariosModelos
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.databinding.FragmentProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.UUID

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var _binding: FragmentProfileBinding
    private var selectedImageUri: Uri? = Uri.parse("")
    private var selectedImageUrl: String? = ""
    val storage = FirebaseStorage.getInstance()
    private val binding get() = _binding
    val TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        Log.d(TAG,"onCreate: ")

        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProfileBinding.bind(view)
        datosUsuario()
        datosFollow()
        datosSave()

        // Set click listener for the button
        binding.button7?.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileUserActivity::class.java))
        }

        binding.button6?.setOnClickListener{
            closeSsion()
        }

        binding.textView5?.setOnClickListener{
            startActivity(Intent(context, FollowUserActivity::class.java))
        }
        binding.button10?.setOnClickListener{
            startActivity(Intent(context, BoughtTicketsActivity::class.java))
        }
        binding.button5?.setOnClickListener {
            pickImageGallery()
        }
        return view
    }

    private fun datosUsuario(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
        databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(UsuariosModelos::class.java)
                    binding.nombre?.text = usuario?.firstName

                    val imageProfileUrl = usuario?.imageProfile
                    if (!imageProfileUrl.isNullOrEmpty()) {
                        Picasso.get().load(imageProfileUrl).into(binding.imageButton3 as ImageView)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun datosSave(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("saves")
        databaseReference.orderByChild("emailUser").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val count = dataSnapshot.childrenCount
                    binding.savesNum?.text = count.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun datosFollow(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("follows")
        databaseReference.orderByChild("emailUser").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val count = dataSnapshot.childrenCount
                    binding.followNum?.text = count.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AddBandFragment.IMAGE_REQUEST_CODE)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val storageRef = storage.reference.child("imagesProfile").child("${UUID.randomUUID()}")

        if(requestCode == AddBandFragment.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            selectedImageUri = data?.data
            binding.imageButton3.setImageURI(selectedImageUri)
            Log.d("UriImage", "URI: "+"$selectedImageUri")

            storageRef.putFile(selectedImageUri!!).addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val downloadUrl = downloadUri.toString()
                    selectedImageUrl = downloadUrl
                    Log.d("UriImage1", "URI de la imagen 1: $selectedImageUri")

                    val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
                    databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val user = snapshot.getValue(BandaModelo::class.java)
                                if (user != null && user.email == auth.currentUser?.email) {
                                    val actualizacionMap = hashMapOf<String, Any?>(
                                        "imageProfile" to selectedImageUrl,
                                    )
                                    snapshot.ref.updateChildren(actualizacionMap)
                                        .addOnSuccessListener {
                                            Log.d("UriImage1", "URI de la imagen 1: $selectedImageUrl")
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


    private fun closeSsion(){
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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG,"onDestroy: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy :")
    }
}
