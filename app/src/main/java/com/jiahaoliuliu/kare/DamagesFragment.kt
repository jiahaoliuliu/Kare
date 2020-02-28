package com.jiahaoliuliu.kare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jiahaoliuliu.kare.databinding.FragmentDamagesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DamagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DamagesFragment : Fragment() {

    private lateinit var binding: FragmentDamagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_damages, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.front.
    }
}
