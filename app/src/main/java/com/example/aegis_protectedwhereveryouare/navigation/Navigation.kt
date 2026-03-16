package com.example.aegis_protectedwhereveryouare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aegis_protectedwhereveryouare.screens.EmergencyScreen
import com.example.aegis_protectedwhereveryouare.screens.HospitalFinderScreen
import com.example.aegis_protectedwhereveryouare.screens.MedicalCardScreen

sealed class Screen(val route: String) {
    object Emergency : Screen("emergency")
    object HospitalFinder : Screen("hospital_finder")
    object MedicalCard : Screen("medical_card")
}

@Composable
fun AegisNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Emergency.route
    ) {
        composable(Screen.Emergency.route) {
            EmergencyScreen(navController)
        }
        composable(Screen.HospitalFinder.route) {
            HospitalFinderScreen(navController)
        }
        composable(Screen.MedicalCard.route) {
            MedicalCardScreen(navController)
        }
    }
}
