package com.example.talevoice.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.TaleApplication
import com.example.talevoice.viewmodel.TaleListViewModel
import com.example.talevoice.viewmodel.TaleListViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun TaleListScreen(navController: NavHostController, modifier: Modifier) {
    val repository = (LocalContext.current.applicationContext as TaleApplication).taleRepository
    val viewModel: TaleListViewModel = viewModel(
        factory = TaleListViewModelFactory(repository)
    )

    val taleList by viewModel.taleList.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    Column {
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        LazyColumn {
            itemsIndexed(taleList) { index, tale ->
                Log.d("TaleListScreen", tale.title)
                Log.d("TaleListScreen", tale.taleId)
                Text(text = tale.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Thin,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isLoading){
                                return@clickable
                            }
                            isLoading = true
                            viewModel.viewModelScope.launch {
                                try {
                                    val taleDetail = viewModel.getTaleDetail(tale.taleId)
                                    isLoading = false
                                    navController.navigate("tale_content") {
                                        popUpTo("tale_list")
                                    }
                                } catch (e: Exception) {
                                    isLoading = false
                                    println("Error fetching tale details: ${e.message}")
                                }
                            }
                        }
                        .padding(16.dp))
                if (index < taleList.size - 1) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

        }
    }
}