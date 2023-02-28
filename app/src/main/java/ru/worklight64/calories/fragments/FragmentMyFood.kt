package ru.worklight64.calories.fragments


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.calories.MainApp
import ru.worklight64.calories.ProductInMyFoodActivity
import ru.worklight64.calories.R
import ru.worklight64.calories.adapters.MyFoodAdapter
import ru.worklight64.calories.adapters.RecyclerTouchListener
import ru.worklight64.calories.adapters.RecyclerTouchListener.OnRowClickListener
import ru.worklight64.calories.adapters.RecyclerTouchListener.OnSwipeOptionsClickListener
import ru.worklight64.calories.databinding.EditDialogBinding
import ru.worklight64.calories.databinding.FragmentMyfoodBinding
import ru.worklight64.calories.db.MainViewModel
import ru.worklight64.calories.dialogs.EditDialog
import ru.worklight64.calories.entities.MenuNameListItem
import ru.worklight64.calories.utils.CommonConst


class FragmentMyFood : Fragment(), EditDialog.Listener {
    private lateinit var form: FragmentMyfoodBinding
    private lateinit var pref: SharedPreferences
    private lateinit var adapter: MyFoodAdapter

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        form = FragmentMyfoodBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return form.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        //val swiper = getSwipeManager()
        //swiper.attachToRecyclerView(form.rcViewProduct)
        observer()
    }

    private fun initRcView(){
        pref = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val linear = getString(R.string.pref_linear)
        if (pref.getString(CommonConst.KEY_LINEAR, linear) == linear) form.rcViewProduct.layoutManager = LinearLayoutManager(activity)
        else form.rcViewProduct.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        adapter = MyFoodAdapter( pref)
        form.rcViewProduct.adapter = adapter

        form.bAddMenuName.setOnClickListener{
            EditDialog.showDialog(requireContext(),this@FragmentMyFood, getString(R.string.menu_name), getString(R.string.enter_caption), getString(R.string.add))
        }

        val touchListener = RecyclerTouchListener(activity, form.rcViewProduct)
        touchListener.setClickable(object : OnRowClickListener {
                override fun onRowClicked(position: Int) {
                    val intent = Intent(context, ProductInMyFoodActivity::class.java).putExtra(CommonConst.INTENT_MENU, adapter.currentList[position])
                    startActivity(intent)
                }

                override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
            })
            .setSwipeOptionViews(R.id.delete_task, R.id.edit_task)
            .setSwipeable(R.id.rowFG, R.id.rowBG,
                OnSwipeOptionsClickListener { viewID, position ->
                    when (viewID) {
                        R.id.delete_task -> {
                            val a = adapter.currentList[position]
                            mainViewModel.deleteMenuName(a.id!!)
                        }
                        R.id.edit_task -> {
                            Toast.makeText(
                                context,
                                "Edit",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                })

        form.rcViewProduct.addOnItemTouchListener(touchListener)



    }

    private fun getSwipeManager(): ItemTouchHelper{
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val i = viewHolder.adapterPosition
                val a = adapter.currentList[i]
                mainViewModel.deleteMenuName(a.id!!)
            }

        })


    }

    private fun observer(){
        mainViewModel.allMenuNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            if (it.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMyFood()
    }

    override fun onClick(newListName: String) { // dialog
        mainViewModel.insertMenuName(
            MenuNameListItem(
                null,
                newListName,
                0.0,
                0.0,
                0.0,
                0.0
            )
        )
    }
}