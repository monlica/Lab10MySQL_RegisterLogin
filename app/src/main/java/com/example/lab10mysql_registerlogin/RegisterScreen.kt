package com.example.lab10mysql_registerlogin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current
    val createClient = StudentAPI.create()
    var studentID by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    //ให้นศ.เพิ่ม
    var name by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    var isButtonEnabled by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = studentID,
            onValueChange = {
                studentID = it
                isButtonEnabled = validateInput(studentID, name, password, selectedGender)
            },
            label = { Text("Student ID") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                isButtonEnabled = validateInput(studentID, name, password,selectedGender)
            },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        // RadioButton for selecting gender
        selectedGender =  KindRadioGroupUsage()

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isButtonEnabled = validateInput(studentID, name, password, selectedGender)
            },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                createClient.registerStudent(
                    studentID, "", password, "Other"
                ).enqueue(object : Callback<LoginClass> {
                    override fun onResponse(
                        call: Call<LoginClass>,
                        response: Response<LoginClass>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(contextForToast,"Successfully Inserted",
                                Toast.LENGTH_SHORT).show()
                            if (navController.currentBackStack.value.size >= 2) {
                                navController.popBackStack()
                            }
                            navController.navigate(Screen.Login.route)
                        } else {
                            Toast.makeText(contextForToast, "Inserted Failed",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginClass>, t: Throwable) {
                        Toast.makeText(contextForToast,"Error onFailure " +
                                t.message, Toast.LENGTH_LONG).show()
                    }
                })
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Register")
        }
    }

}


fun validateInput(studentID: String, name: String, password: String, selectedGender:String)
        : Boolean  {
    return !studentID.isNullOrEmpty() && !name.isNullOrEmpty()
            && !password.isNullOrEmpty() && !selectedGender.isNullOrEmpty()
}

@Composable
fun KindRadioGroupUsage():String {
    val kinds = listOf("Male", "Female", "Other")
    val (selected, setSelected) = remember { mutableStateOf("") }
    Text(
        text = "Student Gender :",
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
    )
    Row (modifier = Modifier.fillMaxWidth()
        .padding(start = 16.dp)){
        MyRadioGroup(
            mItems = kinds,
            selected, setSelected
        )
    }
    return selected
}

@Composable
fun MyRadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        mItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item,
                    onClick = {
                        setSelected(item)
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Green
                    )
                )
                Text(text = item, modifier = Modifier.padding(start = 5.dp))
            }
        }
    }
}
