package id.taufiq.lostandfound.ui.akun

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.api.ApiClient
import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.helper.SessionManager
import id.taufiq.lostandfound.helper.Status
import id.taufiq.lostandfound.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_akun.*

class AkunFragment : Fragment() {
    private lateinit var viewModel: LogoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(LogoutViewModel::class.java)
    }

    private fun initAction() {
        btn_logout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val sessionManager = SessionManager(requireContext())
        viewModel.logoutUser(token = "Bearer ${sessionManager.fetchAuthToken()}").observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        if (resource.data?.isSuccessful!!){
                            sessionManager.clearLoggedInToken()
                            findNavController().navigate(R.id.action_akunFragment_to_welcomeFragment)
                            Log.d("test", resource.data.body().toString())
                            Toast.makeText(context , "Logout Berhasil.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("test", resource.data.body().toString())
                            Toast.makeText(context , "Logout Gagal!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Log.e("error", it.message.toString())
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}