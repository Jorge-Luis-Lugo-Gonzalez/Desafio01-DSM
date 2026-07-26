package com.example.miperfil

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Activity 2: Registro de Perfil
 * Formulario de datos personales + solicitud de permiso de camara en tiempo
 * de ejecucion (mismo patron que CamaraActivity de la Guia04) + validacion
 * de datos antes de continuar a la pantalla de resumen.
 */
class RegistroPerfilActivity : AppCompatActivity() {

    // 1. Variables
    private lateinit var etNombres: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etDireccion: EditText
    private lateinit var btnTomarFoto: Button
    private lateinit var tvEstadoPermiso: TextView
    private lateinit var btnGuardar: Button

    private var permisoCamaraConcedido = false

    companion object {
        private const val FORMATO_FECHA = "dd/MM/yyyy"
        private const val CLAVE_PERMISO_CAMARA = "PERMISO_CAMARA"
    }

    // 2. Registro de la solicitud de permiso (API Activity Result)
    private val solicitarPermisoCamara = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoCamaraConcedido = concedido
        tvEstadoPermiso.text = if (concedido) {
            getString(R.string.permiso_camara_concedido)
        } else {
            getString(R.string.permiso_camara_denegado)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 3. Establecer archivo layout
        setContentView(R.layout.activity_registro_perfil)

        val root: View = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 4. Referencias a elementos de la UI
        etNombres = findViewById(R.id.etNombres)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etDireccion = findViewById(R.id.etDireccion)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)
        tvEstadoPermiso = findViewById(R.id.tvEstadoPermiso)
        btnGuardar = findViewById(R.id.btnGuardar)

        // 5. Recuperar estado del permiso ante un cambio de configuracion
        //    (rotacion de pantalla). Los EditText restauran su texto solos
        //    porque ya tienen un id asignado; el permiso lo restauramos
        //    manualmente con onSaveInstanceState, igual que en la Guia03.
        if (savedInstanceState != null) {
            permisoCamaraConcedido = savedInstanceState.getBoolean(CLAVE_PERMISO_CAMARA, false)
            if (permisoCamaraConcedido) {
                tvEstadoPermiso.text = getString(R.string.permiso_camara_concedido)
            }
        }

        // 6. Listener boton Tomar Foto
        btnTomarFoto.setOnClickListener {
            comprobarPermisoCamara()
        }

        // 7. Listener boton Guardar
        btnGuardar.setOnClickListener {
            validarYGuardar()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CLAVE_PERMISO_CAMARA, permisoCamaraConcedido)
    }

    // 8. Comprobar / solicitar el permiso de camara (mismo flujo que Guia04 - Parte 3)
    private fun comprobarPermisoCamara() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                permisoCamaraConcedido = true
                tvEstadoPermiso.text = getString(R.string.permiso_camara_concedido)
            }
            else -> {
                solicitarPermisoCamara.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // 9. Validar los datos ingresados y, si todo es correcto, enviarlos a la Activity 3
    private fun validarYGuardar() {
        val nombres = etNombres.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val fechaNacimiento = etFechaNacimiento.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()

        // Validar campos vacios
        if (nombres.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
            fechaNacimiento.isEmpty() || direccion.isEmpty()
        ) {
            mostrarError(getString(R.string.error_campos_vacios))
            return
        }

        // Validar formato de correo
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            mostrarError(getString(R.string.error_correo_invalido))
            return
        }

        // Validar telefono (solo digitos, entre 8 y 15 caracteres)
        if (!telefono.matches(Regex("^[0-9]{8,15}$"))) {
            mostrarError(getString(R.string.error_telefono_invalido))
            return
        }

        // Validar formato de fecha dd/MM/yyyy
        if (!esFechaValida(fechaNacimiento)) {
            mostrarError(getString(R.string.error_fecha_invalida))
            return
        }

        // Datos validos: se envian a la Activity 3 mediante un intent explicito
        val intent = Intent(this, PerfilGuardadoActivity::class.java)
        intent.putExtra("NOMBRES", nombres)
        intent.putExtra("CORREO", correo)
        intent.putExtra("TELEFONO", telefono)
        intent.putExtra("FECHA_NACIMIENTO", fechaNacimiento)
        intent.putExtra("DIRECCION", direccion)
        intent.putExtra(CLAVE_PERMISO_CAMARA, permisoCamaraConcedido)
        startActivity(intent)
    }

    private fun esFechaValida(fecha: String): Boolean {
        // Primero se valida el formato exacto dd/MM/yyyy con una expresion regular
        if (!fecha.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))) return false
        // Luego se valida que sea una fecha real (ej. rechaza 31/02/2020)
        return try {
            val formato = SimpleDateFormat(FORMATO_FECHA, Locale.getDefault())
            formato.isLenient = false
            formato.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
