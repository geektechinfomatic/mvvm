package com.geek.mvvm.ui.login

import com.geek.mvvm.data.model.LoginResponse

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
        val displayName: String
        //... other data fields that may be accessible to the UI
)