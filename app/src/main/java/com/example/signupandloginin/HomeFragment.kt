package com.example.signupandloginin

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var logoutBtn : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        logoutBtn = view.findViewById(R.id.log_out)
        logoutBtn.setOnClickListener {
            logoutAlert(it)
        }

    return view
    }

    private fun logoutAlert(it : View) {
        if (auth.currentUser != null) {
            AlertDialog.Builder(requireContext())
                .setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, logout ->
                        auth.signOut()
                        it.findNavController()
                            .navigate(R.id.action_containerFragment_to_loginFragment)
                        Toast.makeText(
                            requireContext(),
                            "You Are Now Logged Out.",
                            Toast.LENGTH_LONG
                        ).show()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, it ->
                        dialog.cancel()
                    })
                .create()
                .show()
        } else {
            Toast.makeText(requireContext(),
                "You Must Be Logged In To Do That.",
                Toast.LENGTH_LONG)
                .show()
        }


    }
}