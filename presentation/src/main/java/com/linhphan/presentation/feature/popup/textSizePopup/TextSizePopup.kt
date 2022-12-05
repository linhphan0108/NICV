package com.linhphan.presentation.feature.popup.textSizePopup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.linhphan.presentation.R
import com.linhphan.presentation.databinding.LpPopupTextSizeBinding
import com.linhphan.presentation.feature.home.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextSizePopup: DialogFragment() {

    companion object{
        private const val tag = "TextSizePopup"
        fun show(fragmentManager: FragmentManager){
            TextSizePopup().show(fragmentManager, tag)
        }
    }

    private val binding: LpPopupTextSizeBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.lp_popup_text_size, null, false)
    }

    /**
     * this view model is shared from the host [com.linhphan.presentation.feature.home.ui.MainActivity]
     */
    private val viewModelMain: MainViewModel by activityViewModels()

    private val viewModelTextSizePopup: TextSizePopupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModelTextSizePopup
        binding.lifecycleOwner = this
        viewModelTextSizePopup.setDefaultTextSize(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val width = resources.getDimensionPixelSize(R.dimen._270sdp)
        val height = resources.getDimensionPixelSize(R.dimen._250sdp)
        dialog?.window?.setLayout(width, height)
        setupObservers()
    }

    private fun setupObservers(){
        viewModelTextSizePopup.onApplyTextSizeObservable.observe(viewLifecycleOwner, {
            viewModelMain.onApplyNewTextScale(it)
        })
    }
}