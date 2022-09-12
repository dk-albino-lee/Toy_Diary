package com.dongkeun.toyproject.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.dongkeun.toyproject.R
import com.dongkeun.toyproject.databinding.SignupFragmentBinding
import com.dongkeun.toyproject.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {
    private var _binding: SignupFragmentBinding? = null
    private val binding: SignupFragmentBinding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var backCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupFragmentBinding.inflate(inflater, container, false)
        initBinding()
        setObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.base.setOnClickListener {
            hideKeyboard()
        }
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
            signUpViewModel = viewModel
        }
    }

    private fun setObservers() {
        with(viewModel) {
            toLogIn.observe(viewLifecycleOwner) {
                if (it) {
                    toLogIn()
                }
            }

            toast.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    makeToast(it)
                    viewModel.initToast()
                }
            }
        }
    }

    private fun toLogIn() {
        val action = SignUpFragmentDirections.signupToLogin()
        Navigation.findNavController(requireActivity(), R.id.main_container)
            .navigate(action)
    }

    private fun makeToast(content: String) = Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()

    private fun backPress() {
        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toLogIn()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.base.windowToken, 0)
    }
}