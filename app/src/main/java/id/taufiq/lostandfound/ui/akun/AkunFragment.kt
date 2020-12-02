package id.taufiq.lostandfound.ui.akun

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.api.ApiClient
import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.data.remote.UserRequest
import id.taufiq.lostandfound.helper.Constants.REQUEST_CODE
import id.taufiq.lostandfound.helper.SessionManager
import id.taufiq.lostandfound.helper.Status
import id.taufiq.lostandfound.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_akun.*
import kotlinx.android.synthetic.main.fragment_akun.view.*
import java.io.ByteArrayOutputStream


class AkunFragment : Fragment() {
    private lateinit var viewModel: LogoutViewModel
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var updateDetailViewModel: UpdateDetailViewModel
    private var img = ""

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
        getDetailUser()

        // Nav host fragment
        val host: NavHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.container) as NavHostFragment?
            ?: return
        // nav controller
        val navController = host.navController

        // Setup bottom navigation view
        bottom_nav_id?.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    img = if(Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPhotoUri
                        )
                        convertImageToBase64(bitmap)
                    } else {
                        val source = activity?.let { it1 -> ImageDecoder.createSource(it1.contentResolver, selectedPhotoUri) }
                        val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                        bitmap?.let { it1 -> convertImageToBase64(it1) }.toString()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            img_profil.load(selectedPhotoUri){
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(LogoutViewModel::class.java)

        detailViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(DetailViewModel::class.java)

        updateDetailViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(UpdateDetailViewModel::class.java)
    }

    private fun initAction() {
        btn_logout.setOnClickListener {
            logout()
        }

        btn_edit_foto.setOnClickListener {
            Toast.makeText(activity, "Edit photo", Toast.LENGTH_SHORT).show()
            openGalleryForImage()
        }

        toolbar.setNavigationOnClickListener {
            Toast.makeText(activity, "Click !", Toast.LENGTH_SHORT).show()
            visible()
        }

        btn_edit.setOnClickListener {
            Toast.makeText(activity, "Edit profile", Toast.LENGTH_SHORT).show()
            inVisible()
        }

        btn_apply.setOnClickListener {
            Toast.makeText(activity, "Apply profile", Toast.LENGTH_SHORT).show()
            apply()
        }
    }

    private fun apply() {
        val name = view?.input_name?.editText?.text.toString().trim()
        val email = view?.input_email?.editText?.text.toString().trim()
        val password = view?.input_password?.editText?.text.toString().trim()

        if (name.isEmpty()){
            view?.input_email?.error = "Nama harus diisi!"
            view?.input_email?.requestFocus()
        }

        if (email.isEmpty()){
            view?.input_email?.error = "Email harus diisi!"
            view?.input_email?.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            view?.input_email?.error = "Email tidak valid!"
            view?.input_email?.requestFocus()
        }

        if (password.isEmpty() || password.length < 8){
            view?.input_password?.error = "Password tidak valid!"
            view?.input_password?.requestFocus()
        }

        updateDetailUser(name, email, password)
    }

    private fun visible() {
        toolbar.title = getString(R.string.akun)
        toolbar.navigationIcon = null
        btn_logout.visibility = View.VISIBLE
        btn_edit.visibility = View.VISIBLE
        text_name.visibility = View.VISIBLE
        text_email.visibility = View.VISIBLE
        img_profil.visibility = View.VISIBLE
        input_email.visibility = View.VISIBLE
        input_password.visibility = View.VISIBLE
        input_name.visibility = View.GONE
        btn_apply.visibility = View.GONE
        btn_edit_foto.visibility = View.GONE
        input_name.isEnabled = false
        input_email.isEnabled = false
        input_password.isEnabled = false
    }

    private fun inVisible() {
        toolbar.title = getString(R.string.change)
        toolbar.setNavigationIcon(R.drawable.ic_cancel)
        btn_logout.visibility = View.GONE
        btn_edit.visibility = View.GONE
        text_name.visibility = View.GONE
        text_email.visibility = View.GONE
        btn_apply.visibility = View.VISIBLE
        btn_edit_foto.visibility = View.VISIBLE
        input_name.visibility = View.VISIBLE
        input_name.isFocusable = true
        input_name.isEnabled = true
        input_email.isEnabled = true
        input_password.isEnabled = true
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun convertImageToBase64(bitmap: Bitmap) : String {
        val byte = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byte)
        val byteArray = byte.toByteArray()
        return " data:image/jpeg;base64,${Base64.encodeToString(byteArray, Base64.DEFAULT)}"
    }

    private fun getDetailUser() {
        val sessionManager = SessionManager(requireContext())
        val id = sessionManager.fetchUserId()
        detailViewModel.getDetailUser(token = "Bearer ${sessionManager.fetchAuthToken()}", id).observe(
            viewLifecycleOwner,
            {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            if (resource.data?.isSuccessful!!) {
                                visible()

                                img_profil.load(resource.data.body()?.image) {
                                    crossfade(true)
                                    placeholder(R.drawable.ic_cancel)
                                    transformations(CircleCropTransformation())
                                }

                                text_name.text = resource.data.body()?.nama.toString()
                                text_email.text = resource.data.body()?.email.toString()

                                view?.input_name?.editText?.text =
                                    Editable.Factory.getInstance().newEditable(
                                        resource.data.body()?.nama
                                    )
                                view?.input_email?.editText?.text =
                                    Editable.Factory.getInstance().newEditable(
                                        resource.data.body()?.email
                                    )
                                view?.input_password?.editText?.text = Editable.Factory.getInstance().newEditable("")

                                Log.d("test", resource.data.body().toString())
                            } else {
                                Log.e("test", resource.data.body().toString())
                                Toast.makeText(context, "Load data Gagal!", Toast.LENGTH_SHORT)
                                    .show()
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
            }
        )
    }

    private fun updateDetailUser(name: String, email: String, password: String) {
        val sessionManager = SessionManager(requireContext())
        val id = sessionManager.fetchUserId()
        val request = UserRequest()
        request.nama = name
        request.email = email
        request.password = password
        if (img.isEmpty()){
            val bitmap = img_profil.drawable.toBitmap()
            request.image = convertImageToBase64(bitmap)
        } else {
            request.image = img
        }

        Log.e(TAG, "image : " + request.image.toString())

        updateDetailViewModel.updateDetailUser(
            token = "Bearer ${sessionManager.fetchAuthToken()}",
            id,
            request
        ).observe(
            viewLifecycleOwner,
            {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            if (resource.data?.isSuccessful!!) {
                                getDetailUser()
                            } else {
                                Log.e("test", resource.data.body().toString())
                                Toast.makeText(
                                    context,
                                    "update data Gagal!",
                                    Toast.LENGTH_SHORT
                                ).show()
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
            }
        )
    }

    private fun logout() {
        val sessionManager = SessionManager(requireContext())
        viewModel.logoutUser(token = "Bearer ${sessionManager.fetchAuthToken()}").observe(
            viewLifecycleOwner,
            {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            if (resource.data?.isSuccessful!!) {
                                sessionManager.clearLoggedInToken()
                                findNavController().navigate(R.id.action_akunFragment_to_welcomeFragment)
                                Log.d("test", resource.data.body().toString())
                                Toast.makeText(context, "Logout Berhasil.", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Log.e("test", resource.data.body().toString())
                                Toast.makeText(context, "Logout Gagal!", Toast.LENGTH_SHORT).show()
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
