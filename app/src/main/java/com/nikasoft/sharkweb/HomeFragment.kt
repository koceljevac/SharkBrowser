package com.nikasoft.sharkweb

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.nikasoft.sharkweb.databinding.ActivityMainBinding
import com.nikasoft.sharkweb.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_home,container,false)
        binding = FragmentHomeBinding.bind(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        val mainActivityRef=requireActivity() as MainActivity
        mainActivityRef.binding.topSearchBar
        binding.searchView.setQuery("",false)
        mainActivityRef.binding.topSearchBar.setQuery("",false)


        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(result: String?): Boolean {
                if(mainActivityRef.checkInternet(requireContext()))
                    mainActivityRef.changeTab(result!!,BrowseFragment(result))
                else
                    Snackbar.make(binding.root,"INTERNET NOT CONNECTED",3000).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean =false

        })

        mainActivityRef.binding.topSearchBar.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(result: String?): Boolean {
                if (mainActivityRef.checkInternet(requireContext()))
                    mainActivityRef.changeTab(mainActivityRef.binding.topSearchBar.query.toString(),
                        BrowseFragment(mainActivityRef.binding.topSearchBar.query.toString()))
                else
                    Snackbar.make(binding.root,"INTERNET NOT CONNECTED",3000).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean =false

        })


    }

}