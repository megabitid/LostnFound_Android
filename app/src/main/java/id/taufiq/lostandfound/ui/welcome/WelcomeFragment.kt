package id.taufiq.lostandfound.ui.welcome

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.api.ApiClient
import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.helper.Constants.CLIENT_ID
import id.taufiq.lostandfound.helper.Constants.RC_SIGN_IN
import id.taufiq.lostandfound.helper.SessionManager
import id.taufiq.lostandfound.helper.Status
import id.taufiq.lostandfound.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_welcome.*


class WelcomeFragment : Fragment() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: SingUpGoogleViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(CLIENT_ID)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        initAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(SingUpGoogleViewModel::class.java)
    }

    private fun initAction() {
        btn_create_account.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }

        btn_login.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        btn_with_google.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null){
            try {
                val authCode = account.serverAuthCode
                if (authCode != null) {
//                    authenticate(authCode)
                    Log.e(TAG, "update Ui")
                }
            } catch (e: Exception) {
                if (e.message != null) Log.e(TAG, e.message!!)
                Toast.makeText(context, getString(R.string.unhandled_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticate(googleAuthCode: String){
        viewModel.singUpGoogle(googleAuthCode).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        val sessionManager = SessionManager(requireActivity())
                            sessionManager.saveAuthToken(resource.data?.body()?.token.toString())
                            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
                            Log.e("test", resource.data?.body().toString())
                            Toast.makeText(context, "Login Berhasil.", Toast.LENGTH_SHORT).show()

//                        if (resource.data?.isSuccessful!!) {
//                            val sessionManager = SessionManager(requireActivity())
//                            sessionManager.saveAuthToken(resource.data.body()?.token.toString())
//                            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
//                            Log.d("test", resource.data.body().toString())
//                            Toast.makeText(context, "Reister Berhasil.", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Log.e("test", resource.data.body().toString())
//                            Toast.makeText(context, "Email sudah ada!", Toast.LENGTH_SHORT).show()
//                        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("WelcomeFragment", "displayName :" + account.displayName)
                    Log.d("WelcomeFragment", "email :" + account.email)
                    Log.d("WelcomeFragment", "serverAuthCode :" + account.serverAuthCode)
                    Log.d("WelcomeFragment", "idToken :" + account.idToken)
                    updateUI(account)
                } catch (e: ApiException) {
                    Log.w(TAG, "signInResult:failed code=" + e.statusCode)
                }
            } else {
                Log.e(TAG, "signInResult:failed code=" + exception.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onStart() {
        super.onStart()
        cekUser()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        updateUI(account)
    }

    private fun cekUser() {
        val sessionManager = SessionManager(requireActivity())
        if(sessionManager.fetchAuthToken()!=null){
            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
        }
    }
}