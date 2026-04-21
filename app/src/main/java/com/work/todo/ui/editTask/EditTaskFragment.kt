package com.work.todo.ui.editTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.work.todo.databinding.FragmentEditTaskBinding

class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabSave.scaleX = 0f
        binding.fabSave.scaleY = 0f
        binding.fabSave.postDelayed({ animateFab() }, 300)

        binding.tvCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fabSave.setOnClickListener {
            // Здесь будет логика сохранения изменений
            findNavController().popBackStack()
        }
    }

    private fun animateFab() {
        binding.fabSave.visibility = View.VISIBLE
        binding.fabSave.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}