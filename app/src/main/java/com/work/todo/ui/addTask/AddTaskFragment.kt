package com.work.todo.ui.addTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.work.todo.databinding.FragmentAddTaskBinding

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabDone.scaleX = 0f
        binding.fabDone.scaleY = 0f
        binding.fabDone.postDelayed({ animateFab() }, 300)

        binding.tvCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun animateFab() {
        binding.fabDone.visibility = View.VISIBLE
        binding.fabDone.animate()
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