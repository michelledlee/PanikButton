package com.example.panikbutton

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.panikbutton.databinding.ActivityEmailpasswordBinding
import com.example.panikbutton.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailPasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEmailpasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProgressBar(binding.progressBar)

        // Buttons
        binding.emailSignInButton.setOnClickListener(this)
        binding.emailCreateAccountButton.setOnClickListener(this)
        binding.signOutButton.setOnClickListener(this)
        binding.verifyEmailButton.setOnClickListener(this)
        binding.reloadButton.setOnClickListener(this)

        // Initialize Firebase auth
        auth = Firebase.auth

    }

    /** Check if user is signed in and update UI **/
    public override fun onStart() {
        super.onStart()

        // Check if the user is signed in and update UI
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    /** Create new account for new user **/
    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }

        showProgressBar()

        // Create user with email
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in on successful create
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user) // Update for current user
                } else {
                    // Sign in failure
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }

                hideProgressBar()
            }
    }

    /** **/
    private fun signIn(email: String, password: String) {
        Log.d(TAG,"signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    checkForMultiFactorFailure(task.exception!!)
                }

                if (!task.isSuccessful) {
                    binding.status.setText(R.string.auth_failed)
                }
                hideProgressBar()
            } // End sign in with email
    }

    /** Sign out the user **/
    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

    /** Send email verification requested by user **/
    private fun sendEmailVerification() {
        // Disable button once clicked
        binding.verifyEmailButton.isEnabled = false

        // Send email
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // After completion, re-enable button
                binding.verifyEmailButton.isEnabled = true

                // Action based on status of email
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }

            } // End send_email_verification
    }

    /** Reloads the user if authentication was successful **/
    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
                Toast.makeText(this@EmailPasswordActivity, "Reload successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "reload", task.exception)
                Toast.makeText(this@EmailPasswordActivity, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        // Check if email is valid
        val email = binding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.fieldEmail.error = "Required."
            valid = false
        } else {
            binding.fieldEmail.error = null
        }

        // Check if password is valid
        val password = binding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.fieldPassword.error = "Required."
            valid = false
        } else {
            binding.fieldPassword.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressBar()
        if (user != null) {
            binding.status.text = getString(R.string.emailpassword_status_fmt, user.email, user.isEmailVerified)
            binding.detail.text = getString(R.string.firebase_status_fmt, user.uid)

            binding.emailPasswordButtons.visibility = View.GONE
            binding.emailPasswordFields.visibility = View.GONE
            binding.signedInButtons.visibility = View.GONE

            if (user.isEmailVerified) {
                binding.verifyEmailButton.visibility = View.GONE
            } else {
                binding.verifyEmailButton.visibility = View.VISIBLE
            }

            val intent = Intent(this, ProfileActivity::class.java)
//            intent.putExtra("key", value)
            startActivity(intent)

        } else {
            binding.status.setText(R.string.signed_out)
            binding.detail.text = null

            binding.emailPasswordButtons.visibility = View.VISIBLE
            binding.emailPasswordFields.visibility = View.VISIBLE
            binding.signedInButtons.visibility = View.GONE
        }
    }

    /** Check for multi-factor authentication failure **/
    private fun checkForMultiFactorFailure(e: Exception) {
        if (e is FirebaseAuthMultiFactorException) {
            Log.w(TAG, "multiFactorFailure", e)
            val intent = Intent()
            val resolver = e.resolver
            intent.putExtra("EXTRA_MFA_RESOLVER", resolver)
            setResult(MultiFactorActivity.RESULT_NEEDS_MFA_SIGN_IN, intent)
            finish()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.emailCreateAccountButton -> {
                createAccount(binding.fieldEmail.text.toString(), binding.fieldPassword.text.toString())
            }
            R.id.emailSignInButton -> signIn(binding.fieldEmail.text.toString(), binding.fieldPassword.text.toString())
            R.id.signOutButton -> signOut()
            R.id.verifyEmailButton -> sendEmailVerification()
            R.id.reloadButton -> reload()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }
}