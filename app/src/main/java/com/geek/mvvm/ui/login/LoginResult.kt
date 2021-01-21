package com.geek.mvvm.ui.login

import com.geek.mvvm.data.model.LoginResponse

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val success: LoginResponse? = null,
        val error: Int? = null,
        val loading: String? = null,
)