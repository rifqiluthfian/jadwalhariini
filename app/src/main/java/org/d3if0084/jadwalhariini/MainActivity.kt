package org.d3if0084.jadwalhariini

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3if0084.jadwalhariini.data.Catatan
import org.d3if0084.jadwalhariini.data.CatatanDb
import org.d3if0084.jadwalhariini.data.MainAdapter
import org.d3if0084.jadwalhariini.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainDialog.DialogListener {
    private lateinit var navController: NavController
    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback {


        override fun onActionItemClicked(
            mode: ActionMode?,
            item: MenuItem?
        ): Boolean {
            if (item?.itemId == R.id.menu_delete) {
                deleteData()
                return true
            }
            return false
        }

        override fun onCreateActionMode(
            mode: ActionMode?,
            menu: Menu?
        ): Boolean {
            mode?.menuInflater?.inflate(R.menu.main_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?,
                                         menu: Menu?): Boolean {
            mode?.title = myAdapter.getSelection().size.toString()
            return true
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            myAdapter.resetSelection()
            actionMode = null
        }
    }


    private val viewModel: MainViewModel by lazy {
        val dataSource = CatatanDb.getInstance(this).dao
        val factory = MainViewModelFactory(dataSource)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MainAdapter
    private val handler = object : MainAdapter.ClickHandler {
        override fun onClick(position: Int, catatan: Catatan) {
            if (actionMode != null) {
                myAdapter.toggleSelection(position)
                if (myAdapter.getSelection().isEmpty())
                    actionMode?.finish()
                else
                    actionMode?.invalidate()
                return
            }
            val message = getString(R.string.catatan_klik, catatan.catatan)
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }

        override fun onLongClick(position: Int): Boolean {
            if (actionMode != null) return false
            myAdapter.toggleSelection(position)

            actionMode = startSupportActionMode(actionModeCallback)
            return true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        val switch: Switch = findViewById(R.id.switch1)

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
        binding.fab.setOnClickListener {
            MainDialog().show(supportFragmentManager, "MainDialog")
            myAdapter = MainAdapter(handler)
            with(binding.recyclerView) {
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                setHasFixedSize(true)
                adapter = myAdapter
            }
            viewModel.data.observe(this, {
                myAdapter.submitList(it)
                binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE
                else View.GONE
            })
        }
    }

    override fun processDialog(catatan: Catatan) {
        viewModel.insertData(catatan)

    }

    private fun deleteData() = AlertDialog.Builder(this).apply {
        setMessage(R.string.pesan_hapus)
        setPositiveButton(R.string.hapus) { _, _ ->
            viewModel.deleteData(myAdapter.getSelection())
            actionMode?.finish()
        }
        setNegativeButton(R.string.batal) { dialog, _ ->
            dialog.cancel()
            actionMode?.finish()
        }
        show()
    }
}