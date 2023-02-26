package ru.worklight64.calories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.worklight64.calories.databinding.ActivityStepByStepBinding

class StepByStepActivity : AppCompatActivity() {
    private lateinit var form: ActivityStepByStepBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityStepByStepBinding.inflate(layoutInflater)
        setContentView(form.root)
    }
}