package com.geek.mvvm.data

import androidx.lifecycle.MutableLiveData
import com.geek.mvvm.data.model.LoggedInUser
import com.geek.mvvm.data.model.LoginResponse
import com.geek.mvvm.ui.login.LoginResult
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoginResponse? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login( username: String, password: String,callback: LoginDataSource.DataSourceCallback) {
        // handle login
     dataSource.login(username, password,object :LoginDataSource.DataSourceCallback{
         override fun dataSourceResponse(result: Result<LoginResponse>) {
             if (result is Result.Success) {
                 setLoggedInUser(result.data)
             }
             callback.dataSourceResponse(result)
         }
     })

      /*  if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result*/
    }

    private fun setLoggedInUser(loggedInUser: LoginResponse) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}