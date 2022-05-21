package com.wastecreative.wastecreative.presentation.view.craft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.FragmentDetailCraftTabBinding

class DetailCraftTabFragment : Fragment() {
    private var binding: FragmentDetailCraftTabBinding? = null

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailCraftTabBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        index = arguments?.getInt(ARG_SECTION_NUMBER, 0)!!

        if (arguments != null) {
            when (index) {
                1 -> {
                    binding?.tvHeadingTop?.text = resources.getString(R.string.tab_tools_and_material_text)
                }
                2 -> {
                    binding?.tvHeadingTop?.text = resources.getString(R.string.tab_steps_text)
                }
            }
        }
    }
    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val EXTRA_CRAFT = "extra_object"
    }
}