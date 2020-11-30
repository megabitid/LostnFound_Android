package id.taufiq.lostandfound.ui.akun

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
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

//        val toolbar : Toolbar = view.findViewById(R.id.toolbar)
//////        toolbar.inflateMenu(R.menu.profile_menu)
//        toolbar.setOnMenuItemClickListener {
//            onOptionsItemSelected(it)
//        }
//
//        isEdit = false
//        edit()

        // Nav host fragment
        val host: NavHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.container) as NavHostFragment?
            ?: return
        // nav controller
        val navController = host.navController

        // Setup bottom navigation view
        bottom_nav_id?.setupWithNavController(navController)
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

        btn_edit_foto.setOnClickListener {
            Toast.makeText(activity, "Edit photo", Toast.LENGTH_SHORT).show()
        }

        toolbar.setNavigationOnClickListener {
            Toast.makeText(activity, "Click !", Toast.LENGTH_SHORT).show()
            btn_apply.visibility = View.GONE
            btn_edit.visibility = View.VISIBLE
            visible()
        }

        btn_edit.setOnClickListener {
            Toast.makeText(activity, "Edit profile", Toast.LENGTH_SHORT).show()
            btn_apply.visibility = View.VISIBLE
            btn_edit.visibility = View.GONE
            inVisible()
        }

        btn_apply.setOnClickListener {
            Toast.makeText(activity, "Apply profile", Toast.LENGTH_SHORT).show()
        }
    }







//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.profile_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here.
//        val id = item.itemId
//
//        if (id == R.id.edit) {
//            Toast.makeText(activity , "Item One Clicked", Toast.LENGTH_LONG).show()
//            id == R.id.apply
//            item.isVisible = false
//            return true
//        }
//        if (id == R.id.apply) {
//            Toast.makeText(activity , "Item Two Clicked", Toast.LENGTH_LONG).show()
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

//    private fun manageMenuOption(view: View) {
//        if (isEdit) {
//            isEdit = true
//            activity?.invalidateOptionsMenu()
//        } else {
//            isEdit = false
//            activity?.invalidateOptionsMenu()
//        }
//    }
//
//    override fun onPrepareOptionsMenu(menu: Menu) {
//        if (isEdit){
//            if (menu.findItem(tes)==null) {
//                val menuItem : MenuItem = menu.add(Menu.NONE, tes, 5, "Share")
//                menuItem.setIcon(R.drawable.ic_edit)
//                menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
//
//                menuItem.setOnMenuItemClickListener(fun(it: MenuItem): Boolean {
////                    when (it.itemId) {
////                        R.id.MenuMoreAbout -> {
////                            requireActivity().openActivity<UIAbout>()
////                        }
////                    }
//
//                    Toast.makeText(activity , "APALAHHHHHH !!!!", Toast.LENGTH_LONG).show()
//                    return true
//                })
//            }
//        } else {
//            menu.removeItem(tes)
//        }
//
//        super.onPrepareOptionsMenu(menu)
//    }

//    private fun edit() {
//        if (isEdit) {
//            actionBarTitle = getString(R.string.change)
//            toolbar.title = actionBarTitle
//            toolbar.setNavigationIcon(R.drawable.ic_cancel)
//            toolbar.inflateMenu(R.menu.profile_menu_apply)
//            btn_logout.visibility = View.GONE
//            text_name.visibility = View.GONE
//            text_email.visibility = View.GONE
//            btn_edit_foto.visibility = View.VISIBLE
//            input_name.visibility = View.VISIBLE
//            isEdit = false
//            if (!isEdit){
//                toolbar.setNavigationOnClickListener {
//                    myMenu?.findItem(R.id.apply)?.isVisible = false
//                    visible()
//                }
//            }
//        } else {
//            myMenu?.findItem(R.id.apply)?.isVisible = false
//            visible()
//        }
//    }

    private fun visible() {
        toolbar.title = getString(R.string.akun)
        toolbar.navigationIcon = null
        btn_logout.visibility = View.VISIBLE
        text_name.visibility = View.VISIBLE
        text_email.visibility = View.VISIBLE
        btn_edit_foto.visibility = View.GONE
        input_name.visibility = View.GONE
    }

    private fun inVisible() {
        toolbar.title = getString(R.string.change)
        toolbar.setNavigationIcon(R.drawable.ic_cancel)
        btn_logout.visibility = View.GONE
        text_name.visibility = View.GONE
        text_email.visibility = View.GONE
        btn_edit_foto.visibility = View.VISIBLE
        input_name.visibility = View.VISIBLE
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.edit) {
//            Toast.makeText(activity, "EDIT", Toast.LENGTH_SHORT).show()
//            Log.e("TAG", "klik")
//            item.isVisible = false
//            isEdit = true
//            edit()
//        } else if (item.itemId == R.id.apply) {
//            Toast.makeText(activity, "Apply", Toast.LENGTH_SHORT).show()
//            Log.e("TAG", "klik")
////            item.isVisible = false
////            isEdit = false
//        }
//
//        return super.onOptionsItemSelected(item)
//    }


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
