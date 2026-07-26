package com.example.miperfil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Activity 3: Perfil Guardado
 * Muestra un resumen claro y ordenado de los datos ingresados en la
 * Activity 2 y permite regresar al inicio o registrar un nuevo perfil.
 */
class PerfilGuardadoActivity : AppCompatActivity() {

    // 1. Variables
    private lateinit var tvResumen: TextView
    private lateinit var btnRegresarInicio: Button
    private lateinit var btnNuevoPerfil: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 2. Establecer archivo layout
        setContentView(R.layout.activity_perfil_guardado)

        val root: View = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 3. Referencias a elementos de la UI
        tvResumen = findViewById(R.id.tvResumen)
        btnRegresarInicio = findViewById(R.id.btnRegresarInicio)
        btnNuevoPerfil = findViewById(R.id.btnNuevoPerfil)

        // 4. Mostrar los datos recibidos por intent
        mostrarResumen()

        // 5. Listener boton Regresar al inicio
        btnRegresarInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // 6. Listener boton Nuevo perfil
        btnNuevoPerfil.setOnClickListener {
            val intent = Intent(this, RegistroPerfilActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarResumen() {
        val nombres = intent.getStringExtra("NOMBRES") ?: ""
        val correo = intent.getStringExtra("CORREO") ?: ""
        val telefono = intent.getStringExtra("TELEFONO") ?: ""
        val fechaNacimiento = intent.getStringExtra("FECHA_NACIMIENTO") ?: ""
        val direccion = intent.getStringExtra("DIRECCION") ?: ""
        val permisoCamara = intent.getBooleanExtra("PERMISO_CAMARA", false)

        val estadoPermiso = if (permisoCamara) {
            getString(R.string.permiso_camara_concedido)
        } else {
            getString(R.string.permiso_camara_denegado)
        }

        tvResumen.text = getString(
            R.string.formato_resumen,
            nombres, correo, telefono, fechaNacimiento, direccion, estadoPermiso
        )
    }
}
