package com.helloanwar.ideatracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helloanwar.ideatracker.ui.theme.IdeaTrackerTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    HomeScreenBody()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBody() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showAddIdeaBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home")
                },
                actions = {
                    IconButton(
                        onClick = {
                            
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Account"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("New Idea") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "New Idea") },
                onClick = {
                    showAddIdeaBottomSheet = true
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Latest ideas",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Title 1", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Lorem ip sum lorem ipsum",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        if (showAddIdeaBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddIdeaBottomSheet = false
                },
                sheetState = sheetState
            ) {
                AddIdea(
                    onDiscard = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showAddIdeaBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    IdeaTrackerTheme {
        HomeScreenBody()
    }
}

@Preview
@Composable
fun AddIdeaPreview() {
    IdeaTrackerTheme {
        AddIdea(onDiscard = {})
    }
}

@Composable
fun AddIdea(onDiscard: () -> Unit) {
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = {
                Text(text = "Title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = {
                Text(text = "Description")
            },
            maxLines = 5,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 100.dp
                )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {

                }
            ) {
                Text(text = "Save")
            }
            TextButton(
                onClick = {
                    onDiscard()
                }
            ) {
                Text(text = "Discard")
            }
        }
    }
}