package com.example.miperfil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Activity 1: Pantalla de Bienvenida
 * Su unica funcion es dar la bienvenida y llevar al usuario, mediante el
 * boton "Iniciar", hacia la pantalla de Registro de Perfil.
 */
class MainActivity : AppCompatActivity() {

    // 1. Variables
    private lateinit var btnIniciar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 2. Establecer archivo layout
        setContentView(R.layout.activity_main)

        val root: View = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 3. Referencias a elementos de la UI
        btnIniciar = findViewById(R.id.btnIniciar)

        // 4. Listener del boton Iniciar
        btnIniciar.setOnClickListener {
            val intent = Intent(this, RegistroPerfilActivity::class.java)
            startActivity(intent)
        }
    }
}
