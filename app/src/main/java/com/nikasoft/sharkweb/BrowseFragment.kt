package com.nikasoft.sharkweb

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nikasoft.sharkweb.databinding.FragmentBrowseBinding


class BrowseFragment(private var urlNew: String) : Fragment() {

    lateinit var binding: FragmentBrowseBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_browse,container,false)
        binding = FragmentBrowseBinding.bind(view)
        binding.webView.canGoBack()

        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()

        val mainRef =requireActivity() as MainActivity

        binding.webView.apply {
            settings.javaScriptEnabled =true
            settings.setSupportZoom(true)
            settings.builtInZoomControls=true
            settings.displayZoomControls=false
            webViewClient= object :WebViewClient(){
                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                    mainRef.binding.topSearchBar.setQuery(url,false)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    mainRef.binding.progressBar.progress=0
                    mainRef.binding.progressBar.visibility=View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mainRef.binding.progressBar.visibility=View.GONE
                }
            }
            webChromeClient=object :WebChromeClient(){
                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    super.onShowCustomView(view, callback)
                    binding.webView.visibility=View.GONE
                    binding.videoView.apply {
                        visibility=View.VISIBLE
                        addView(view) }
                }

                override fun onHideCustomView() {
                    super.onHideCustomView()
                    binding.webView.visibility=View.VISIBLE
                    binding.videoView.visibility=View.GONE
                }
            }

            when{
                URLUtil.isValidUrl(urlNew)->loadUrl(urlNew)
                urlNew.contains(".com", ignoreCase = true)->loadUrl(urlNew)
                else ->loadUrl("https://www.google.com/search?q=$urlNew")
            }
        }
    }
}