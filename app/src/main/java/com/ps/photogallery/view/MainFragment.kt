package com.ps.photogallery.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ps.photogallery.R
import com.ps.photogallery.showAlertDialog
import com.ps.photogallery.viewmodel.ExceptionMessage
import com.ps.photogallery.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var viewModel: MainViewModel
    private val photoListAdapter: PhotoListAdapter = PhotoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = photoListAdapter
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.photoListLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.toString())
            photoListAdapter.items = it.toMutableList()
        })

        viewModel.progressLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
            }

        })

        viewModel.exceptionLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ExceptionMessage.NetworkException -> {
                    showAlertDialog(it.message,
                        requireActivity(),
                        { viewModel.fetchPublicPhotoList() },
                        { requireActivity().finish() })
                }
                ExceptionMessage.EmptyList -> {
                    showAlertDialog("",
                        requireActivity(),
                        { viewModel.fetchPublicPhotoList() },
                        { requireActivity().finish() })
                }
            }

        })

        viewModel.fetchPublicPhotoList()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        when (item.itemId) {

            R.id.menu_sort_by_captured -> {
                viewModel.sortItemByDateCreate()
            }
            R.id.menu_sort_by_published -> {

                viewModel.sortItemByDatePublished()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun newInstance() = MainFragment()
        const val TAG = "MainFragment"
    }
}