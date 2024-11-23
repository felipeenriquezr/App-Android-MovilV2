package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.Toast
import com.example.app.models.AuthResponse
import com.example.app.models.LoginRequest
import com.example.app.network.ApiClient
import com.example.app.network.ApiService
import retrofit2.Call

class MainActivity : ComponentActivity() {

    private val defaultEmail = "usuario@example.com"
    private val defaultPassword = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Asociamos los componentes del XML con variables en Kotlin
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Pre-fill the EditText fields with default values for testing
        emailEditText.setText(defaultEmail)
        passwordEditText.setText(defaultPassword)

        // Acción para el botón de "Iniciar Sesión"
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                // Llamada a authenticateUser con callback
                authenticateUser(email, password) { success ->
                    if (success) {
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CarryCompass::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Acción para el botón de "Registro"
        registerButton.setOnClickListener {
            // Redirigir a la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun authenticateUser(email: String, password: String, callback: (success: Boolean) -> Unit) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        apiService.login(loginRequest).enqueue(object : retrofit2.Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: retrofit2.Response<AuthResponse>) {
                if (response.isSuccessful) {
                    callback(true) // Éxito
                } else {
                    callback(false) // Error (por ejemplo, credenciales incorrectas)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                callback(false) // Error de red
            }
        })
    }
}
