package id.taufiq.lostandfound.ui.akun

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import id.taufiq.lostandfound.helper.Constants.IMG_DEFAULT
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
            openFile()
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

    private fun openFile() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/"
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
        if (id != null) {
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

                                    view?.input_name?.editText?.text = Editable.Factory.getInstance().newEditable(resource.data.body()?.nama)
                                    view?.input_email?.editText?.text = Editable.Factory.getInstance().newEditable(resource.data.body()?.email)
//                                    view?.input_password?.editText?.text = Editable.Factory.getInstance().newEditable()

                                    Log.d("test", resource.data.body().toString())
                                } else {
                                    Log.e("test", resource.data.body().toString())
                                    Toast.makeText(context, "Load data Gagal!", Toast.LENGTH_SHORT).show()
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
    }

    private fun updateDetailUser(name : String, email : String, password : String) {
        val sessionManager = SessionManager(requireContext())
        val id = sessionManager.fetchUserId()
//        val img = Coil.get(imageUrl)
        val request = UserRequest()
        request.nama = name
        request.email = email
        request.password = password
//        request.image = " data:image/jpeg;base64,/9j/2wCEAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDIBCQkJDAsMGA0NGDIhHCEyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMv/AABEIAZABkAMBIgACEQEDEQH/xAGiAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgsQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+gEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLEQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/APf6KKKAEIBGCMiuZ8Q+GUv4nltolN3JMrM7NjC4wfw7109FJq407O6PEJYZInZGBBBIP4HBrldasX+2xz2wXzd2WU8A46GvcfEvhn+0R9oswqzhduzoDlsk/Xk15Nf6fc27wPcwPGJgXQt/EORkVlZwd0dMZKorMwPN1hP+WKn8R/hSfbNWH/LBP++q3YYpXkESxs7HoFGc1NNaSQNiaFo2PZ1INS5LsaKPmc59u1b/AJ91P/AhSf2hqo62in/gQroFt97BVXJPAAGc0+fTp7bHn28keem9SM/nU8y7Ds/5jnP7T1IdbFfz/wDr1mhr9L83ItOCSdgIxk4z39q60w9PlqRrGRIhK8LrGejFSAfxpqdtkJwva7Oa/tG/zgWLj6D/AOvWVrNxczLELiBogCSMjGeldoygDAIrmfE6n/R/+Bf0qqTTlsRWTUdyDSb65t7EJFZmRckhgw5rR/ta/HXTX/z+NN0JVGnJlQclufxrWEUZ6AVnUaUnoaUovlVmZR1u7H/MOl/75/8Ar0g1+4/i0+b/AL5Naxt1I6CmeTyAVz+FRzR7GnLLuZZ8QEfespv++T/hR/wkiDrayD65/wAK0nijU4YDP0qPYG6J+lK8ew7S7lH/AISaHvCw/P8AwpreJbU/wkfUn/CtB4jEfnUqfQjBphSN+rii8OwWl3MePXrcanLcMpCtGqDnuCf8aur4lswO/wCdWTAi5+RQPXHWmmOI9Yk/75FDcH0ElNdSH/hJbP8AyaX/AISWz/yaf9kh+8YU/FaT7LaHk28JP+4KLQ7B7/cT/hJLL1P5j/GmnxHY/wB5vzH+NSf2fayDItofwjFMOm2g628X02Ci0OwfvBkevWQeRi5+Y56jpge9Sf8ACQWH94/mP8aSPTrNmkBtoj839wegpH0yzPyi1i/74FFoCXOKdesD/wAtf5f40xtbsCP9eKY2l2o/5dY/++BUTaba9reP/vkU7QBuZX1PUrOexmSOZWYrwPet2yHn+H7B8c+SF/Lj+lY7aVbhC5hQYHA2iui0+MJpFqoGAF4H41pC1rIwmne7M27TZFjGOn8qziK19TXaSP8AdH6VlEc1SIZmagP3qf7tVgMVav8A/Xr/ALv9arYq0ZPcaByeacAfWkFPAoYzpPCKZku29Ao/n/hXTMvFc/4PX5bxv9z/ANmrpXXArOW5cdijIOarsOauSDmqzjmkUVZBk1AasSioWHFMRBgZpMU8jmkoAYaTFPIoxQI+pqKxNYlv1VkivLS1WT5YzKTuJ46HPHf16e9cjBqHjCS8VW+aGzkMcpiAYMwXdhuec8DPbNdVjklUs7WPSaK5a18ZLeX0MNvpl60Er7BO0ZA4OCfpnHXFdQDkZpFKSlsLWXrOh6frNuiXqHEZJR1OCvrg1qUjAMpB6HrQUZek6Bp2io32SLDvw0jHLH2zV26sbW+i8u6t45k9HUGp8DOcc0DkcUrId3uULTQtLsJfNtbGGOQdGC5I/E1ZubO3vIGhuYUljbqrjIqeiiyFcw7fwhodtOJksgWByA7FgPwJrXmtobiBoZo1eJhgqwyCKloosh3Zyr/D/RGm34nVf7iycfyz+tcJ8Y9LstO0jR4LO3SJfMkztHJ4Xqepr2WvJvjf/wAeekf78v8AJacUkxSba1JPA/w/0zUPCWm3081yJJkZnVWAH3j04qZNPttNmljto9oyVLHkkehNdV8P12+A9IA/54Z/U1z91zcy/wC+f515+OVkmj1crd20zDudAs53LqZISe0ZAH5EH9Ks6dYQ6XKJrYt5w4EjHJH07CrhFNNedzS7nsKnC+xR1DTbfUrhrifd5zfedTgt9e1R2mmW2nTC4hUmaPlGkw20+uOlaBqOT/Vt9KOeXcr2UOwmrg62ii/PmFM7XAAYfjisaPw3ZI25y8g7KcAfpW2eKaaHUn3GqFP+UhvoYdQtkt54IfLjGE2RKhX8QKxx4atVfPmSbfTj+dbhppo9pPuV7Cn/ACjEWOPTDp4t7c25HOYlLfXdjOffNVtG8D2ur6oYftDRRKhcgJk9QMZz7+lWT1rpPBY/4mk59IT/ADFbYeUpVEmzlxlKEKEpRVmbFh4K0HT7cxLYRykjDPMNzH8e34YrlNV+GkF3qb/2ZMIIgPmWTJAPoDXol1OLeEt/EeFHvVawjlaLdI77Tyozj8eK9d04vSx80qk073OT0n4XaXaKzX8sl07HO1SUUflz+tM1L4X2cr79Oujbg9Y5F3j8D1/nXfbQPX86rXdwUxDFzM/T2HrR7KFth+1ne9zzzT/hvai8Zb27NxGn3hGNij6nOT+lM8SeB9NlgWfSYhayDP7ssSr4+ucGuxv7iLT7Qxhhnq59T6VnTGUaZCZhtcgsQfcnH6YoVONrWE6s73ueM6jpOo2xZJLOT6qNw/Sr1pGUsIEZSpVBwRgiup1FySawpOcms+Tl2NXUctzC1ldrZxwSP5VjHmul8RxeXDF7kfyrm6EBl33+vH0qvirF9/x8fhUAHFWjN7jVp4+tNA4p49KAOu8Gr/o92f8AaUfoa6N14rD8Fp/oNyf+muP0FdC61nLcuOxRlWqrrwavSLVVxxSKKEo5qFhxVmZe9VyKYiEikxTsc0YoAZikxTyKTFAHo8V3B4ovLW3vNbnjlEaRiNlDbpGJ3bTxxwOvQkdhXQabDYeH76W3E08clzP5ELNhwcEZ7cYyM568e4rI8V+F7ptdtIrTybOxd90csYwYj36YwNxH0z1qSbR/EtlZebf6uWtY0AaRgGIDD5yM8nGOCeST2xXU7HnKLOya70nWGbTba9UzcyBoG5Ugg5BHfJH61uKMKBnPvXlUniq307VoZ7e0jecw7HuUTAc5HIXsCAOnTcfpW1efECaEmWGxzbMQY2l+Ukcce5zmockbKSO8rG1nxDa6MF8+K4cFgpaOMkLkHGT07VzWoeNr6bSIVsbKRL2RisjcYjK8nGevAJ9q5W+8RvrsZk1GRYbm2wkRhQkE5+bcc8HBPbqKTlpoDl2PZIpVmhSVT8rqGB9jSyFhG3l43Y4z0zXnSeF7+5N4sGoXe1ED2xB2Rtk5wMEkjHTHr+b73UvE93Z29ssIgZbgQieJypdxkfgPU4o5+6Hc6y58Q2umxJ9vkUTFgrJFl9pxnn/Pp61n2/jzS5pNsizQDZu3OAQfYYPJrzbU9J1eHVUtLhS13ccgq29m59fwrZT4f6oNM8+S6WOcJvWJjj5icMCc9MAH8fxpXkSpN7Hbt420UWzTLO7heqrGSe/4dvWqUXjhbyaaOys2mAH7pgfoAWHXGSOmaytD8JSK11b38Ec0ZCMk0YOT16E4BHA9aNe0a70GxnutOnlt4ScMm8Y2kgBQPxPJJqbztqXqdVqGtQxadKgvYYL4R5Kgb2Q8ZO3rjn071418SfEZ1q20uLeshh8ze4UjLZHsB0A6V1OgeHr/AFK+kmuJY4Z1TJhlHzSIwxnPp/nvXHfEfR7zSrm0FyzlHDFA0pcA4XdjPQZNXB3aYmz1n4a332zwPYLsCGFPLxuyTjv7d+K2H8O6e7lij5JyfnNcB8M21aOytIre2hitJIwzSuGHmYPOOoz19OleqjpzSajP4kaQqTh8LsY58Nad/ck/77rnZfD8s2oXSW920cUb7VUoD2Heu5PQ1mWS/wClXZP/AD1NQ6NN9DZYqsvtM5hPC92ygm+b/v2KR/Ct0VIN8cH/AKZiuzXasAZuBjk1yGv/ABB0XQ5Nkskkr5wEhTcSalYen2K+uV/5mVJPDmoAqFvI8E45h9j700+GtS/5/If+/J/+Krl5/jSzXax2+gmRQ2QTccnjGcKpH61JF8brQ8XOi3MZH/PKRX/nih4Wn2H9dr/zHQHw7qg/5e7f/vyf/iqhGg6qWcfabX5Wx/qW9Af73vUdj8XvDF0Qtw11ZMT/AMtodw/NM10+m61pOrGV7DULW5+YHEUgJ+6O3UUvqtPsP6/X/mOabQdXH/Le0/79N/8AFV0/hbRrzTZXuby4tm82PCrGpBHOecmrbew/Or2mIpRuOQeuf6VUKFOMrpE1MZWqRcZS0GSg3t4Bx5Sf5NXICZCzg4j6KB3x3pZIwf3aALv+8R6U/wApMg7RwMCtzlCWQRxlup7D1NZ80v2SJppW/fMOSf4RWky5U4AJ7ZrDbTrq/viLtQkC88NndSYFbTbB9UuftlyuLdD+7Q/xH1pfER2vgcAKK6REWNAqABQMACuW8RtiV/pVITOGv25NZD9DWpenJNZrDORWUjZbFTxYm2C3+p/lXJmuw8YjEVrj1b+QrkCKkpGVef8AHwfoKg7VPef8fLfQfyqHFUSNXJGaeBSKOlOAoEdz4KT/AIlM7es5/wDQVrfdetY/gtf+JG59Z2/ktbrrUS3LWxSdeKpyrWi6+1VJlz2qRmZKO1V2HWr0q1VccGgCsRSYqQim4pgMxRin4pMYNAHSWmpPeytL9quhMzqVnEhygHXJ7jkHHqBXa6Z4/Ju3t9YiT7O4JVkBOBnHOQOOCa8vikAmBh4CHKoDnPr+lX7SdZEInlwQu1MH5vWqc3E4Ytnp9zB4Vura5uE+zTzJ5k3zjH3Rg8AdgRj86zbXWry+kEN5YQPAsAaLdEdsIAGJPmOGXPXnOBXn7yoJAqsFZiQFJJAFSLexW9s0aTsZXHVAf++fpzR7XyC7Jrv7ONYniM7XEIZlWXPbgBuMc4/z3q9dX1jDBbWmmQRgLCrS3DKA5l6Hufrjjk1iyKlwF81mjJXGSOvvVJoruK0yyOqnA2t3PWo5rvQNjp4vEus6beLI90GcMzhXOQMk5AHTB64HoPSrdz431yVgtsyxwKyyAKqKCo5IJI7nn+hrhvOmdkEifJ0yatPGyRySy3OAg/uE5JBIXt6dfY+lUuZdQuzX0/xbqWk669xNfNOJnLTqMZJz0GRxjjp24ruPC+pa/dXUk13pVxdWsjMI5XkAKAsfXHTGOMVy+h6Hf3dulxJpAuLe8ix5hAbys5G9Rng8Z/L1r0bwhoq6VFOywzwBmwkcj7sLgHH1DbuffvWkZN6DjHqdMqhVAHQDHPNV9QsIdSsntZ8+W5BOOvBB/pVhpFVlB6scDj2zTJp0gj3uTtzjgZrQ1CO3jikLqoBIA4HYV5F8bSDc6Uv+w5/UV61b39rdOyQTxyMoyQpzx6/SvIPjY5/tPTVx0gY/+PU47iZ6X4OQL4N0YAD/AI84v/QRW5WN4SGPB+jf9eUP/oArYJqegxG6Vn2R/f3Z/wCmpq+xFZEc4t4r6XPIkfA9TUgc94x8QNZ2cdrASXZ9rYPI4yT+o/P2IrwXUNSe+1GSZo93GxEDHAA9T1Oep9T144rv/G199mtuZcz3D5PPYFgf1P6V5fLMEJA/GrigBpDESWkbeeoXjNV2fPLcVXMpZy7cnOaYzFjk80wJ947c0CXawYcMOhHBFQeYeM8D2pM59qAOl03xt4h0vAttUnKD+CY+Yv8A49nH4V6H4V+NL286Qa9aL5BGDcWw5U56le/4enSvGhs/vj8aUZHINAH2Fo3iLSfEEHn6ZexXC9wpwy/UHkfjWhHMskrovOzGT2zXx1p2q3ulXQuLK5ltph/FGxGfr617j8PviZa3sAstTmhgnBAUSSbS59QTwe3BOfrQB6xRTY5FlQMpyD7Yp1IArkfEZzPJXXVxviAkzyfU0COKu+pqgR86j1YD9av3Q+Y1TVQZox/tr/OsmbLYq+Mx+7tPq39K45hzXZeNePsg/wB/+lccRzSe41sZF2M3LVD2NT3X/Hy/1qE/dNMkFHFOApF6U4UgPRfBSf8AEgz6zN/StyReayvBaY8NxnHWRz+tbjr14qJbloz3HNVZVyK0HSq0q1IzKlWqjpxWjMtVHX0pgVCtMIqcrTSvFAEOOKTb1qXFG2mBfuPD8cemTara3qRxIwaKOU/vJF4CsMepJ+hGKyUdo5iJTtIGcdxx1qN7mbTo5VtpdsVymxkHzYXdkfQ5Gf8A9dLb6ZqF5p11f7GEUSgs7Hna3AJ9iR16ZreUUzhs0M3efvk3HgZXJxUkKzMY2SF5SAcuOnHv7VRiuSkTLtJG3B96mt2eOBmjdo1z2JHJBGMe4OPoTU8oJFxLiRyjNJlF5PHI9/8A61T/ANqwzWjWvkM5OSGHXdng/THasSWcEqNrAH/a4qPzDERtkO7tnjFT7Jbh1OsbWNJvLYz3VoBfQgIqodqSgcL8oHAA68jOOtZN/q8t7dIZBsSMBVAbcApJbHv941hmQliSRgU8jo2ST2x3q2gOg07xFc6dums5pYm+4GU42r6Ctr/hNNcaBAdWnLLnIJwWJ5xXD5VGBxjPOCOKlS6MuEBVVzuBK89PWs3F9BanqVj4/wBSisJVu51eRgArMoyhweT6/wAJ/A1zd/4iv71hDczS3AwVUlyOv0PHQVziXZkDysU3EY3H+X5U+G6MTHgMW+Y5b2qHzdytTu28aXyw2sMZkhZP4sAEDnjnPr3zXMfETX3129sJHAVo7YI2O7bjk1X/ALQh3ZGxsDcdx5H0rF8QTLJMjLjAQDiqoylz6jTPpDw3dxxeCdInUh0WyhBKsP7qg8+39K15JIIx+8lC/V8f1rwLw/4gvI7Wz0tLhxbN5atGvQ5x2r31o1PLfMAOh71opX0LTuMVo3H7vc3/AAI8frXIeI7qSDTr9YCQyuzseTg84HPqcV0WoHTrCB7uZ0hVBkvv28V4r4r8dEtdWmmzNJBMzbpHUHIJ6DI/WqSGcXquo3F3cF55S7ZJGe2TWKytIx4rRgt5b6bgFvXAro7LwvJIoZ12D8aJ1FHc1hSlPY4gWsjHAU804WE5ONhr0lPDlvGemamXRrSIZEKlvWsHiUdEcE+p5ymiXLjOwkewpsmjToCdp/LFelfZ1AwqbRUE1qCpG0YNZ/WWa/Uo2PMXjeA4aP8AMU3O7kDFdre6ZE+coKwbzSNis8eRjtXRCsmctTDyjsZG7cOacjlTwT7GmOjITnrSBu9bHOe1fCTx5P8AaRoWoM8yNzBKTkr224xyMkY9K9v618feGpxB4j092n8hfPQGTP3ORz+B5/Cvr2FmaFC4AfHzAdM96QElcVrxzcS/U12tcTrZzPKfc0w6nH3Iyx9qrRrm6hH+2P51cuByarwL/pkP++Kxe5qtih43H720H+yx/lXH4rsPG3/H1ar6Rk/rXJGpY1sYd3/x9SfWoSPlNTXPNzL/AL1R/hn2qyWNQhlBHSpBUFqd0IHQg4qwKTA9Q8GL/wAUxb/77/8AoRrcdax/Bg/4pa2/3n/9CNbrDNZS3LRRkSqkqVpMnWqkqflUjMqZOTVRk4NaUqdapyL1p3GUCKZjmrDJTSlAiHbSbal20baYEy28M++K9W3hkYoFaeaUPHjAxJ8uB8uAPu/jWKdUu7JzZw6jK9n8wQKfldSc9PQkZ5qTWNYuNZt4kcRoA7OTuI3s2Mk5JA6Dpip7Se2fRby3mihCtCPLyCCsi9wecnkjHTBPTNdCZyJmcsEkiSRhTmMneVG4RgHGSfc8VtR+FZJtEtL59QtYjL84iknMbbd23oRx0Jzz9Kx7K9bSLZXikkBmcCWPOFkVWDfzHf3rQu/FGo+Ib+KG/vkt4nKoSiHYigkrx1wCf69qHceiJZPC+vNqBgFvdwQsqzlRIZWhjZiADjG4+wGe+KzbxhDbQoVmRjECS8+5mP8AeGMAD0HPfOa7eXxteXEkmmDW7Mwi3CfaHLR7nXJ3Bip64A55OePWvP8AVZbS6uJZ7UlC5BKbeMnryT+vfNLUmVuhCPspbfmQNt43MCM8f/ZevakQC61DAYQRk9FyQPYZqlIPkUZG7cV4Hb1zV1NPkCxyeYCGGTjqOB29OcU2ShbiEK5TzMqnALDnHvUUKbpDHjLEfKOn+eM064RADh1JUY471G1xLbHaMD8ODSW1hlqMsAI2Kqr8kA80xWcKyK2+VOMg84+neqbO0hO89eeBj34xTGk2pjJpcoFiMjzgZOUA5x1pmplcAqMA9s9KS2RrqQxq4Ut0LHAzTtWjEW2NZFkCqBuX1px+IaNzQzANS0yLZhmliO4Hvkf5/OvpS8uYrO0luJm2xxoWZsdAK+XtBJk8R6V/d+0Qjp33CvonxfMieFdQVmw0kLIoHUkjGBSUbMuOx4Z408V3Wv6rK7yMtqrEQRZ4AzjPueOtcjta4uEiXlnOAKsXuGmdgML/AAjOeKt+GbYTazG787RurST5Y3LhHmkkdppGjx2VunyDdgZraWJcYxihF+UdKdnFedJt6s9mEUlZEEyrGCe9Vi+UOFJNXJUD9uarhOTWLNSlJnaSpquxbnOePWtZoMj2qhOscZO9lUAdzjFCBmVP8xOelUplXYasXl/YxcNcx89MHNZE2q2hYgS8HgZFbRjIwnOPcyNTtRksuKxTkHBrp5Csy8EMD3BrBvIfKmIxwa7actLM82tDXmRCp5r6G+EPjp9asf7C1B2a9tI8xyu2TLHnGD7jIH5V87jjit3whrTaD4p0/UA5RI5QJSO6Hhs/gTWpzn19XDawczSH3NdBPrXmRKLALKzAHzD9xc/zrndSJZmJpDOauByahth/p0A/26szr8xqO1TN/B/vf0NZPc1WxjeNf+QjAPSL+prlWXFdX4yGdUj9oh/M1zDLUvca2ObuP+PiT/eNAAWNncNtx/CQDSy83TgDd8x4/GptQ07UIbVJpVRY3fYMKeTjPrWiIb6GTDIImYHcQTV9TkZHSqrWd4Lv7OSqvjPPAqytpPaSGO4YM2AQQcgj2pNCTPWPBY/4pe1+r/8AoZroNua5nwdMf7DtoscDcfzYmuqAzWEnqbLYqulVJlrSdetVJF4NQMypk61SlTANakqjNU5k7UAZxSmlKtNHTClVcCsUxTdlWClIVpiObezHlvLDIGj4yrHlecflUdpE87lJEZY4xuYjgKAef8+tQxTizdJI8svIZSeGB7EVZvrpBDElrlYpSWfdnJOOBx6ZNdG5yWW5TuhJPcO5QKgAVcDCqOwqJYyX3FSBjr6HHT+dXI9QkhQR71D46kdfXPYj9fSmpfFp2W4YCOUguUJH+f8A9dapKy1M+d3d0UngknlIjy2TjLc801VKR/PztbDAdenFatxHJ5MIMhC84VWxtxnOB2/+vUMdqkLK8+SZBiOM8bvc+2f5VMtHYcXzFSIDcs83CZ+UY+9j+lK128k21jthzyEOMj6n/wDVTrlftDKWBLMcDBxjtgD+lMEbrKsSJtJbYCemfr07ipuUQSSGL7owCe/PFR7mlAJ9OKu+fFbgxuPMYsV3noBj069/0qMI843SLhG+bcq4/LFK4rlV42KK4OFyRz64zSxtvj27C3HB5qcgKCxXhjjnpU4hdYyFYMoGWI6gHH4frQ3YLFNXWBsGMc9Ae1JdtvhVuPm54qWcQvcqYkbyVwDuPJPeoLhswLhAuOMCnF6jR0HhyH/iodHJVhm6hPJ/2hXqfxX1tbLSYrQMfOuCR04Cjr/MV5h4Tu8+JdLi8tCGu4VyRzncOldb8bo2XU9LfOFaFwAPYjn9f0pR1lqWtjy+WbecdQa2/C5/4mDf7uBXMhszDniuk8L7v7Qc4ztXNOr8LNqPxo7q51i002BHupdpb7qgZJrIk8bWoJEVvKceoH+NRpoZuZ2uLwbpWOSD0HtU82lafEu1xErAdM4NcjcFo9TvXtJarQWHxbBcOB9ncL3boR+Fbcc0Txq6n73NckbW3VyYdp+hrQsJHyI+QO3tWM1F6o3puX2jYnulSJ9vWuI1aSa6LKM4Lciul1UG2jHOc1hNGI4nuJf9Wg3GnSdtRVVzaGXa+H5LkhmO1f1qxN4ehiHzkt+NSXep3draRSskcEcys0XmF+QPZR/M1lrq17KWYoGCnB2Mf61v+8epyWpJ2BtPe0kLQhtncE5qpqcO+ESAcit61m+1xZKMDjnNUL+DYGGOMZFOM3fUJ01y6HLk4NKvUbeTRKAHOOlNQlWBBwR0rqOBn1n4Py3gbSbkxF5msoiQeOdoqjqC/Mal8DapY6t4Ws3gMgeG3RZY/LwVOO5xznGeO2KL5QSTQwRzc6ncabZp/p8X1P8AKpLyaKG6ggbO+fds44+UZOafZR/6cnsDWPU16HN+LhnVh7Rr/WuaKEGur8VLu1ZvZF/lXOsg9Kl7lLY5izdYdbjkddwWbdt9eeldb4p1ePU7PSQ0O0CaYbQR12x+1cTKxW5d1+8HJH1zWpLr1pGtlLEjyG2uPMZGUYAK4YZ9eB+Vap6WMZLUt+Jo4z4lsxbxBS0PODncd5qtrFpJA0DSIV3q2Ce/K/41Tn1+O98QxXyxSbI0wF4J+9mrGp63JrM0ZKMscK7FDDBz37+w/Kl0A9B8HRE6Rb8fw5/U11ax4Fc14Gm+0aJEfLKhPkBJ+9jqfbnNdeEGK55rU2WxUePiqkkZrTZMiq7pxzUlGRLHz0qnLHnNa0qc1TlTnpQBmNH1qMpzV10qIpQBVZOKYUq0ydqaY+KdwPOVSeeaNI1J8wjaMfzqW2JuLQqN26Nw4A6kVNb3BEqvxt5zgYDA9f51ct7aG2t9hmUMzEIM8vk8fiMfrXWmmrdThi2mZwsbiaCS8ER8lep68DrVZCjXCo7YBGB7Hsa6CzvLW00orcq0m3IlVMEr8x7ViafCJP8ATJAXjhyRn5i3fGO575rWpBRipEwk3Np9C9aSHTLcz3K+bID8kT8jn+L+f5VSaZ55DLKWMpy7HOeKW+d7ieKXbyy7jx0PH6VCbfYkiu4Lu4BIHp2rK66mjsgWdTMrxkrkYPG7II6YNTWokJzKdvOSHOeMAdPTgVXyLOMl9oY/dCcn65pIiZQDtZufSpeorEt0loihliZm/i5IAP1/+tTPtMixqGjKwr8oXJx+H506aMmUp1Gfl+YDP50wxsFxJsyvQBs4oWwtQa481fLEMfX5SetTwxpFCzu0byYJPLEfQcdeKhITYRuAHqO3NBlZeCB/vY/nUtX2EQSyvJgbcIDgAH3P+JqO5wIAAen+NWohEzo07MqdGK8kfnVW6UJAApyPXrWkCkbvg0n/AITDSAMYN7CMEf7Yr1z4s6AdS0H7dEC0tqrN9F6k/wAq8q8HhP8AhLtFXPzfboSOP9ofpX0pcW8dzBJDMoaORSrA9waXUuOx8dAnzDx07V1Xg1g+rOh7oOPxp3xB8PvoHiy8BQJDcsZ4MDgqT/jmovBKMfEEWBwUYn8qKmsGa0dJo63WLuS1iKwpJJIfuqgyT/h9a5u8GptbxyFp2Z9weC2Jj2ccc4JNegyW+fmIwSOMVQntGclQ7AHrgVwRmonrSpOa3OE06zuHRzI9yk24FCxyffNdjYxHyYvMQCXHzEdKt22mxockfn3q2Ihv2qvTrROfOVTpezW5ia2Q0ZB7cVixKLi3NtLlot4Ygd8VvazGSCAK52CUxXIRuhNTDYupa+pqyWiXECIyCZV+6JSTj86jj0dcYEUaD0UZrUtlDqDj8assCkXAGfapc3sUqcTDksFt0+Xr3rFv49wweSK6GaVmLBlwB0NYl4uTkck1pTvfUwqpWOJuE2TOhGMGnR2Ujxh0KnPbNWtRjH2hiPap9Jl2MMjK9DXfze7c8tQTnZnsXwNuriXRNStJHcRwyfu89BxyvPp1x7+9dpdxk96xPhRbwxaLqcsR/wBbOCy+4QDP41sa0iHT5lkk8tWG3fu27cnGc/jTvdXM3Hlk0eX+LddgtfFWlOkxMVszGbbnoeDx34Fdjpc0N35d1BIHikTcjDoRXn2reDY2uy8F9PJEb02+6SUFiMlc/mp/Ouh8IWqaXDcW0W87oUnR3bJw3UHtwQfzqHboCbJPEq7tWk/3V/kKwmi5zXRa2u/UHb2X/wBBFZLRjFZX1NktDziQ5kY8cmomAwwK/e9//rVI33jSMMgfWtTJlK1j/euy/Lt+U5Oa0Y/lXMhAxySKpLuhnk2xO4JB4PH8qZcSXEiYaJkj78H+dO1xHrXw0uvPsbq2P/LGQFeezf8A1wa9B215b8II5JJ9UkIOwCJfx+avWQlYVPiNIPQrMKgkHGKuOuBVZl61mWZ8q1UkTNaMoqpKMGgZnuhzioylW2XJphWi4FRkphXirRSmleOlAHlFvdJGFyEOBjaW6/yqwXuRFFK0GYw2cg5GPqOhrHyDz71twzrZWxi8h1m4IkEhwfYqeK7Gkjjja+os8ipOZlH7ueMq3uQOP1xVq7kKwQWxIUgb5V7p6ZPbAz+dZk10yoTEqoBjKheM85xTobmSa4/eKsjOS2AOCcfl/wDqpN6ajly6kufP2xQASLjLyDuf84qK4R4GZGLFwN/ytnP+HHNOaRY5NsRXYM4KDbUtncFpnbeIxtbOcDcMfd/Ssr6mJU8rzI/N3OJCAQwcBcf41OFhSMux8yQKMbmJ2ZHP19KjtwIrLZI6uu7kkD8PpV2a7hjsWZkjI24A4O386HJ7FMp20ZKpL5LMM/LIfb0/yavXWnoWnkWZdqoSgAOXPv6Gs2DURdHYPMBUHgtwB7DtS/aJGJjR2yBtJHQDOf6UNSuLUruVxHHnDe9IwYYVvkJ5POatTWsTOjGURsfvAncSeef8aiu9Na3Qt58boMZIPPPsavmQ2QzPcW0pjdmwPU/0qK5k8y3V8/j+NTxJJIpOVZ2z9/sMdaguF2wIOPwrSLGmdP4Odh4z0RWiTBu4znv972NfSzHAr508FqJvGOi+ZErjzl+YnnjOOO/SvoZreDGPIi/FBUlx2PBvjNdfafFcMC/8sLZcfUkn/CsXwcFTWunK25x+YrtPib4fMmtRXKwqqSIEV0H1JB/H+dcZ4Zj8vUw7HDn5CvQjilOXutHRRg+ZSPREuMqSRnikmAK7kxkj8qpljgqCBk9asCRccACvNZ7MRkbsDhjk47U2W8hsmAdlaR/U/pUgTcWK9ccVyep+GZNavWe4klh28KwOR+FXBBNu2hf1fWYFt2ZtigdSa59Liy1GJnt51aVRkgdRW1L4Xt/JSJpWljVADvySTjrWbFpEFhA0NooRieZG5Jq1ypGT52/I19Hu1mgAPDLwwrReTCnH4Vz1jCbED5txJyT61ptckgc4rKS10N02lqV7t857ZrHuMsp6Vdu3LSZz9RVN+lawRzVHc5vUYxvYnrjinaXbEws2D1xmr8kXmXOCoOOcntXVeDPCk/iLUVgwY7OI7p5AOAPQH+8f/r103bVkcfKk+Znpvwy0uWw8JPPNkNdsZFX0XGB+fX8RWhrNmbywnt0IDuvyE9Aw5H6gVvW0UdvaNDEoWOMbEUdAAMAVnzrgmtGrRscjlzSuzxLWr3U9O1Oysms4lkurtbmEeeDgs33W44+Yn8663SIbhYbia8hjgKRpbxqsgcbVPXIA6sT+lL4r0i0i1bRr02aPFHPJvjit/MZsoSMqoyQCM1zmt619q1TSdLstNurW1+2xsZJoDFvbd2GPcmovfQdrG/qsYN4/4fyFZUsWI2OOgJrd1NP9Ok9sfyrJu1220x9EY/pWV9TZbHlBHzUh6CnHrQR0+tbGQKoBOOpqxGmeKiUdBVuEZOcUXHY9h8B+HYdE0CN1cySXeLh2Ix1UYA+grqSuAag0ZNuiWC46W0Y/8dFW3FYz3HHYquOKruKtuKruKzLKUgqpItX3HNVZRQMpFeaaVqwV5phWgCuVphSrBWmleKAPEoA6SJJKj7BnPUgkDmpJJ1c7wTjAzn1qwLeSK3LzqMTghHVx1Xrkf561SeJosgH5SuSR2Pf9a7LpnBJNbkkcx3qwx97p0q5gxqjCVSxH97nkdPpWZ5jRbJAFIJOARVq4meKd1lhkjZo1KKc9DyO3oQc1MoslpkTuVfGFO4YPvTTJj92wzk56dKjd96K+3C5IHHpSIjMR8x2DknGapJDLCybrcRlVfBy2T1HpUF5cyOWjOShbOPTApMSRuWVznOAQMVNPbNsHmOD3+gNNJIaGRRlG3lEwV3BgSAR/LvUsLZgJXKkMM4780ltsHBOCq5OeRwc1qDSk0/wzLqJuVMk0wt4FDlXHIy+O4wGHNFr6FFcFjbKzRh0Z8EDsM9f/AK9Z1xNEzBED8ZyzNkn6cUNdXcPmSpueONyhk2A4PuRg/rTo9WSYEyW8bvg5bocfiD/OmoIfKNYFCNrK3uO1FzzBH06VZfULS8ZmkQq5GPM2HP8A47n+VQXfl7E8rOzIxzn+gpqNhJWOx+HvPjnS1XGBKT/46TX0STXzn8N45h8QNKD5A3OeR/0zavous4q1ylsZevaZHqmmSQsoLqN8Z9GFeQT6D5WpfaYJFA3BypznI9PrXt8rYjY+gJrlh4YW+soLi2lEczICwcEqePzFRUi3qjqoVYxXLI4nLHkjgCm+cwU5yOcVbvrOewupbS6UCReG2nI5HY1VlUbR7GuKSsz1ISTV0WoLgQx5Ygmo5dSTfgAY4zXJ3Wq3E07xr+7hVupHWh9XSN8xfvXx12mtIwaRLqXdkdLLOSJGzyQAK5q6umhnbggdye1K+o6o64S0fJ7lMfzrNvP7UkP7xYx7Zqox7hO6V0WxqafeZsAetWUvY7lN0bcjqKwo9EvLoYlKqvfAya1bLSl0+Fz5jMT60TUEvMzjKo3rsSu2TUUnGacOTmoZmw2KIoJMwNQurlNQeKGRlAQHC4zmvpD4beHpPD3g+1huCTdTjz5tw5DNzg/QYH4V4v8ADbQIPFPj9muG/wBHtB9oZB/GVICj6Zwa+lgMCu2MbI8ucm2yFOYpf941QnGc8Vfj/wBU/wBTVOYUpbEozZIlfaxUFl5U46HGKrTafb3pUTxh/KkWVMk/Kw6GtBlpIk/ePWPU06HH6mgGoTezVkakuNOuj6Qv/I1u6mub+c/7ZrG1MY0q8PpA/wD6Cay6miPID1ob+H60d6VuoroMiRRzV2BeOlVI+wrSt0yufakxnv2mqF0q0X0gQf8AjoqdhxRaJtsYF9I1H6U9hWUykVXFV5AKtsKryisyim4qrKOatyDNV5BQMrY5NNIqXHNNIpAQFe1NK1Njmmlc0AeJ28/2m3vDIGc5BUk/c45wPUgfpTLpRBG25cyFeRnp/nNd9qvhWDTvDWnXunTrLb3kSzOr4yMAdPcbiDj06da457R2kussRH5TGTPTaOc9exxj3xXXFp6nK431ZnRwPcfZ0SKRlzgKo3M3rgfnTboX3mKJRLJsGxC+W2qOAP8ACuk8M6ZNfXSxWdjLc5UBXZ9ojyR82B1xyaPEWjWmnatMlnIt3EpxJIhwiyN2DLxxnpmtOZCcTBt9MabS7i7lRhDC4AbI5bBJX1Jxz6Dv1FQxBQ2I5wBnHI5rXkmTTdN82C4uReCVtvP7oJx2xkk4FY7KkhaV/KYOC5fc3J9OnWjRolxJCJIpt/lGRc7iwXPPeo7m+i2xpE0y5QCZSwwzLnBwMYGD3z35pjLGEWRWkiVmIB65IAyOMY6j86tPJHJADDsLKgHJzubnOd3SnyoLDba7jcvKLdi6IuQuMMcrntjJwfX8ar/b7i4CWRbEf2jztmejYx/KtDY0YmMcaNECoDbNmTgd1/EVmWsloL0Sgy78nII3ZJHr+NO1tikjd8Oy3a3F1FDbedEzBpc9ADnk+g46/oag1HQ45JJvsKCJ2OBCf4jyCRjjHXpxxTtMItp5JHvRD5q7fK2MNwPGecYx1/SrY+2Wc6Wf20X1mr+ZC3BwPY9VPqvtx61Otx6HFSW8kMhjkRlfpgjmtF1220K5yQBzXYXNvaapLFHIIzI3zLMg+Y4B7H8PX+VctqESQyLFC25FICkDqKpO4rHR/Cd9vxB04NKqpiUkbsD/AFbYyOlfSQdCOHU/jXzfBpbadbTQSxRmWKQu7McofkUj5lPTnHB/nT9Nt7+5tLi0SULZkBpMkEO2AMAg9RwfwFJxe/Qaabstz6HvGC2kzAj5Y2P6VHpDAadb/wDXMfyrxESJGI7R2V82qR+a+MLwMsM9+3/6qo2Fn++uBH5vLlQEc7QuM8k/Xriob0uVY9b8cWO6KK/RfmQ+W59u365/OuIaTjBrmb2G7t23WzyeYnLKrEjj1rXsLxNRthIBtccOmeh9K5q0PtHfhan2GW7e1hkm3Moq+1nahQ7L83qKrRkBflpXlLRnrxXNdo7lYgurpEQgSuEFUA8WQwGSe5qWeMygjbmoYrdgAOgq76FNslUhcsO9V7o5jNTzDy0A71mX1xsQjNJK5nJ2RDuAHvVOaTL49ab9o3LnNMjy77jW8VY5ZSud78FtPuIPEF/cCFvJMOGmVcLyfu5ODnI6Y9TnkV7fXM+ADG/gvTpFJZihDknJJUlf6AfQCumrsWx5r3IY/wDUn6mqsozVuP8A1X4mq8oqZbAimy0ka/M9SMOKSIfMax6mhyGojdezn/pof51i6ydujXx/6YP/AOgmt68GbmY/7bfzrA8Q/LoF+en7lh/Ssupp0PIBQ3LLS0p+8K6DInhHNa9tHlazIF5FblmnQY61Eike9QrtgjHoooccVIowij2priokCKrDrUEg+U1aZeTUEg4rMspOuKrSCrrjiqzikMq4ppWpttIRQBAVppWpitN20AeYaxb6l4Wvm0i6ZpLFnDxDJ2DJ++vpnHI/wqp/Y0+ta3Z2UQAeYnnGQuOSTXoni2w/tjSVeSRA1uhKL1LZxxz9K4xNVudAv7TVLcZaNtrgjgqeDXVqle2pjo1YhWy1HSNOEYmntDNEGyj4YxNyM4Poelbmp6GkvhG5vY3toZLV1Db0AMwxgjd6kHj1IqHxDqseqXjNagOuxYoSoOH4wOw75q7Y+HryK2L3yxXlwybVWR8rFnuB0z7/AP66Wrd0XFrl5XucvpXg/Ub7w3dagRFHawh8B87nYdsds5xzWRY6C8k6Q3HlRA7slHDEkZwCucjp1969SibUINJk0z7NCtvISWYNyM47fhXNpolws8km5gMjBaPb2P4n8aab6kyjG+5wd3atGMqwMaFVUtjuN2B+dUtjGYj5doBAPGeprs28KTOSZblEyxbOwdMYApJLK2tYWXEErBfvrHnp3yRW0XcyasczbQNPbSskZMqkBdnXpk/WnWMF5KHmk+RRlRuTc27029ce9bqPAjxLEsaMSMtECM5z1OOuO+O9RXkxZpEO5RxtAPGMHr+fX/6wp9wS2RUedjGN8gcgbQFyox6Efl1qNpWMLQvCAvbAHB+oq3NHbxpickfJlSGyS44Iz3piWE98gm8kW9vghSByxB5+vJ/zipurXK9nK9kjOu7kxW6SwzOJYmDjnoR0I/8A11TEpd7U8MS68E4B5rd1Tw/ss9tsS8zMPvHGQQDnHbGTWHd2FxYxQtOFTBAHzdTVpaXIejsegat4n87RLvTG08wSum84dWBGcZBHOOp/+tWNoVpPPZhop5FkdsIiBfmPTuPb8s1zkrGaRpmvg7lAuMtzz0OV5GK67womh29vv1LxBHEXGTE1vK5j9QuABz35PQVMtQWhn6lFLFp7SS3Ylk+1tEyhRj5cjOcZ7dKk0m51SfZa2koGcYQhASSQo696NbTSjo8bWGpPLcm6kL25GFCbmKt06kFf4u/Su48JXfhXQ9cmL65DNbfY41SZ4RGd+45HyqDxxz1PqalpWKucp4p8PXWiWdldXF08kly5UgxhcEeh3HPX0Fb9n4b1GDSBqhvYTBGHaSHytp64OCDyeB16VF8TNV0/Vp7A6dq1pcwCRv3ccW0xcKMs2fmzz2GK1INcsofBM1lLrNhcTNHKCA+xsljtwCTkY9880mrqw4yaldGS0pSQE5wcH86nS4QZJPSrC2sV7pduyOpPlLtkQg9v1rnr+C+tSy+XvX+8lcLSbsespOKua8lxGxO0Ae9VGv0ReSDzgcVzEmqyoSsm4exGCKgOpDknBP16VSpEuvc3bzUlD56rjrWNPcmZiScL/OqL3LTE9wewqWOJ5CN3T0rRQUTNzciRMyHAGF/nV1EwBSQQY7VbEdKUioxPYvhRciXwtLDk7oblhj0BAP8AMmur1y6kstBv7qE4lht3dDjOCFOK8u+FWo/Z9fu7FmwlxBvA/wBpD/gx/KvSfEk0q6HcxwafPfGeNotkG3Iyp5OSOK6oO8Tz6sbTZznw48S32ueHb251GXzZYboxhtoHy7VPYepNdWJlkGRXmHwlumTw7q8Jt5QFut5lONmdqjb1znv0r0K1YOoYdCKJbELcstRCMsx96axot2B3H3rHqaHLTjMsn+8a53xVhfDV8f8AYA/UV0so+dvqa5vxkuPC96fZf/QhWK3NOh5CMA0rffUUg604j94tdJmXbYdK6LT03SRj1YCsG0Xmun0qPN1bj1kUfrWcho9uAxTXFSgZFMdamQIrMOtV5BxVplqBxWZRTccVVkFXJBiq0g5pDK+KTFSEYpuKBkZFIRUmKbjmgDzWTUJmJQXWU4+VjuPH1+lRzqstpGrMu0kA9OBnmrEPhwXcDlb24DLklRK2Op461WuUFpp+zg+X8oLjkkV2zilsc9OTkV7PWkt9ZWRbdHRCFQnouepA9a719VsoDtmuYkc9nnRf/r15l/wjoW0e5LsHGT97gGspI44GIaWIkdi6kt+Zx/L2rRRXQV77nrdxr2lQM4k1CAMOSiuGIrHvfFmnxbhbuJ2xnIcY/PjP4V540sMJ3vCYGIx5igqfz3Hjn0NQyyZKYHmls/O2D34+v40NCOpvdUnvAXlmKsqGRIUUBc9uOcn6nqRWbNO4nmQLgEEBtpyPlBU/oeuenpVHTmk3GR2JVPlxjICk8g+3Q+3UdKnnvHR4vLXMiZRdpzgrg8dx/Lk0xDonUtCsihPlVeCcHsrDHOf8KiuLzzAEA81gOoOMd+vpkk1D+/ufPZA0a7XdsnquN2P616d4a0OxstMV/symYgh3Kc7g069+nGB+FSxpnG6L4evrqeO4ksmnG48Op2EYGOfxrbms/s1vBC4TzAzkRJyAC2ev4112s6jBp9tNbAn7S8TIiAcgliM/p+lchGFs7fz55DtQAbm5PsBRGK3Zo6zStDQllVLS3kurkjcTzkck9gK8+1zUzqtxsjRfLVTjoMnHqe1WfEOuS6gzRKSEyRgHgD0+vrWBISiEg4OOtW2YpD5I3jR2BUYC4w4zX0J4Yi+HcvhXSft39gG8+yReeZTGH37Ru3Z5znOc188SNlH57j+lfQOheBPDM2gaLNceHzO1zYxSSSxyy5LlASThsDk1EnYpI4TxhDoEPgnTZNNayXUHvpxKYZAX8ve+3IBzjG3H4V3Gn6B4JuPFupRPFprWSW0HkjzwE3nduwd3J6Zrz/xd4d0nTvAGh6pZ27R3l3NKJXMjHcoLYGCcDAA6V13hr4ceHNR1nUbW6hnMdva2brsmYHe8ZZz+dIDM+JuieH9LudBXRYreJbi4dbjyZS4ZcpjPJx1NXtU8L+HLXwzJOmnkXJilZJlmc4YE4GN307VjfE/wTo3hiXRv7NFwq3crpKJJN3AKdMj/AGjXK2Gk2F14Xlv2nuBeRO+5VYBNuF2dR1yTnnsKHsC3PU7e0h0+GK2t0KRJGuFLZ6jJ5+uaLi3SRM4qppul2+jWq2VtNJLGPm3SEE5POOAKvkkrivMq6TZ7VHWmjnrzRbO5JE0a898VjzeE7VCWXgdQM11c0ZbPrWbOJthXOR70Kcu4/Zx7HNf2XFGcKOlOW2RDgCtj7MduTgfSqjKFnAA71fO2TyJBBbfJkj8KWROMY4q8keEH0qGVPQVN9SrFG2eSLUbfyr6WwLuENzETujBOCeCP516XJ4C8TW8LyL4+vtqqWO5G6AZ/56V5dep8hGK0v+E98VJposI9TTyBEYsTRKzFcYxuxnPbJ/OuqjNJanDiabbuh3gjTPEGq2eorpuutp8MUq+YgBxIxHXj2ArXmtPGti2yPxKHx2AP/wASawPAfiqDQby+s9QvI7e1uF34aMk+YCAOQOBgt7V6hpFzaanH51tPFPET9+Ngw+hxXQ7Wucavc4j7Z8Qk+7qJkH/XJf6pXf8Age71S60y4Or7vtKSYyyhcjGe1bUdlHt+4MVLDEsHmYGARn9Kw5tTSxgyD5j9a5jxrlfC137lB/48K6lhya5Tx623wxN/tSIP1z/SsVuaHkg604j98v0pq8mn4/fL9K6TM1LJeRXVaMmb+1H/AE1T+YrmrIcjjiur0Rc6lZj/AKbJ/wChCspblLY9lA4prjipB90U1hSkJEBXg1VkHJq43Q1VccmoZSKco4qq4q5KKrOKkZBim4qQjmkxQMjxSYp5FIelAHn9vfrCqsLuIAxyNhJhzk5GeeDXO6jqVqIRC85MvmckhsZxnGcH6/j9KiS2mGmG/ikkMChjgOVG1SRuwMcHGT3qqIpr6bTYXuYZTLAxWRznEg6rn6Y616DVzli7aFn+0D9jup2vI2EQPlRhm+fjnIzgHPA4yMZrBmmaaBZZFeRMKAJOSBjqG65yM1o6fpct7Lc2RlhgljctsmOCc5zjj1qCS2urfztNkkby4XyVBygJ7j60alJQvqzPjAkhkBYeUvI5+7k+/SowQqYCkFeQpHH4EHPNPmESxBpGDYYo64OdvUHjHp60tpZ3F7DOYOEhTzHDNjgA+vfjp1qiSS2dpXeKJSzzbR2J68jpk8kflXSafoNtIUubwHzT5DhVJ+YO5DFsk8/5wKz/AA1pssN7p2oGa3WGeR4sb/mU7X+8McdP5V1dqI0aFftMAG23B+bP/LbFS7iObDIlxOsYUqbVRg9ibc5/9Brur3X3spp7KGIGVWKmQngHdJnj6MO9cdBBNJK7sqkPFEn3ccCMqQRj/aNaDGKxtzPMpAB9Rlj6ChR6sbl0Jri5Fusl/eOzszbmfuzHsPeuQ1zxBPeMyqSi/wACA5CD/H3puqarcapew2keN8riONB0jyf51jSL+8YZyAcZ9aq4hqjCKPYUyfiNv896kPAH0qK4/wBW34fzoARj+7f6/wCFe1+GvHXihdDsrO3TSTFBaxIhkSQNt24GcHrgV4ixOxv96vRdLQHTbRs4PkJ3/wBkVE02tC6coqV5K6IvGl7qsfhXSNJvlszBayP5Tw797E8ndnjv2rutG17xDpeo31//AGdp832xIUKCd12CNNox8vevM/FTw3KW0MdyjNGWLgHdjOKqal4ovNQzHE5hgzgBDyfqahwlbc2VSlzNuGnqdp8T9cv9cXRHuLaxhMTSuEgu/MYcr98EDb0465rA8N6fd6no02nW8BEDybnvGYqnUcAYyx4rmbNVkljjf7sjqHPsTzXultBFBbJHCiqiqAoUcAVnVqOmkupdCiqjb6FIRGBUj3lyqgFj1J9alDUsyndmoxXBN3dz04RUVZDZBznsaqvBvJ4xVtjxVWSVozU3KKs8AjXmsmZD5wPTmtS5uWl4wapNGSwJz1q0yWiyoHljFQsman+6gFPWIsuaVxmLdxk5GKprbBuCOtbc0IJI71GtpgYxWilZGbjqYlzp0MsREqK2BwSORXIW17c2kokt7iWFwdwaNipB9ciu81mVbPSLmVuG27V+p4rzjPWuvDttO5wYtJSVj0jQvjHr+mFI79YtRgHB8wbJMezD+oNekaL8VvDesoUmnbTrkrgpc8KT7OOPzxXzdmgOQa1dNM5eY+pYrmG6jEtvLHNGeQ8bBgfxFcp8RDjwz9Z1H6NXh9nqV3p8wms7mW3lHO+Jyp/St678datqemix1J47iIOHD7Ar5AI6jg9fSsvYtPQ050RL1p//AC3X6VVgu4JCAHAPo3FWxzcr9KtiRt2K8ius0NM6pZf9d4//AEIVy1gOldfoCZ1Wy/67J/MVi9y+h68BgCmsM1Io+UUjDFEiUV3HFVHHWrr9KqOKhlIqSiqzirbiqsnFSUQkU2nkU0igBh60mKcRSgUAeHadeNHNJpN0V8slo1OPXPH0I6VnSWT6fKsMwPko/wAk6emeD9RWtrAsbvTnuopYxOi5jaJTzg5wapyamBaI1yqyx3EWMqm1gxHIx3GeM/j7V6BElBaMnvWa2urfUbqOK5hjcJOIW2l0J+nyn0IqDUfENkmsG60q3niiaHy5IpZfMOecEHH09e9YEl3JNMIlQJhAjhejYHUj8KtxvbrHDM8Cl/NjLDGFKAYI5B69ev4UzJtN6aFc2ss1vPcnOYgpZCMjB7k+vHT6+ldPIkcAvI4VCRtYlsKOvLf/ABVZc+pW0jaiI9irPCqoPMB5AI49adJqRnJjgiVmktTCCOcnn3/zig0jyR3LlmphgMZbAjvww+jLx/OtCK385VDE/cCsDgg4OaqWwdmaTaUMhDMo6AgAcflWkZobWNpZTtjTGTjk56U0u5E6iekSVjDZW7TzHai+g6n0Arj9Z1uW5uACe+1FHRR6/WpdX1V7xyvKxLny09BnqfesCTJkDE8k5NDdzMvaGN3iGwPfz1Of1qs7ZYn1q9oAH9u2fs5P5KTVAKWHA/GkAGo5UaRCFH41KzIn+0f0phnZjj9BTAfJHDhSC5JGWHQbvb2/wqxJq17LAlu0pWFVCBF44Axz69KpFyDzzSN7dKBkwO5Tz2qJMrwexpEfBwe9OOD0PNAE0blR7ivZ/C+prqejwyFsuBtb6ivEwx6muq8H6+NLvfJlbEUhA+hrmxFPmjddDrwlTklZ9T1maIEdKrtAcZxVqC6huIwUbNTAxngEV5rPVRmGJlHIqrNFuyQK25UG3pWfJgPjFRcZleUTxgUhtiecVrJbFjwnvTntvUYquYLGBIuGC1pQ2+IelR/Zwbo9wDWjvVVwKbY+UxLiIpLjFORFK5PX0q1dIHbKgk1xfinxEbEPYWj/AOkniRx/AD2+v8vrV04ubsjOrJU1zMx/GGqpdXYsrd90UJ+cju//ANb/ABrlmPFOJ71GTzXqQgoxsjxak3OXMwycUZpM0CrMxQacT3pgPFKDzQAuec1Yt72e3I8t+B2bkVWIxzQDSYzrtJ8TW6kJeKYmH8ajK/l1H616T4WuILvU7GS3mSVPOTlGz3rwkE1paPrF7omoRXthO0M8bBgRyDj1HQj61lKknqilNrRn2KvSkauF8CfErTvFUMdpdMlpq2MGEn5ZfdCf/Qev1613LGspKxSIpOFqo4q0/Iqs4rNloqyVWfrmrUnWqz1IyHFNNSEU0igYzFLS0YoA+aje+Tpj/ukLAhAzNz68Cqm6JNrP87I20ID1Gep4/D/PKoyxs2Y1lcDCs+cA+uO/4/lUDgluTzxmvSOQ04NTt4EdHsBI0hJV/MxtB7Yx2pqapCLCG2NoS8TBmk8372DnGMcVnEZdPYAU+1tJrskRJnA5PYc0Aa6anHez3KRaYpa4iCLmQHysBhuzj3Hp0rYhtEaQNHAkWFC4Vun+TUFjYw2yBUhJOOSxyT+NaiyJCpf5URRlix4FNIRKkMdrCZJSyxgZLHoK5TWNW+1zhUykAPyIep9z70uq61LqHycpAh4UH73ua59pW+0CQ846KaTdwSNC6uVnERWMRgIBjOc+/wCNVsFmGOpPanO2/aQOMdqfkxRZA+8D+VAySFzbSLMjlZEOVI7VA8pboeOlIz7hUZPagYhJoA3MACB7k4AptA64oAerbh70oJ71FyDShqAHkc0ZwfekBzRQBJnPWgHBx+VR5pd3FIZ2nhfxglgUtdSDNb9FnHLJ9R3FeoWvlXMEc9vKssUgyrqcgivnvOK0tJ1/UdFmEljdPGO6dUb6qeK5K2GUtYnbRxbjpM93kSSMZU/nVKcmTDAYZfTvXGWHxS3KE1Kw+slu3/sp/wAa2oPHHhucZa8eE+kkTf0BFcUqFRdDvjXpy2Z0lpMGUbuoq0+0ocAZNcyvi3w2nP8AasePaN/8KhuPiD4dgQ7LiWc+kcRH/oWKn2c30K9rTW7N77H1bpmmmCKFGmmdVjQZZnOAB6muEv8A4pnlbDTlHo87k/oP8a4rVvEGpa1Juvbp3XtGDhB9FHFbwws5b6GNTG04/Dqdn4m8eQiKSz0XJLDDXRGMf7o/r/8ArrzpnLMWY5J5JNNJ/GkzXfTpRpqyPMq1pVXeQMeKjJwRTicmmtWhkHag9KO1IOpFAhVzinUxeMipKADqMU0dDTqaxwtACgYpwpo5paQE0UrRyKyMVZTkEHBB9RX0f8JvE1/4i8MypqDebJZSCFZ2OWkXGRu9SPXvXzWOtezfAjU1S71bTGYAyolwgJ67SQ3/AKEtZ1FeJUXqe1MOM4qs9WWPFV36Vys2KslVXGKtPVZ6ljIiKbUlMPWkAhpKWjFAHzDszcSLj+EGoxGTNIPQCrm3/iZMvrFn9amhsn+1vI6HyyBjnGa9M5Spa2n2i4ZWbai4ycdeOgrctrWOCMJEGCg8AmnxrkbVVQAPpUvyIhZ2VFHUk4xTAcQI1Lu21R1Y9qwNT1NrxxHHkQr0H94+ppupX5u5PKj/ANUp4x/EfU1XWLYm5hyFz/Ok2BA5zUKozzoFGWOcD14qRjmp9MeSHVLeSJtsikkHGcfKaTAaItuyM5Bx3/OiYg/KOwq3qMm+/eQqAWwMDp0HSs5mxKT2NNANzxTetDna3tSDrQMM4OKKGpoNAC0UtJQAZ5pwNNpaQC5pKSg0AKDRmkooGLnFG6m0maAH76Td7U3PvRmgB240hNNozQAuTS9qbmjNAB3pHpRSP1oAQGgEbqSk75oEKeGp+eKYe1KDQA7NJIfl/GkzzSOfkoAeORS0xT8tOJ4zQA4Guz+GWoHT/iBpL7sLLIYG5xkOCoH5kH8K4tau6dePYahbXkeQ9vKsq/VSCP5VMldDR9iueKrv0qSOVLiCOZDlJFDKfUEZFMfpXEzcqPVdxzVl6gcZNIZHimkCn4prCkBH3oxTjSGgD51eyf7YtwrAfJsKke9WQjscs2fpThGAeXzRLLHbRl34Ue+K9NHKPBSBGkkkAUDnIH9a5+/1N7w+WuVi/u56896h1DUHvH4ysY6KTU1nZBYkunOSzrsHoNw5pNgNt4dpDt1+U49Pmx/SpJflhfPoyH6g8fpVhI8sEP8AF5kRPoc5H6ZqO6P+jO+OHwGx/Cw6/p/KkBl1a00M2qQBM7vmxjr0NVamtZhBc+YVDbUfGRnBIwD+tAyW/m83UZD2U4/Ksxnyx5qZT8rMT1FVD1pgTucqpoBpin939DTqAHGm5waXPrSGgBaKSigAFLSUZpAFFJRmgBTRSZ5ooGBxmkoooAKKSjvQAvSkozSUALxQelFJmgQopH60oNNfrQAdqQ9KOlHagAJ+WgHgUdqaDgZoAcDzQ5+Q00UrfdNADl+6KUfMfYVHuwoHepV4GKAFFSoagqVDSA+svBlwbrwPoczHLGxiDH1IUA/yrXfpXIfCe9F78OtPGfmt2khb8HJH6EV2DiuOSszdbFVhUDjmrL9agYVAyPFNYcU+mmgZHigin45pCKQHz6zLHG0j7QoGSTXNahfG8kOABGp+UAYpL+9N3LhcrGOgqrg4Nek2cpBKx+6K6exHn6PHt5YJgfUdP6VzLr1rotEcx20UZ5WUFlP+0Dgj8gD+dICVx5hDq22ObayP/dcdM/lj8KjvSGtJXA2twJEI7jGDVt0WJnSRR9nkPGf4Seo+hqtqUbJZsW+cDAV884z0PrQBiGkdgsXu3FKTUc5+cL6DFAwz+6Y1UByxq23+pxmqWcORTETR55HqKetRxn5utPHC0DHUGm54606gAAopaSgBKQnilNNNIBetGaSjNAC96KKQ0DDNFFJ3oADRSGigQuaDQKQ0ALSUUhoAUHFI5o6UjngUAGeKKaKKAFBpO1FN6GgBymnOflNMWlY/KaAETk5qUMCeKgU4GKmUYFACk05T6VHnmng8UAevfB/xT/ZVlf2U3zW/nrJj+JSy4yPX7vSva4buC8gWe3lWSNhwymvmPwHLINQvYo3VS8Qb5s44OOxH96vStN1TUdLm82J49pHzqBww9xXJUXvG8FeJ6e55qFjWdpevWuqxZjykwGWjPUe49RVhbpJF3IwYZxkVkMnpCKjEma4rxH49msL8ado+nSXt3gk/IzdOuFXkj34ppNhc7jFJXkNz8Q/GMF3DaS6THBcTkCKJ7V1ZyTgYBbnnirq638TJR8mj7B7wov8A6E1P2bC6PHUjPBYcdRTmIxV/VQF1GUAAAYAA7DAqg3Wu45iFxxW/YwmPT4XbJgkG5wOqMDww/SsNhhTXSaeZbexg3ZkgdA2ccpnt9KALKuyoBPtZT0cfdYe9VdVjEenHbkKWHHarqIoXdC42H+HtVHWFC2A+VRmQcg+x9qAMJT82T0HNVmJJJqweIz7nFVc4OKYxxPyY9qqP9/NWj0qpJw1AEqN8wPvUpPvVYGp2OTmgBy/e5p9RqMGn5xQA4UhozxSE+lACGmE80ppueaQC5pRTc8Uq+lAHSeH/AAlda5aSXrXMNnYpJ5RmlBYs+ASqqOTgEE9ByOabr/hO60OCO7W4ivLGR/KE8SlSr4ztZTyCRkjqDg88Guv8FXENx4BuYcN59nf5z2KyoMfjmI/nTtZjafwxq8WcgQpMB7rIvP8A3yW/OsXUany9D0I4SMsM6qeqPLsU012vgfRLO7S+1W/gS6is2jjjtnzsd33HLYPIAQ8dyR6YPQXvhqy8VH7Pa2Npp98hUpLaxCNChdVbeo44DFsjB+UjnNU6kVLlOeOGqSpuqtkeXQW893cLBbQyzzN92OJC7H6Ac1bvtD1bS0V9Q0u9tEb7rXFu8YP0JFep5tdMtzYaVL9i0y2TM9zjDSgHBkkI5YkkYHuABSaL4g0nU5pbGxurm4lKnNpexAR3aj7ygBjzjnB59Oan219lobPBqKSnJKT6Hj+KCpr0b/hXUDavLeSzS2/h8qJYGHzSSk5/dKfVSCCx9AcHIrobOGxiItdK8NaZgDpJai5cgdSzSAn+QpyqxiRSwVWpdpWS7ni5pK9i1jw3peoQp/aOiR6W8nCXthF5Y/FB8jj1AwfevMdd0K70G/8As1zsdWG+GaM5SZM43Kf6dR0NVCopbGdXDzpfFt3WxldqRvu0Gj+E1ZgJ2pM0A0UAGaaTzSmkbtQA4Uj/AHDSLyKJPuGgAjGeal5qNOFqSgBKeOlR96eOKANXQNXl0bUmuIkVy0RQq3QjIP8ASupHj6Y8PZQ5HXD1x+jWkd9r1hZzSGOO5mWFnAyQGO3P619CaL4dstC0aHTV23CxFj5kqDJyxP8AWsaije7NISa0R5fD45ZZQwtgjDkMsh6/gK7bwT4rOtfardoWRosPuLZzuJ/rXQy6Zpz/AH7C1b6wr/hTLezs7RmNrbQQ7uvlxhc/XFYvl6Iu76k2u6g1j4ev7pGKvHCxUjsTwP51g/DSQXOi3l++WuJrpleQ9SqgbR9B/WneN7jy/BuonuVRfzdar/DE7PBsR/56Tyt/49j+lO1oivqdP4g0Gy8R6a1rdALIvzQzAfNE3qPb1Heud8M+KbvS9S/4RjxKxW9RglrcnJWZT0yx6k9j36Hnr14krD8V+GrbxPpvlNtjvIgTBPj7p9D/ALJ/+vUrsxs8M1g51KT6D+QrONa/iOSOfXJ5YY0jidY2RUPABRSKySK7E7q5g1Z2GMPlP0rqtMiuGtbaONPNzErYHBUYz+VcrJ9xvpXqum+Dftei2v8AZuqRytJFG00Ewx2BIyOcA9sY4piOcZER8EbW78EfrWdrQxZLgscyDuT2PrXcXOlzwWr2v2PTGJ4M6s+72xkcY9q4HVpF80QMCHiY7vmJHcelMDGm4IUdqrtyKkc5zUVAxM8c1XmHNTk1XlOTxQAgPFTryBUC9KniPy0AS9BSZprNzSA+lICQGjPP+FIOBTS3NAAx5ptOboKbQAU4GmU4UAdp8O7otqt1pO4AX8GIx6yp86j6kb1H+9XZWtv9tNxY/wAV3bTQJ/vshCj/AL6215Ba3U1ldQ3VtIY54XWSNx1Vgcg/nXqttq+naykd9Y3ttbXDnfJazTrC8MnfaWIDLnkEHOOvNc9aLupI9PA1o8kqM3a5z/w/uW26xpjDHmwpcqD/AHomwR/3zIx/4DXZ+HCf7WmUNhntpIl5xlnARR+LMKbaaJFb6jJr8OkzrctDIkhtXD28hdCrOQAcHBJwCBnsKxru2ubyzvbayBNzJD+6w4TBV1fO4kAcKe9ZzalNNHTh4yp4aafT5i3jPHpmqxyhhb/ZpFn3DABwdmfQ79uPfFcH4bsNV1DWbVtHgmkuIpUdZI1O2Ig53M3RQMdTXpcTXlrpNrHrL/2jqciiaQ3mJlgz90Kpypbbgljk88GtK1fWdRsC0z38liCVWG2jZt5H8Kqo2j6nAH6U4T5PdWpFejLEJV52ivxNK8SK/wDt0IuI4LWFJbq2ZzhCvmnPPYY7+1cFr+uWWmaPdW9veQXV9eRmArA+9YoyQWYsOCTjAAJ6kmuvln1BdNsr28sFtGjaW1NsXWQGLqAxB5yrMDnBODXHy+B/D97K0sOrXmnKTkwSWwuAo9FcMpP4j8TSioKXvbjquvOj+6V4v7/6Zzfg7WLyw8Q2VtE7SW13OkE9sxJSRXYKeP7wzkHqCBXY+IbMX2hajp2RJ9lDXUDkcqU+9j2KBsjuQvpTtL0vRPDspn0+GS8vR9y7vFA8o+qRjgH3JJHbFU9bvTp2hXd0Tg3EbW0WeN5YbWx64UnPoSPWqlNSmuUzp4eVOhN1tL7HmB60dqGNIDxXUeSN6Nil6UjDDZ9aTNACmmtyKd2pp6UACnilflcU1TSscAH3oAeOBgUtJmjNAC5pwpg60+gC3pcvkazYTZx5dxG+fowNfSzuecc18ukkFSOoNfRtrc/abOC4U/62NX49xmsavQuBfeQgcGqvnYbOaguZnS2mfccpGzDn0FUbK8a5t1dhhj1FZGpR8fXH/FIXKjqzxj/x4H+lW/h+fK8F6cOmRI35yMaxPHsp/wCEa2/3p0H6E/0rb8I/u/CmmLn/AJd1b8xn+tV9knqdYsmRTw9UUkqZX5rOwz571VrNtTuGsCTaF/3WQR8vpz27VSNST481sJ5fQbD245qLvXYlZWML3GS8Rt9K9Ct7YQWsQ+2NGAi4VZAe1eeygiJs+ldaIHMbKLgq38IEgx/OnYCe91G5iEkFqb2V/wCBs/Jn36VzF7JeKnl3jkuTnBq5cwG4IaS/TCgjajM2fcnBGayLjAbapyF4B9aYELHNRGnMaYTQAGq8nWpiagegAWpFbbUS04jIIoAmFPAqNW4BPenB6QDmamjk1GWJNSRDmgB8nBA9qjzUkhyc1EaAFpaQdKMnNADs0obim5oFAGpo2t32hahFe2FzJDJGwYhGIDgHJVh3B6EGvWnsZzquqCyTIg3qFXqQW24HqcZ/AGvET91vpXqHxEuWttGeJDg3eobyw44jTP8AOUH8KwqQ5mkd2ErulGT9PzLNzfaTYNb2Gp3r2d6ULMXjLoiknar4yytxnoeCOlRS+IdD0qMltYN6GOTb2KuN31ZwAPrhvpXlZYk5JJPfNJmmqERyzCs7pM9Ft/iZDHJPFN4etXsZBhY0mdZF9G3nOT17D8uDIvizw7JlvO1CD/Ya2V8f8CDjP5CvN80E1TpRZlDGVobSPQJvGujW6lre1u72T+ET4hTPuFLEj6EVxmrazeazcie7kB2jbGiqFRFznCgcAVQyaQ0404x2M6uIqVfjYZpRTaWrMRJOBn0poNOblTUQPSgCXqKZTgaaetACL1NEn3R7mkHWhz90e9AEvag0nWgntQAo5p+c9aYKdnigBGJ4+te+eE5/tPhTS5O4t1Q/8BG3+leBtwv417N8Nr5Z/CSQsfmt5njx7E7v/Zqyq7F09zo9RXdp12P+mTfyrO03BtUI9K1bwK1nccjBjYH8qw9DkzaJnniuds2K3iu1t7zTYYLmTy0MwIbOADg4yew5/wD1dRmaRe3+mRLY3Fw5iQBYmxt2KBgKf6HkHsTV/wAXupis7cuFaUuVz3I28frXIw38tjMIrhGktwCNm7BXPdTzj1x0PcGtoWcbMiV07o72LUboqcXRPpwKDq97Ec/a3+mxP8K5iG83RrJbyl4zwW242n0Yfwk/iD2PYWBMz8ZOTxyO9ZuLRSdzhb1xLeyuJDIGbIcjBNVT1qzftuv7g4AzITgdOtVSea6znGSn92QK7LVNQt0ggibzozKSFby87lzjABxjJB5Nca/T8RWnPqNlLdNNsuC6nAO7gfTNAEkkUdpEfIaWNAfusvU/XNZEuSc1duLwXEWFaQpnJD9RVBmPNMRG1Rk09jUR60hgTULnmpDUbc0ACU/rTUp9ADc/w9xSknFNfI5FPhUSc0AIOtWYxgUgiBIqQjatAEbdTUZpzd6Z3oAXPFKOaSlFAB0oopKAFzWxq3iXUdbsbG1vnjdLJSsbKgDNwoyx7nCKM+1Y1GaAuBNJSGigBxNGaaKWgAzSGjvSHNAC9qWm0tAAelQ/xEVN2qF/v0ASA0jUg6Up6UAMHWnH7y0z+KnMcEGgB4pM80vVc96QZzQA8UZpKTvQArfcNdX4O1K8tEuobW5SLcVch0DZ6jj9K5Q9CPatPQSDelDzuQ4wce9TNXRUdz0Ua9qAQq1yhyMHCr/hUFtqc9ogSKRQB2IBrCaE44Q/nUTJIBwhP/AiK5+U3NjVrg6yIftcinySSm3jrjP8hVKWON0Cs2/36VSKSd1IP+8aTDYwc/rVLQRYjU2kwlgY7sEEFhhh3BGOR7VI95PuzH+7XGNitkfhnkfnVAlh61Gztnoae4FCVt8rN6nOKiIqSY5mfHTccUytznGPxt7fMOcVpNcWJkMhlZnxgl4Fx9cVmyAHYGOAWAJx0qwbWJwBBdLIhIyWUqQOmcUxE+mW226EkYguuwhkAHmKc5xngEYo1e1t0ljNtDLAHXJSTPBz71pxQhdOTT7yEBFB8i8hwR1zn/P/ANes25uZ9RKNLlniHlnaOOO/vnr+NN7CW5kNG4GSMj2qI1rfZLjH+qfHrtqlcMCohCDeHPOOcnA/pUlFM9aYRXoepfDcqhfTrzJ/55zj/wBmH+FcbqWiajpLYvbV41zgOOVP4jiojVjLZmkqU47ozhTxTccU4EY5qzMbJ0pLY4mx2I5pJGp0K7fm70AXximOcmhWzihqAIm6UylY0lAC0opBS0AHeikNFACUdKKSgAPSg0UUAFKKSloAQ9aTNK3rTetAC96KPeloAKilHQ1J3pkv3aAGqaf2qNTUgoAYcA0EZ2/WlYc0hOMUAPPTFApoOacBQAufWgU4AY5p24DoKABE3SKpyATg+tdDbafbWjeYquzDoWb/AArn1OHU+hzXWicL94YP0qJsuCIg4bqhA9RQQnv+tSSTlhnjioTMDn7v41kaikIP4iPxNNITIPmN+ZpPOHP3aQvk/wAOKAF+XH+sb86Y23/no350jOvHT86jYjsBTFcpy8yNznk03FOc5cn1OaStzAjkyfLC9SwArT0q0nhc3SwpOUyklvnLFD3ArNZHmlhiiGZHkCr25PStiJnSNTGkttqVsoypBIkAHP5j8DTQmTJdJZ3oto5pEsrvhkbO+I+g+vT8farc5Wed3WMIpPAFPjvLfUNO82RVeV5BIVP8BHAH4Af5zSZHYEfhTEUpLfCtIzkKoJP0rnYMzalBxkvMvH1Ird1q4ENlsGN8nH4d/wDD8azPDdubnxFpsfXNwhP0Byf5VE3ZM0grtHuRUbaglt45EKuoKkYIIzmrTL8tV5X2ivGb1PetocVrPgbTrp2ktSbWQ84QZQ/8B7fhiuFv/Dmpae7K0HmoOjxHIP4da9buJ8Zrn9SmGxq6qNaa0epw1qEHqtDy5oJA4V0ZfqMVIeOK0dRk33B9BWc3Wu9O6POas7Do26ink8VAowalbOBTEMNAozQKAHUfWkozQAlFGKKADNJilooAToKKWkPSgBBTjTRTutACHpTKf60w8GgBaWk60UALSOMoaWg9KAK61MpzUHQ/jUqHNACuM0xug+tSN0q1plpFe3axTbthBPynmk3ZXGld2Ka89qeM+lem2PgPRXRHY3DZHIMnH6CtiHwToUQBFirH/bYt/M1zSxUEdUcHUZ46kbyHCKWPoozV+30W+mI/c+Wv95zj9OtestoVnAuIrdEHbaMVlXtukW4KMDFSsTzaJFvB8q1ZxNvoqoQ0uZCOduOKveVt6RD8BTi5z98/nTS5/vH6ZFattmCSWwv7znCVGXk/iQU4sSOuaZub1oGNLE9VH5U3B54X8qUuf7wpNzeo/OmIYVf+6v5VE6FuxHuKmbd9fxqMsw4I/WgD/9k="
        request.image = IMG_DEFAULT

        if (id != null) {
            updateDetailViewModel.updateDetailUser(token = "Bearer ${sessionManager.fetchAuthToken()}", id, request).observe(
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
                                    Toast.makeText(context, "update data Gagal!", Toast.LENGTH_SHORT).show()
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
