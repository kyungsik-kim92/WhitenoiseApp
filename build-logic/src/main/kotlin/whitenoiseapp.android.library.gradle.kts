import com.example.whitenoiseapp.configureComposeAndroid
import com.example.whitenoiseapp.configureHiltAndroid
import com.example.whitenoiseapp.configureKotlinAndroid


plugins{
    id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()
configureComposeAndroid()