package com.example.whitenoiseapp.util

import androidx.fragment.app.Fragment
import com.example.whitenoiseapp.MainActivity

fun Fragment.getMainActivity(): MainActivity {
    return requireActivity() as MainActivity
}