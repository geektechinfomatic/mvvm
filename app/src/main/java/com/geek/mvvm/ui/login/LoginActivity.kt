package com.geek.mvvm.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*

import com.geek.mvvm.R
import com.geek.mvvm.data.model.LoginResponse
import dagger.Binds
import dagger.Component
import dagger.Module
import org.w3c.dom.Text
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var loginViewModel: LoginViewModel
    val TAG = "LOGIN_ACTIVITY"
    private lateinit var textViewUser:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
          textViewUser = findViewById<TextView>(R.id.textViewUser)
        val loading = findViewById<ProgressBar>(R.id.loading)
        DaggerLoginComponent.create().inject(this)
        loginViewModel = ViewModelProvider(this, factory)
                .get(LoginViewModel::class.java)



        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            Log.d(TAG, "loginResult() called:$loginResult")

            if (loginResult.loading != null) {
                loading.visibility = View.VISIBLE
                textViewUser.text=loginResult.loading
                    //showLoginFailed(loginResult.error)
            }
            if (loginResult.error != null) {
                loading.visibility = View.GONE
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                loading.visibility = View.GONE
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            //finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: LoginResponse) {

        Log.d(TAG, "updateUiWithUser() called with: model = $model")
        val welcome = getString(R.string.welcome)
        val displayName = model.errorMessage
        // : initiate successful logged in experience
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
        ).show()
        textViewUser.text="${model.user?.userId}\n ${model.user?.userName}"
        Log.d(TAG, "updateUiWithUser() called with: model = $model" + "$welcome $displayName")
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

@Component(modules = [LoginModule::class])
interface LoginComponent {
    fun inject(loginActivity: LoginActivity)
}

@Module
abstract class LoginModule {
    @Binds
    abstract fun bindLoginViewModelFactory(loginViewModelFactory: LoginViewModelFactory): ViewModelProvider.Factory
}