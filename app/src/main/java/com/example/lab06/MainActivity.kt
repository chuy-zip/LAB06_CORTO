package com.example.lab06

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lab06.navigation.AppNavigation
import com.example.lab06.navigation.AppScreens
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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
fun MainActivity(navController: NavController, context: Context) {
    val citiesList = remember { mutableStateOf<List<City>>(emptyList()) }
    var imgUrl by rememberSaveable { mutableStateOf("Prueba") }
    val searchText by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF284B630)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .width(280.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xffff5757)
            ),
            onClick = {

                fetchCitiesData(
                    context,
                    onSuccess = { cities ->
                        citiesList.value = cities
                    },
                    onError = {
                        // Handle errors, such as network request failures or JSON parsing errors
                        Toast.makeText(context, "Error while fetching", Toast.LENGTH_LONG).show()
                    }
                )
            }
        ) {
            Text(text = "Fetch Data")
        }

        ShowCitiesScreen(navController = navController, citiesList = citiesList.value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCitiesScreen(navController: NavController, citiesList: List<City>) {
    Column {


        LazyColumn {
            items(citiesList) { city ->
                CityItem(city = city, navController = navController)
            }
        }
    }
}

@Composable
fun CityItem(city: City, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                expanded = !expanded
                var nameC = city.url
                val parsedString = nameC.substringAfterLast("slug:").removeSuffix("/")

                navController.navigate(route = AppScreens.showCityScreen.route + "/$parsedString")
            },
    ) {
        Column {
            Text(
                text = city.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        // Use the same delimiter to extract the city URL
                        var nameC = city.url
                        val parsedString = nameC.substringAfterLast("slug:").removeSuffix("/")

                        navController.navigate(route = AppScreens.showCityScreen.route + "/$parsedString")
                    },
            )
        }
    }
}


fun fetchCitiesData(context: Context, onSuccess: (List<City>) -> Unit, onError: () -> Unit) {
    val urllink = "https://api.teleport.org/api/urban_areas/"
    val queue: RequestQueue = Volley.newRequestQueue(context)

    val request = StringRequest(
        Request.Method.GET, urllink,
        { response ->
            try {
                val data = response.toString()
                val jArray = JSONObject(data).getJSONObject("_links").getJSONArray("ua:item")

                val citiesList = mutableListOf<City>()
                for (i in 0 until jArray.length()) {
                    val cityObject = jArray.getJSONObject(i)
                    val name = cityObject.getString("name")
                    val href = cityObject.getString("href")
                    citiesList.add(City(href, name))
                }
                Log.e("Objects",jArray.toString())
                onSuccess(citiesList)
            } catch (e: Exception) {
                // Handle JSON parsing errors
                onError()
            }
        },
        {
            // Handle network request errors
            onError()
        }
    )
    queue.add(request)
}

