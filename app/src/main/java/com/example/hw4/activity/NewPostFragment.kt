package com.example.hw4.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hw4.databinding.FragmentNewPostBinding
import com.example.hw4.util.AndroidUtils
import com.example.hw4.util.StringArg
import com.example.hw4.viewModel.PostViewModel

class NewPostFragment : Fragment() {
    companion object{
        var Bundle.textArg: String? by StringArg
    }
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        arguments?.textArg
            ?.let (
                binding.edit::setText)
        binding.edit.requestFocus()
        binding.ok.setOnClickListener{
            val content = binding.edit.text.toString()
            viewModel.changeContent(content)
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}