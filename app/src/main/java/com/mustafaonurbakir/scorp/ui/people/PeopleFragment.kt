package com.mustafaonurbakir.scorp.ui.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mustafaonurbakir.scorp.R
import com.mustafaonurbakir.scorp.data.entities.Errors
import com.mustafaonurbakir.scorp.databinding.PeopleListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.people_list_fragment.*

@AndroidEntryPoint
class PeopleFragment : Fragment(){

    private lateinit var binding: PeopleListFragmentBinding
    private val viewModel: PeopleViewModel by viewModels()
    private lateinit var adapter: PeopleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.people_list_fragment, container, false)
        binding = PeopleListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        initView()
    }

    private fun setupRecyclerView() {
        adapter = PeopleAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun initView () {
        // catch scrolling
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1 && progress_bar.visibility != View.VISIBLE) {
                    viewModel.loadNextPage()
                }
            }
        })

        // catch refresh action
        swipeLayout.setOnRefreshListener {
            viewModel.refreshPage()
        }
    }

    private fun setupObservers() {
        viewModel.people.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()) {
                adapter.setItems(ArrayList(it))
                if(swipeLayout.isRefreshing) swipeLayout.isRefreshing = false
                textView_empty_list.visibility = View.GONE
            } else if (it != null) {
                onErrorStatusChanged(Errors.NO_DATA)
                if(swipeLayout.isRefreshing) swipeLayout.isRefreshing = false
            }
        })

        viewModel.loadingState.observe(this.viewLifecycleOwner, Observer {state ->
            when (state) {
                true -> {
                    progress_bar.visibility = View.VISIBLE
                }
                false -> {
                    progress_bar.visibility = View.GONE
                }
            }
        })

        viewModel.generalErrors.observe(viewLifecycleOwner, Observer {
            onErrorStatusChanged(it)
        })
    }

    /**
     * Show errors
     *
     * @param error represents an item from Errors
     */
    private fun onErrorStatusChanged(error: Errors) = when (error) {
        Errors.NO_DATA -> showToast(getString(R.string.error_no_data))
        Errors.NO_NEW_PERSON -> showToast(getString(R.string.error_no_new_people))

        else -> showToast(getString(R.string.error_sorry_something_went_wrong))
    }

    private fun showToast(msg: String) {
        Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
    }

}
