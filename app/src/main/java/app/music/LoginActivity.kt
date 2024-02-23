package app.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import app.music.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener{
            val email = binding.emailEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()

            if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)){
                binding.emailEdittext.setError("Invalid email")
                return@setOnClickListener
            }

            if(password.length<6){
                binding.passwordEdittext.setError("Length should be 6 char")
                return@setOnClickListener
            }

            loginWithFirebase(email, password)
        }

        binding.gotoSignupBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    fun loginWithFirebase(email: String, password: String){
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                setInProgress(false)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
                //Toast.makeText(applicationContext, "User login successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                setInProgress(false)
                Toast.makeText(applicationContext, "Login account failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun setInProgress(inProgress: Boolean){
        if(inProgress){
            binding.loginBtn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.loginBtn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        FirebaseAuth.getInstance().currentUser?.apply {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}