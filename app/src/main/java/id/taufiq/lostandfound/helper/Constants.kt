package id.taufiq.lostandfound.helper

/**
 * Created By Gogxi on 17/11/2020.
 *
 */

object Constants {
    // Endpoints
    const val BASE_URL = "https://megabit-lostnfound.herokuapp.com/"
    const val REGISTER_URL = "api/v1/android/auth/register"
    const val LOGIN_URL = "api/v1/android/auth/login"
    const val LOGOUT_URL = "api/v1/android/auth/logout"
    const val LIST_USER_URL = "api/v1/android/users/"
    const val SING_UP_GOOGLE_URL = "api/v1/android/auth/oauth2/google/authorize"

    //Sing_Up_Google
    const val RC_SIGN_IN = 120
    const val CLIENT_ID = "78806170996-palomanpb7h85m39le96ilm2mqd2jjdo.apps.googleusercontent.com"
//    const val CLIENT_ID = "78806170996-ojo8p5h9k278qpi14cnt3r3sdrdce7g5.apps.googleusercontent.com"
//    const val CLIENT_ID ="304092645592-42g8iugfoohtdgq3jqgio9h67ojb7vhh.apps.googleusercontent.com"

    // Default Image
    const val IMG_DEFAULT = "data:image/jpeg;base64,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"
}