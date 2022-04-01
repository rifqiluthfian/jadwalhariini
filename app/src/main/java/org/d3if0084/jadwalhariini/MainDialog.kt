package org.d3if0084.jadwalhariini

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.d3if0084.jadwalhariini.data.Catatan
import org.d3if0084.jadwalhariini.databinding.DialogMainBinding

class MainDialog : DialogFragment() {
    private lateinit var binding: DialogMainBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        binding = DialogMainBinding.inflate(inflater, null, false)
        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.tambah_catatan)
            setView(binding.root)
            setPositiveButton(R.string.simpan) { _, _ ->
                val mahasiswa = getData() ?: return@setPositiveButton
                val listener = requireActivity() as DialogListener
                listener.processDialog(mahasiswa)

            }
            setNegativeButton(R.string.batal) { _, _ -> dismiss() }
        }
        return builder.create()
    }
    interface DialogListener {
        fun processDialog(catatan: Catatan)
    }
    private fun getData(): Catatan? {
        if (binding.nimEditText.text.isEmpty()) {
            showMessage(R.string.catatan_wajib_diisi)
            return null
        }
        if (binding.namaEditText.text.isEmpty()) {
            showMessage(R.string.jam_wajib_diisi)
            return null
        }
        return Catatan(
            catatan = binding.nimEditText.text.toString(),
            jam = binding.namaEditText.text.toString()
        )
    }
    private fun showMessage(messageResId: Int) {
        Toast.makeText(requireContext(), messageResId, Toast.LENGTH_LONG).apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}