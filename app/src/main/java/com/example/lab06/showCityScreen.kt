package com.example.lab06

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

@Composable
fun showCityScreen(context: Context, navController: NavController, url: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3D51AE)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround

    ){
        var imgUrlTest by rememberSaveable { mutableStateOf(url) }
        Text(
            text =  "Bienvenido a $url",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp)
        )
        fetchImageWithName(
            context,
            url,
            onSuccess = {ImageLink ->
                imgUrlTest = ImageLink
            },
            onError = {

            }
        )
        AsyncImage(
            model = "$imgUrlTest",
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            error = painterResource(id = R.drawable.ic_placeholder),
            contentDescription = "City Images",
        )
    }
}

fun fetchImageWithName(context: Context, cityName: String?, onSuccess: (String) -> Unit, onError: () -> Unit){
    var urlForImages = "https://api.teleport.org/api/urban_areas/slug:"
    urlForImages = "$urlForImages$cityName/images/"
    val queue: RequestQueue = Volley.newRequestQueue(context)

    val request = StringRequest(
        Request.Method.GET, urlForImages,
        { response ->
            try{
                val data = response.toString()
                val jData = JSONObject(data).getJSONArray("photos").getJSONObject(0).getJSONObject("image")
                val ImageLink = jData.getString("mobile")
                Log.e("Objects",ImageLink)
                onSuccess(ImageLink)

            } catch (e: Exception){
                onError
            }
        },
        {
            onError()
        }
    )
    queue.add(request)
}