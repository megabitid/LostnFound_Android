package id.taufiq.lostandfound.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created By Taufiq on 11/9/2020.
 *
 */


// show toast
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

//move to another activity
inline fun <reified T : Activity> Context.openActivity(vararg params: Pair<String, String>) {
    val intent = Intent(this, T::class.java)
    params.forEach { intent.putExtra(it.first, it.second) }


}


