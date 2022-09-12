package com.dongkeun.toyproject.ui

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.dongkeun.toyproject.R
import com.dongkeun.toyproject.databinding.LoginFragmentBinding
import com.dongkeun.toyproject.function.StaticValue
import com.dongkeun.toyproject.viewmodel.LogInViewModel
import com.dongkeun.toyproject.viewmodel.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText

class LogInFragment : Fragment(), View.OnClickListener {

    private val baseLyt: ConstraintLayout by lazy {
        view?.findViewById(R.id.logIn_base) as ConstraintLayout
    }

    private var _binding: LoginFragmentBinding? = null
    private val binding: LoginFragmentBinding get() = _binding!!

    private val viewModel: LogInViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        initFragment()
        setObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseLyt.setOnClickListener(this)
    }

    override fun onStop() {
        super.onStop()

        baseLyt.setOnClickListener(null)
    }

    private fun initFragment() {
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            logInViewModel = viewModel
        }
    }

    private fun setObservers() {
        with(viewModel) {
            logInFlag.observe(viewLifecycleOwner) {
                if (it) {
                    measureAndSetWidthAndHeight()
                    toList()
                }
            }

            toSignUp.observe(viewLifecycleOwner) {
                if (it) {
                    toSignUp()
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

    private fun toList() {
        val action = LogInFragmentDirections.loginToList()
        Navigation.findNavController(requireActivity(), R.id.main_container)
            .navigate(action)
    }

    private fun toSignUp() {
        val action = LogInFragmentDirections.loginToSignup()
        Navigation.findNavController(requireActivity(), R.id.main_container)
            .navigate(action)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(baseLyt.windowToken, 0)
    }

    private fun measureAndSetWidthAndHeight() {
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        StaticValue.setWidth(width)
        StaticValue.setHeight(height)
    }

    private fun makeToast(content: String) = Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.logIn_base -> hideKeyboard()
        }
    }
}