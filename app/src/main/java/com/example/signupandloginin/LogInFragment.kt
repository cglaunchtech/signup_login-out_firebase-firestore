package com.example.signupandloginin

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var logInBtn: AppCompatButton
    lateinit var signUpBtn: TextView
    lateinit var forgotLogin: TextView
    lateinit var loginEmailField: TextInputEditText
    lateinit var loginPasswordField: TextInputEditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentuser = auth.currentUser
        if (currentuser != null) {

            view.findNavController().navigate(R.id.action_loginFragment_to_containerFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)

        auth = FirebaseAuth.getInstance()
        logInBtn = view.findViewById(R.id.log_in_btn)
        signUpBtn = view.findViewById(R.id.sign_up_btn)
        forgotLogin = view.findViewById(R.id.forgot_login)
        loginEmailField = view.findViewById(R.id.login_email_field_2)
        loginPasswordField = view.findViewById(R.id.login_password_field_2)

        login()
        forgotlogin()

        signUpBtn.setOnClickListener {
            val myIntent = Intent(requireContext(), SignUpFragment::class.java)
            signUpBtn.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        return view
    }

    private fun forgotlogin() {
        forgotLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (loginEmailField.text.toString().isNotEmpty()) {

                    auth.sendPasswordResetEmail(loginEmailField.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val builder = AlertDialog.Builder(requireContext())
                                builder.setMessage("Email Sent to ${loginEmailField.text.toString()}")
                                builder.setCancelable(true)
                                builder.setNegativeButton("OK", DialogInterface.OnClickListener
                                { dialog, which -> dialog.cancel() })
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.show()

                                Log.d(ContentValues.TAG, "Email sent.")
                            }
                        }
                } else {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Please Enter a Valid Email Address")
                    builder.setCancelable(true)
                    builder.setNegativeButton("OK", DialogInterface.OnClickListener
                    { dialog, which -> dialog.cancel() })
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()

                }
            }
        })
    }

    private fun login() {

        logInBtn.setOnClickListener {
            if ((loginEmailField.text.toString()).isEmpty()) {
                loginEmailField.setHint("Please Enter a Valid Email Address")
                loginEmailField.setHintTextColor(Color.RED)
            } else if ((loginPasswordField.text.toString()).isEmpty()) {
                loginPasswordField.setHint("Please Enter your password")
                loginPasswordField.setHintTextColor(Color.RED)
            }

            val vFieldValidationError: String = fieldValidationError()
            if (vFieldValidationError != "No Error") {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Please Complete All Required Fields")
                builder.setCancelable(true)
                builder.setNegativeButton("OK", DialogInterface.OnClickListener
                { dialog, which -> dialog.cancel() })
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(
                loginEmailField.text.toString(),
                loginPasswordField.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("AppDatabase", "AAA LogicAct 1")
                        logInBtn.findNavController()
                            .navigate(R.id.action_loginFragment_to_containerFragment)

                        Log.d("AppDatabase", "AAA LogicAct 2")
                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Please enter valid email address and password")
                        builder.setCancelable(true)
                        builder.setNegativeButton("OK", DialogInterface.OnClickListener
                        { dialog, which -> dialog.cancel() })
                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                        Toast.makeText(
                            requireContext(),
                            "Login failed, please try again!",
                            Toast.LENGTH_SHORT
                        )
                    }
                }

        }
    }

    private fun fieldValidationError(): String {
        when (true) {
            (loginEmailField.text.toString()).isEmpty() -> {
                return "Please Enter your Email Address"

            }
            (loginPasswordField.text.toString()).isEmpty() -> {
                return "Please Enter Password"

            }
            else -> {
                return "No Error"
            }
        }
    }
}