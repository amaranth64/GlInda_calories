package ru.worklight64.calories.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.worklight64.calories.databinding.FragProgCategoryBinding

private const val ARG_PARAM1 = "param1"

class FragProgCategory : Fragment() {
    private lateinit var form: FragProgCategoryBinding
    private var param1: ProgressInteractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ProgressInteractor?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        form = FragProgCategoryBinding.inflate(inflater, container, false)
        return form.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        form.bTst.setOnClickListener {
            param1?.setStr("product")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(s: ProgressInteractor) = FragProgCategory().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, s)
            }
        }
    }
}