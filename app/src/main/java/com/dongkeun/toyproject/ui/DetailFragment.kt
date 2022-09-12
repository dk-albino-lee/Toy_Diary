package com.dongkeun.toyproject.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.dongkeun.toyproject.R
import com.dongkeun.toyproject.databinding.DetailFragmentBinding
import com.dongkeun.toyproject.model.Diary
import com.dongkeun.toyproject.viewmodel.DetailViewModel
import java.lang.Exception
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var backCallback: OnBackPressedCallback

    private lateinit var postId: String

    private lateinit var menuProvider: MenuProvider

    private var _binding: DetailFragmentBinding? = null
    private val binding: DetailFragmentBinding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)

        initBinding()

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.detail_more -> {

                    }
                }

                return true
            }

        }

        (requireActivity() as MenuHost).addMenuProvider(menuProvider)

        val myArgs: DetailFragmentArgs by navArgs()
        postId = myArgs.diaryId

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
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
            detailViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private fun setObservers() {
        with(viewModel) {
            diary.observe(viewLifecycleOwner) {
                makePageWithObservedDiary(it)
            }
        }
    }

    private fun makePageWithObservedDiary(diary: Diary) {
        if (diary.comments.isNotEmpty()) {
            // TODO : adapter
        }

        // TODO : if (hasThumbnail) getImage else use default image
//        val thumbnail: Bitmap = if (diary.isThumbnailExist) {
//            // TODO : get image
//        } else {
//            ContextCompat.getDrawable(requireContext(), R.drawable.ic_default_photo)!!.toBitmap()
//        }

//        binding.detailImage.setImageBitmap(
//            if (diary.thumbnail.isNullOrEmpty()) {
//                ContextCompat.getDrawable(requireContext(), R.drawable.ic_default_photo)?.toBitmap()
//            } else {
//                toBitmap(diary.thumbnail) ?: ContextCompat.getDrawable(requireContext(), R.drawable.ic_default_photo)?.toBitmap()
//            }
//        )
    }

    private fun toList() {
        val action = DetailFragmentDirections.detailToList()
        Navigation.findNavController(requireActivity(), R.id.main_container)
            .navigate(action)
    }

    private fun backPress() {
        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toList()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
    }

    fun toBitmap(encodedString: String): Bitmap? {
        try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            return null
        }
    }
}