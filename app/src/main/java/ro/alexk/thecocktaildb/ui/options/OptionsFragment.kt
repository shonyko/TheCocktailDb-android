package ro.alexk.thecocktaildb.ui.options

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ro.alexk.thecocktaildb.MainActivity
import ro.alexk.thecocktaildb.R
import ro.alexk.thecocktaildb.databinding.FragmentOptionsBinding
import ro.alexk.thecocktaildb.ui.login.LoginActivity

class OptionsFragment : Fragment() {

    private lateinit var binding: FragmentOptionsBinding

    private lateinit var viewModel: OptionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dependencyContainer = (activity as MainActivity).dependencyContainer
        viewModel = OptionsViewModel(dependencyContainer.loginRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.username.text =
            String.format(resources.getString(R.string.hello), viewModel.viewState.value.username)

        binding.logOutBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
            dialogBuilder
                .setTitle("Please confirm")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Log Out") { _, _ ->
                    (activity as MainActivity).dependencyContainer.loginRepository.logout()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }
}
