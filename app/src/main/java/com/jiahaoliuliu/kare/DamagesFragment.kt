package com.jiahaoliuliu.kare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [DamagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DamagesFragment : Fragment() {
    companion object {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_damages, container, false)
    }

}
