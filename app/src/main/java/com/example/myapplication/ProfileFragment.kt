package com.example.myapplication


import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var _binding: FragmentProfileBinding
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

        // Set click listener for the button
        binding.button7?.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileUserActivity::class.java))
        }

        binding.button6?.setOnClickListener{
            closeSsion()
        }
        return view
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
