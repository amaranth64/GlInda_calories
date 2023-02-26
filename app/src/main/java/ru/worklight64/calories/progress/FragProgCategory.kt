package ru.worklight64.calories.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.worklight64.calories.databinding.FragProgCategoryBinding


class FragProgCategory : Fragment() {
    private lateinit var form: FragProgCategoryBinding

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        form = FragProgCategoryBinding.inflate(inflater, container, false)
        return form.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragProgCategory()
    }
}