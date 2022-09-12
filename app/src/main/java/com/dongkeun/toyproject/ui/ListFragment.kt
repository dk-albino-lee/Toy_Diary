package com.dongkeun.toyproject.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dongkeun.toyproject.R
import com.dongkeun.toyproject.databinding.ListFragmentBinding
import com.dongkeun.toyproject.function.OnClickInterface
import com.dongkeun.toyproject.model.Diary
import com.dongkeun.toyproject.viewmodel.ListViewModel

class ListFragment : Fragment(), PopupMenu.OnMenuItemClickListener, OnClickInterface {

    private lateinit var backCallback: OnBackPressedCallback

    private val swipeLyt: SwipeRefreshLayout by lazy {
        view?.findViewById(R.id.list_swipe_refresh_lyt) as SwipeRefreshLayout
    }
    private val listRecycler: RecyclerView by lazy {
        view?.findViewById(R.id.list_recycler) as RecyclerView
    }

    private lateinit var menuProvider: MenuProvider

    private var _binding: ListFragmentBinding? = null
    private val binding: ListFragmentBinding get() = _binding!!
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ListFragmentBinding.inflate(inflater, container, false)
        initBinding()
        setObservers()
        // Deprecated
//        setHasOptionsMenu(true)

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.list_settings -> {

                    }
                    R.id.list_more -> {
                        showPopUp(view!!)
                    }
                }

                return true
            }
        }

        (requireActivity() as MenuHost).addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUIComponents()
//        initTestList()
        viewModel.getDiaries()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MenuHost).removeMenuProvider(menuProvider)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPress()
    }

    override fun onDetach() {
        super.onDetach()
        backCallback.remove()
    }

    private fun initBinding() {
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            listViewModel = viewModel
        }
    }

    private fun setObservers() {
        with(viewModel) {
            diaries.observe(viewLifecycleOwner) {
                if (binding.listRecycler.adapter == null) {
                    binding.listRecycler.adapter = DiaryListBindingAdapter(this@ListFragment)
                }
                (binding.listRecycler.adapter as DiaryListBindingAdapter).makeList(it)
                binding.listRecycler.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun initUIComponents() {
        listRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        listRecycler.addItemDecoration(ItemOffsetDecoration(requireContext(), R.dimen.list_spacing))
        swipeLyt.isRefreshing = false
    }

    private fun initTestList() {
        val testList = listOf(
            Diary.TEST(), Diary.TEST(), Diary.TEST(), Diary.TEST(), Diary.TEST()
        )
        listRecycler.adapter = DiaryListAdapter(testList, this)
        listRecycler.adapter?.notifyDataSetChanged()
    }

    private fun toDetail(_id: String) {
        val action = ListFragmentDirections.listToDetail(_id)
        Navigation.findNavController(requireActivity(), R.id.main_container)
            .navigate(action)
    }

    private fun toLogIn() {
        val action = ListFragmentDirections.listToLogin()
        Navigation.findNavController(requireActivity(), R.id.main_container)
            .navigate(action)
    }

    private fun backPress() {
        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toLogIn()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
    }

    private fun showPopUp(v: View) {
        PopupMenu(requireActivity(), v, Gravity.RIGHT)
            .apply {
                setOnMenuItemClickListener(this@ListFragment)
                inflate(R.menu.list_submenu)
                show()
            }
    }

    override fun onClick(_id: String) {
        toDetail(_id)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.list_sub_write -> {

                true
            }
            R.id.list_sub_logout -> {

                true
            }
            else -> false
        }
    }
}