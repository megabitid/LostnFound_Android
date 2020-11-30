package id.taufiq.lostandfound.ui.register

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.api.ApiClient
import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.helper.Constants.IMG_DEFAULT
import id.taufiq.lostandfound.helper.SessionManager
import id.taufiq.lostandfound.helper.Status
import id.taufiq.lostandfound.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*


@Suppress("LABEL_NAME_CLASH")
class RegisterFragment : Fragment() {
    private lateinit var checkBox: CheckBox
    private lateinit var button: Button
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.btn_create_account)
        checkBox = view.findViewById(R.id.checkbox1)
        initAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(RegisterViewModel::class.java)
    }

    private fun initAction() {

        btn_back.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
        }

        checkBox.setOnClickListener {
            val name = view?.text_name?.editText?.text.toString().trim()
            val email = view?.text_email?.editText?.text.toString().trim()
            val password = view?.text_password?.editText?.text.toString().trim()

            if (checkBox.isChecked && name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                button.isEnabled = true
                button.setBackgroundResource(R.drawable.background_text_radius_gradient)
                button.setTextColor(getColor(requireContext() , R.color.grey_color))

                btn_create_account.setOnClickListener {

                    if (name.isEmpty()) {
                        view?.text_name?.error = "Nama harus diisi!"
                        view?.text_name?.requestFocus()
                        return@setOnClickListener
                    }

                    if (email.isEmpty()) {
                        view?.text_email?.error = "Email harus diisi!"
                        view?.text_email?.requestFocus()
                        return@setOnClickListener
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        view?.text_email?.error = "Email tidak valid!"
                        view?.text_email?.requestFocus()
                        return@setOnClickListener
                    }

                    if (password.isEmpty() || password.length < 8) {
                        view?.text_password?.error = "Password harus lebih dari 8 karakter!"
                        view?.text_password?.requestFocus()
                        return@setOnClickListener
                    }

                    createUser(name, email, password)
                }
            }else{
                button.isEnabled = false
                button.setBackgroundResource(R.drawable.background_text_radius_before_press)
                button.setTextColor(getColor(requireContext() , R.color.grey_color_paragraf))
            }
        }
    }

    private fun createUser(nama : String, email: String, password: String){
        viewModel.createUser(nama, email, password, IMG_DEFAULT).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        if (resource.data?.isSuccessful!!){
                            val sessionManager = SessionManager(requireContext())
                            sessionManager.saveAuthToken(resource.data.body()?.token.toString())
                            resource.data.body()?.id?.let { it1 -> sessionManager.saveUserId(it1) }
                            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                            Log.d("test", resource.data.body().toString())
                            Toast.makeText(context , "Reister Berhasil.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("test", resource.data.body().toString())
                            Toast.makeText(context , "Email sudah ada!", Toast.LENGTH_SHORT).show()
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