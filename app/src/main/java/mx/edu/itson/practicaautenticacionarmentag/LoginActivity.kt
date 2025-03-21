package mx.edu.itson.practicaautenticacionarmentag

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val email : EditText = findViewById(R.id.et_email)
        val password : EditText = findViewById(R.id.et_password)
        val errorTv : TextView = findViewById(R.id.tvError)

        val button : Button = findViewById(R.id.btnLogin)

        errorTv.visibility = View.INVISIBLE

        // Set the button click listener to handle login
        button.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()

            // Check if email and password are not empty
            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                login(emailText, passwordText)
            } else {
                showError("Por favor, ingresa un correo y una contraseña", true)
            }
        }
    }

    fun goToMain(user: FirebaseUser){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", user.email)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun showError(text:String ="", visible: Boolean){
        val errorTv : TextView =findViewById(R.id.tvError)

        errorTv.text=text

        errorTv.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
    public override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            goToMain(currentUser)
        }
    }

    fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    showError(visible = false)
                    goToMain(user!!)
                } else {
                    showError("Usuario y/o contraseña equivocados", true)
                }
            }
    }
}
