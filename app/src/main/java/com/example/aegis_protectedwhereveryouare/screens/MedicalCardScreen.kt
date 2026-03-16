package com.example.aegis_protectedwhereveryouare.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MedicalCardScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var conditions by remember { mutableStateOf("") }
    var medications by remember { mutableStateOf("") }
    var translatedCard by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("← Back")
            }
            Text(
                text = "📋 Medical Card",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bloodType,
            onValueChange = { bloodType = it },
            label = { Text("Blood Type (e.g. A+, O-)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = allergies,
            onValueChange = { allergies = it },
            label = { Text("Allergies (e.g. Penicillin, Peanuts)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = conditions,
            onValueChange = { conditions = it },
            label = { Text("Medical Conditions (e.g. Diabetes)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = medications,
            onValueChange = { medications = it },
            label = { Text("Current Medications") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    try {
                        val result = withContext(Dispatchers.IO) {
                            val apiKey = "AIzaSyDA7qeDkdDPiuVu-0TI7zdeFowZoLp8zX8"
                            val url = java.net.URL("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=$apiKey")
                            val connection = url.openConnection() as java.net.HttpURLConnection
                            connection.requestMethod = "POST"
                            connection.setRequestProperty("Content-Type", "application/json")
                            connection.doOutput = true

                            val requestBody = """
                                {
                                  "contents": [{
                                    "parts": [{
                                      "text": "Create a medical emergency card in both English and Korean for:\nName: $name\nBlood Type: $bloodType\nAllergies: $allergies\nMedical Conditions: $conditions\nCurrent Medications: $medications\n\nFormat it clearly for a doctor to read quickly in an emergency."
                                    }]
                                  }]
                                }
                            """.trimIndent()

                            connection.outputStream.write(requestBody.toByteArray())

                            val response = connection.inputStream.bufferedReader().readText()
                            val jsonResponse = org.json.JSONObject(response)
                            jsonResponse
                                .getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text")
                        }
                        translatedCard = result
                    } catch (e: Exception) {
                        translatedCard = "Error: ${e.message}"
                    }
                    isLoading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("🤖 Generate Medical Card", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (translatedCard.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = translatedCard,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
