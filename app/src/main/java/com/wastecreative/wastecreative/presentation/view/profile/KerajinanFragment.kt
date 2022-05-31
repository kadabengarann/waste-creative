package com.wastecreative.wastecreative.presentation.view.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wastecreative.wastecreative.data.model.Craft
import com.wastecreative.wastecreative.databinding.FragmentListBinding


class KerajinanFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val data = ArrayList<Craft>()

    private val kerajinanViewAdapter: KerajinanViewAdapter by lazy {
       KerajinanViewAdapter(
            arrayListOf()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeDummyData()
        showRecyclerList()
    }



    private fun makeDummyData() {
        if (data.isEmpty()) {
            for (i in 1..4) {
                val items = Craft(
                    i.toString(),
                    "Saifuddin",
                    "https://picsum.photos/300/300?random=$i",
                    "Kapal Mainan $i",
                    "https://picsum.photos/200/300?random=$i",
                    "yoman $i"
                )
                data.add(items)
            }
        }
    }
    private fun showRecyclerList() {
        binding.rvKerajinan.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = kerajinanViewAdapter
        }
        kerajinanViewAdapter.setData(data)

    }


}