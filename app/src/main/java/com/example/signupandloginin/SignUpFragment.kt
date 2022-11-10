package com.example.signupandloginin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SignUpFragment : Fragment() {

    lateinit var auth : FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var nameField : TextInputEditText
    lateinit var emailField : TextInputEditText
    lateinit var passwordField : TextInputEditText
    lateinit var confirmPassword: TextInputEditText
    lateinit var aboutMeField : TextInputEditText
    lateinit var signUpBtn : AppCompatButton
    lateinit var cancelBtn : AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        cancelBtn = view.findViewById(R.id.cancel_btn)
        signUpBtn = view.findViewById(R.id.sign_up_btn)
        nameField  =  view.findViewById(R.id.name_2)
        emailField  =  view.findViewById(R.id.email_field_2)
        passwordField =  view.findViewById(R.id.password_field_2)
        confirmPassword =  view.findViewById(R.id.confirm_password_field_2)
        aboutMeField =  view.findViewById(R.id.about_me_field_2)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()



        cancelBtn.setOnClickListener {
            Toast.makeText(requireContext(), "SignUp Cancelled", Toast.LENGTH_LONG).show()
            cancelBtn.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        signUp()

        return view
    }
    private fun signUp(){


        signUpBtn.setOnClickListener {


            if(TextUtils.isEmpty( nameField.text.toString())){
                nameField.setError("Please Enter First Name")
                return@setOnClickListener

            }else if (TextUtils.isEmpty( emailField.text.toString())){
                emailField.setError("Please Enter Your Email Address")
                return@setOnClickListener

            }else if (TextUtils.isEmpty( passwordField.text.toString())){
                passwordField.setError("Please Enter Password")
                return@setOnClickListener
            }else if (TextUtils.isEmpty( confirmPassword.text.toString())){
                confirmPassword.setError("Please Confirm Password")
                return@setOnClickListener

            }

            println("Please complete all fields")


            auth.createUserWithEmailAndPassword(emailField.text.toString(),passwordField.text.toString())
                .addOnSuccessListener {
                    Log.d("AppDatabase","AAA to 1")
                    Toast.makeText(requireContext(), "Successfully Registered", Toast.LENGTH_LONG).show()
                    firestoreUserInit()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                    view?.findNavController()?.navigate(R.id.action_signUpFragment_to_containerFragment)
                }

                .addOnFailureListener {
                    Log.d("AppDatabase","AAA else 1")
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage(it.message)
                    builder.setCancelable(true)
                    builder.setNegativeButton("OK", DialogInterface.OnClickListener
                    { dialog, which -> dialog.cancel() })
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                    Toast.makeText(requireContext(), "Registration Failed; Please Try Again", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun firestoreUserInit() = CoroutineScope(Dispatchers.IO).launch{
        try {
            Firebase.firestore.collection("users")
                .document("New Users")
                .set(
                    User(
                        id = null,
                        uid = auth.uid,
                        email = emailField.text.toString(),
                        password = passwordField.text.toString(),
                        name = nameField.text.toString(),
                        aboutMe = aboutMeField.text.toString(),
                    ), SetOptions.merge())
        }catch (e: Exception){
            Timber.e(e)
        }
    }
}