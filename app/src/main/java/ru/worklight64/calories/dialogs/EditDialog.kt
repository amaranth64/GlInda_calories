package ru.worklight64.calories.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.worklight64.calories.databinding.EditDialogBinding

object EditDialog {

    fun showDialog(context: Context, listener: Listener, titleText: String, hintText: String, buttonText: String){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val form = EditDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(form.root)
        form.textTitle.text = titleText
        form.edText.hint = hintText
        form.bButton.text = buttonText

        form.bButton.setOnClickListener {
            val listName = form.edText.text.toString()
            if (listName.isNotEmpty()){
                listener.onClick(listName)
            }
            dialog?.dismiss()
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener{
        fun onClick(newListName: String)
    }

}