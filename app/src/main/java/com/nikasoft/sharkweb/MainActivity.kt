package com.nikasoft.sharkweb

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikasoft.sharkweb.databinding.ActivityMainBinding
import java.lang.Exception
import java.text.FieldPosition
import java.util.concurrent.FutureTask

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    companion object{
        var tabList: ArrayList<Fragment> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabList.add(HomeFragment())
        binding.myPager.adapter=TabsAdapter(supportFragmentManager,lifecycle)
        binding.myPager.isUserInputEnabled=false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBackPressed() {

        var frag: BrowseFragment?=null
        try {
            frag= tabList[binding.myPager.currentItem] as BrowseFragment
        }catch (e: Exception){

        }

        when{
            frag?.binding?.webView?.canGoBack()==true ->frag.binding.webView.goBack()
         binding.myPager.currentItem!=0->{
             tabList.removeAt(binding.myPager.currentItem)
             binding.myPager.adapter?.notifyDataSetChanged()
             binding.myPager.currentItem= tabList.size-1

         }
         else->super.onBackPressed()
        }
    }

    private inner class TabsAdapter(fa: FragmentManager,lc:Lifecycle): FragmentStateAdapter(fa,lc){
        override fun getItemCount(): Int = tabList.size
        override fun createFragment(position: Int): Fragment = tabList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeTab(url:String, fragment: Fragment){
        tabList.add(fragment)
        binding.myPager.adapter?.notifyDataSetChanged()
        binding.myPager.currentItem= tabList.size-1
    }

    fun checkInternet(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork= connectivityManager.getNetworkCapabilities(network) ?: return false

            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                else->false
            }
        }else{
            @Suppress("DEPRECATION") val networkInfo=
                connectivityManager.activeNetworkInfo?:return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}