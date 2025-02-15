package com.example.whitenoiseapp

import androidx.fragment.app.Fragment

fun Fragment.getMainActivity(): MainActivity {
    return requireActivity() as MainActivity
}