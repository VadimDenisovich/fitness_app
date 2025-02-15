package ru.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.example.myapplication.databinding.ActivityLoginBinding

// LoginActivity.kt (app/src/main/java/com/yourpackage/)
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inflate: XML → View
        // layoutInflater — системный сервис Android,
        // который используется для создания View из XML-описания макета.
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // метод компонента Activity, устанавливающий корневое представление (root view)
        // для актуальной Activity.
        // Метод определяет, что будет отображаться на экране.
        // .root — св-во, возвращающее самый главный View-элемент из XML макета
        // в данном случае — ConstraintLayout
        setContentView(binding.root)

        setupToolbar()
        setupLoginButton()
        setupPasswordToggle()
    }

    private fun setupPasswordToggle() {
        val mainTitleColor = ContextCompat.getColor(this, R.color.main_title_color)
        val subtitleColor = ContextCompat.getColor(this, R.color.subtitle_color)

        binding.passwordInput.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordInput.endIconDrawable?.setColorFilter(subtitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                binding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordInput.endIconDrawable?.setColorFilter(mainTitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
            }
            // Move cursor to the end of the text
            binding.passwordEditText.setSelection(binding.passwordEditText.text?.length ?: 0)
        }
    }

    private fun setupToolbar() {
        // Обработка кнопки "назад" в тулбаре
        // .setNavigationOnClickListener — метод, устанавливающий слушатель нажатия на кнопку навигации
        // кнопка Навигации устанавливается в AppBar
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val login = binding.loginEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(login, password)) {
                performLogin(login, password)
            }
        }
    }

    private fun validateInput(login: String, password: String): Boolean {
        var isValid = true

        // Валидация логина
        if (login.isBlank()) {
            binding.loginInput.error = "Введите логин"
            isValid = false
        } else {
            binding.loginInput.error = null
        }

        // Валидация пароля
        if (password.isBlank()) {
            binding.passwordInput.error = "Введите пароль"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInput.error = "Пароль должен содержать минимум 6 символов"
            isValid = false
        } else {
            binding.passwordInput.error = null
        }

        return isValid
    }

    private fun performLogin(login: String, password: String) {
        // TODO: Реализовать вызов API для входа
        showLoadingState(true)

        // Пример успешного входа
        if (login == "test" && password == "123456") {
            navigateToMainActivity()
        } else {
            showError("Неверные учетные данные")
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.loginButton.isEnabled = !isLoading
        binding.loginButton.text = if (isLoading) "Загрузка..." else "Войти"
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.error_background))
            // White color
            .setTextColor(0xFFFFFFFF.toInt())
            .show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}