package com.example.mviapp.data

import android.util.Base64
import javax.inject.Inject

// Simple wrapper for Android framework function to allow unit testing
class Base64Decoder @Inject constructor() {
    fun decode(encodedString: String) =
        Base64.decode(encodedString, 0)
            .toString(Charsets.UTF_8)

    fun encode(plainString: String) =
        Base64.encodeToString(plainString.toByteArray(), 0)
}