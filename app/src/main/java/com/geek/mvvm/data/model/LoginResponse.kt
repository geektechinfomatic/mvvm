package com.geek.mvvm.data.model

import com.google.gson.annotations.SerializedName

data class   LoginResponse(   @SerializedName("errorCode")
                         var errorCode: String,
                         @SerializedName("errorMessage")
                         var errorMessage: String,
                              @SerializedName("user")
                              var user: User,
                              ) {
}
data class User( @SerializedName("userId")
                 var userId: String,
                 @SerializedName("userName")
                 var userName: String){

}