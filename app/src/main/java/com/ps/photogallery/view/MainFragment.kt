package com.ps.photogallery.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ps.photogallery.R
import com.ps.photogallery.viewmodel.MainViewModel

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.fetchPublicPhotoList()

        viewModel.photoListLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.toString())
        })

        viewModel.progressLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Show Progress: ${it.toString()}")
        })

        viewModel.exceptionLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Show Progress: $it")
        })

    }

    companion object {
        fun newInstance() = MainFragment()
        const val TAG = "MainFragment"
    }

}