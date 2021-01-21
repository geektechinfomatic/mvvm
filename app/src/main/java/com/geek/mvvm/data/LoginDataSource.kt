package com.geek.mvvm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.geek.mvvm.data.model.LoggedInUser
import com.geek.mvvm.data.model.LoginResponse
import com.geek.mvvm.network.GithubApi
import com.geek.mvvm.ui.login.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(){

    fun login(username: String, password: String,callback:DataSourceCallback) {


        val api = GithubApi.retrofitService.login("application/json","357175048449937","510110406068589",
            LoggedInUser(username,password)
        )




        api?.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                Log.d("TAG_TAG", "Failed :" + t.message)

                callback.dataSourceResponse(     Result.Error(Exception(t.message,t)))
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("TAG_TAG", "onResponse :" + response.isSuccessful)
                val login=response.body() as LoginResponse;
                Log.d("TAG_TAG", "response.body() :" + response.body())
                callback.dataSourceResponse(    Result.Success(login))
                //_userData.value = response.body()
            }
        })



//        try {
//            // TODO: handle loggedInUser authentication
//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
//            return Result.Success(fakeUser)
//        } catch (e: Throwable) {
//            return Result.Error(IOException("Error logging in", e))
//        }
        callback.dataSourceResponse(Result.Loading("Loading! please wait"))
          //  return Result.Loading("Loading! please wait")
    }

    fun logout() {
        // TODO: revoke authentication
    }
    interface DataSourceCallback{
        fun dataSourceResponse(result: Result<LoginResponse>)
    }
}