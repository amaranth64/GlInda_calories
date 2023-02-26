package ru.worklight64.calories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.worklight64.calories.databinding.ActivityStepByStepBinding
import ru.worklight64.calories.fragments.FragmentManager
import ru.worklight64.calories.progress.*

class StepByStepActivity : AppCompatActivity(), ProgressInteractor.Observer{
    private lateinit var form: ActivityStepByStepBinding
    private lateinit var interactor: ProgressInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityStepByStepBinding.inflate(layoutInflater)
        setContentView(form.root)
        interactor = ProgressInteractor(this)
        interactor.setStep("category")
    }

    override fun observe(s: String) {
        when (s){
            "category" -> {
                form.tvStep1.background =  resources.getDrawable(R.color.card_protein)
                FragmentManager.setFragment(FragProgCategory.newInstance(interactor), this)
            }
            "product" -> {
                form.tvStep2.setBackgroundColor(resources.getColor(R.color.card_protein))
                FragmentManager.setFragment(FragProgProduct.newInstance(interactor), this)
            }
            "weight" -> {
                form.tvStep3.setBackgroundColor(resources.getColor(R.color.card_protein))
                FragmentManager.setFragment(FragProgWeight.newInstance(interactor), this)
            }
            "final" -> {
                form.tvStep4.setBackgroundColor(resources.getColor(R.color.card_protein))
                FragmentManager.setFragment(FragProgFinal.newInstance(interactor), this)
            }
            "done" -> {
                finish()
            }
        }
    }
}