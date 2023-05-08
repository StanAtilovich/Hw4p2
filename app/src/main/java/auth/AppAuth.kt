package auth

import android.content.Context
import androidx.core.content.edit
import com.example.hw4.DTO.PushToken
import com.example.hw4.api.Api
import com.example.hw4.model.AuthModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AppAuth private constructor(context: Context){

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)


    private val _data: MutableStateFlow<AuthModel?>
    init {
        val token = prefs.getString(TOKEN_KEY, null)
        val id = prefs.getLong(ID_KEY, 0L)
        if (token == null || id == 0L){
            _data = MutableStateFlow(null)
            prefs.edit{clear()}
        }else {
            _data = MutableStateFlow(AuthModel(id, token))
        }
        sendPushToken()
    }
    val data = _data.asStateFlow()

    @Synchronized
    fun setAuth(id: Long, token: String){
        _data.value = AuthModel(id, token)
        prefs.edit {
            putLong(ID_KEY, id)
            putString(TOKEN_KEY, token)
        }
        sendPushToken()
    }

    @Synchronized
    fun removeAuth(){
        _data.value = null
        prefs.edit{ clear()}
        sendPushToken()
    }

    fun sendPushToken(token: String? = null){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                Api.service.saveToken(pushToken)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }


    companion object{
        @Volatile
        private var INSTANCE: AppAuth? = null
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val ID_KEY = "ID_KEY"

        fun getInstance(): AppAuth = synchronized(this){
            requireNotNull(INSTANCE){
                "You must call init(context: Context"
            }
        }
        fun init(context: Context): AppAuth = synchronized(this) {
            INSTANCE ?: AppAuth(context).apply {
                INSTANCE = this
            }
        }
    }
}