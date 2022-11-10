package com.example.signupandloginin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_container.view.*

class ContainerFragment : Fragment() {

    private val homeFragment = HomeFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_container, container, false)


        replaceFragment(homeFragment)
        view.bottom_nav.selectedItemId = R.id.homeFragment
        view.bottom_nav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.homeFragment -> replaceFragment(homeFragment)
            }
            true
        }

    return view
    }
    //Call the parentFragmentManager, so a transaction can occur
    private fun replaceFragment(fragment : Fragment) {

        val transaction = parentFragmentManager
        transaction.beginTransaction().replace(
            R.id.fragment_container,
            fragment
        ).commit()
    }
}