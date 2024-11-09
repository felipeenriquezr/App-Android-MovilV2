package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.Toast

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

            // Validar si los campos no están vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                // Lógica de autenticación (simulada aquí)
                if (authenticateUser(email, password)) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    // Redirigir a la actividad CarryCompass (lista de productos)
                    val intent = Intent(this, CarryCompass::class.java)
                    startActivity(intent)
                    finish() // Finaliza la actividad actual para que no regrese con el botón de retroceso
                } else {
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
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

    // Función para simular autenticación (puedes sustituirla por tu lógica real, como una consulta a un servidor)
    private fun authenticateUser(email: String, password: String): Boolean {
        // Lógica de autenticación simulada
        return email == "usuario@example.com" && password == "123456"
    }
}
