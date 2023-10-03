package com.example.lab06

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lab06.navigation.AppNavigation
import com.example.lab06.navigation.AppScreens
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun MainActivity(navController: NavController, context: Context){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF284B630))
    ){
        var imgUrl by rememberSaveable { mutableStateOf("Prueba") }
        Button(
            modifier = Modifier
                .width(280.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xffff5757)
            ),
            onClick = {
                jsonParse(context)
                navController.navigate(route = AppScreens.showCityScreen.route + "/$imgUrl")
            }) {
            Text(text = "Continuar")
        }
    }
}


fun jsonParse(current: Context) {
    val urllink = "https://api.teleport.org/api/urban_areas/"
    val queue: RequestQueue = Volley.newRequestQueue(current)


    val request = StringRequest(
        Request.Method.GET,urllink,
        { response ->

            val data = response.toString()
            val jarray = JSONObject(data).getJSONObject("_links").getJSONArray("ua:item")
            Log.e("Objects",jarray.toString())
        },
        { })
    queue.add(request)

}
