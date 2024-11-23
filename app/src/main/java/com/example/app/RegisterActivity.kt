package com.example.app

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.models.RegisterRequest
import com.example.app.models.AuthResponse
import com.example.app.network.ApiClient
import com.example.app.network.ApiService
import kotlinx.coroutines.launch
import java.util.*
import java.text.SimpleDateFormat
import retrofit2.Response

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Asociar los componentes del layout
        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val phoneNumberEditText = findViewById<EditText>(R.id.phoneNumberEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val roleEditText = findViewById<EditText>(R.id.roleEditText)
        val birthDateEditText = findViewById<EditText>(R.id.birthDateEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        birthDateEditText.setOnClickListener {
            showDatePickerDialog(birthDateEditText)
        }

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val role = roleEditText.text.toString()
            val birthDate = birthDateEditText.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                role.isEmpty() || birthDate.isEmpty()
            ) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val birthDateObject: Date = try {
                dateFormat.parse(birthDate)!!
            } catch (e: Exception) {
                Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formattedDate = dateFormat.format(birthDateObject)
            val registerRequest = RegisterRequest(
                firstName = firstName,
                lastName = lastName,
                username = email,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                brithOfDate = formattedDate,
                role = role
            )

            registerUser(registerRequest)
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun registerUser(request: RegisterRequest) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        lifecycleScope.launch {
            try {
                val response: Response<AuthResponse> = apiService.register(request)
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    sharedPreferences.edit().putString("TOKEN_KEY", token).apply()
                    redirectToMainActivity()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@RegisterActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectToMainActivity() {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
