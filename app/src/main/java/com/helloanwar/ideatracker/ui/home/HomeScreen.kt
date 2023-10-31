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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.helloanwar.ideatracker.NetworkResource
import com.helloanwar.ideatracker.ui.theme.IdeaTrackerTheme

@Composable
fun HomeScreen(
    onLogin: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    HomeScreenBody(
        onLogin = onLogin,
        state = state,
        onSubmit = { title, description ->
            viewModel.addIdea(
                idea = mapOf(
                    "title" to title,
                    "description" to description
                )
            )
        },
        onRetry = {
            viewModel.getIdeas()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBody(
    onLogin: () -> Unit,
    onSubmit: (String, String) -> Unit,
    onRetry: () -> Unit,
    state: HomeUiState
) {
    var showAddDialog by remember { mutableStateOf(false) }

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
                            onLogin()
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
            if (state.isLoggedIn) {
                ExtendedFloatingActionButton(
                    text = { Text("New Idea") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "New Idea") },
                    onClick = {
                        showAddDialog = true
                    }
                )
            }
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
            when (val ideas = state.ideas) {
                is NetworkResource.Error -> {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = ideas.error.message ?: "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 48.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.error
                                )
                            )
                            TextButton(
                                onClick = {
                                    onRetry()
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = "Try again")
                            }
                        }
                    }
                }

                NetworkResource.Idle,
                NetworkResource.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                is NetworkResource.Success -> {
                    if (ideas.data.documents.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Idea collections are empty",
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 16.dp
                                        )
                                        .padding(top = 48.dp)
                                        .align(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.titleSmall
                                )
                                if (!state.isLoggedIn) {
                                    Text(
                                        text = "Login to add new ideas",
                                        Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(horizontal = 16.dp)
                                            .padding(top = 8.dp),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    TextButton(
                                        onClick = {
                                            onLogin()
                                        },
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    ) {
                                        Text(text = "Login")
                                    }
                                }
                            }
                        }
                    }
                    items(ideas.data.documents) {
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
                                Text(
                                    text = it.data["title"] as String,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                (it.data["description"] as String?)?.let {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Surface(
                    modifier = Modifier.wrapContentHeight(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddIdea(
                        onDiscard = {
                            showAddDialog = false
                        },
                        onSubmit = onSubmit
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    IdeaTrackerTheme {
        HomeScreenBody(
            onLogin = {},
            onSubmit = { title, description -> },
            state = HomeUiState(),
            onRetry = {}
        )
    }
}

@Preview
@Composable
fun AddIdeaPreview() {
    IdeaTrackerTheme {
        AddIdea(onDiscard = {}, onSubmit = { title, description ->

        })
    }
}

@Composable
fun AddIdea(
    onDiscard: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var titleError by remember {
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
            onValueChange = {
                title = it
                titleError = ""
            },
            label = {
                Text(text = "Title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            isError = titleError.isNotBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (titleError.isNotBlank()) {
            Text(
                text = titleError, style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
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
                    if (title.isNotBlank()) {
                        onSubmit(title, description)
                    } else {
                        titleError = "Title can't be blank"
                    }
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