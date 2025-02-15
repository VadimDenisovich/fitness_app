package ru.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.example.myapplication.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обработка кнопки "назад"
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Обработка клика по кнопке регистрации
        binding.registerButton.setOnClickListener {
            validateAndRegister()
        }

        setupPasswordToggle()
        setupConfirmPasswordToggle()

        // Делаем ссылки кликабельными
        setupPolicyLinks()
    }

    private fun setupPasswordToggle() {
        val mainTitleColor = ContextCompat.getColor(this, R.color.main_title_color)
        val subtitleColor = ContextCompat.getColor(this, R.color.subtitle_color)

        binding.passwordInput.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordInput.endIconDrawable?.setColorFilter(mainTitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                binding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordInput.endIconDrawable?.setColorFilter(subtitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
            }
            
            binding.passwordEditText.setSelection(binding.passwordEditText.text?.length ?: 0)
        }
    }

    private fun setupConfirmPasswordToggle() {
        val mainTitleColor = ContextCompat.getColor(this, R.color.main_title_color)
        val subtitleColor = ContextCompat.getColor(this, R.color.subtitle_color)

        binding.confirmPasswordInput.setEndIconOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                binding.confirmPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.confirmPasswordInput.endIconDrawable?.setColorFilter(mainTitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                binding.confirmPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.confirmPasswordInput.endIconDrawable?.setColorFilter(subtitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
            }
            // Move cursor to the end of the text
            binding.confirmPasswordEditText.setSelection(binding.passwordEditText.text?.length ?: 0)
        }
    }

    private fun validateAndRegister() {
        val login = binding.loginEditText.text.toString()
        val nickname = binding.nicknameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (validateInput(login, nickname, password, confirmPassword)) {
            performRegistration(login, nickname, password)
        }

        // TODO: Реализовать логику регистрации через API
    }

    private fun validateInput(login: String, nickname: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        // Валидация логина
        if (login.isBlank()) {
            binding.loginInput.error = "Введите логин"
            isValid = false
        }

        //Валидация никнейма
        if (nickname.isBlank()) {
            binding.nicknameInput.error = "Введите никнейм"
            isValid = false
        }

        // Валидация пароля
        if (password.isBlank()) {
            binding.passwordInput.error = "Введите пароль"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInput.error = "Пароль должен содержать минимум 6 символов"
            isValid = false
        }

        // Валидация повтора пароля
        if(confirmPassword.isBlank()) {
            binding.confirmPasswordInput.error = "Подтвердите пароль"
            isValid = false
        }

        // Проверка паролей на совпадение
        if (password != confirmPassword) {
            binding.confirmPasswordInput.error = "Пароли не совпадают"
            isValid = false
        }

        // Валидация выбора пола
        if (binding.genderGroup.checkedRadioButtonId == -1) {
            binding.genderError.text = "Выберите пол"
            binding.genderError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.genderError.visibility = View.GONE
        }

        return isValid
    }

    private fun performRegistration(login: String, nickname: String, password: String) {
        // TODO: Реализовать вызов API для регистрации

        showLoadingState(true)

        // Пример для успешного входа
        if (login == "test" && password == "123456") {
            navigateToMainActivity()
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.registerButton.isEnabled = !isLoading
        binding.registerButton.text = if (isLoading) "Загрузка..." else "Войти"
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left  )
    }

    // Установка ссылок для политики конфиденциальности и пользовательского соглашения
    private fun setupPolicyLinks() {
        val policyText = binding.policyText.text.toString()
        val spannable = SpannableStringBuilder(policyText)
        val linkColor = resources.getColor(R.color.primary_button_color, null)

        class CustomClickableSpan(
            private val onClick: View.OnClickListener,
            private val color: Int
        ) : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick.onClick(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }

        // Политика конфиденциальности
        val privacyPolicy = "политикой конфиденциальности"
        val privacyStart = policyText.indexOf(privacyPolicy)
        if (privacyStart != -1) {
            spannable.setSpan(
                CustomClickableSpan(View.OnClickListener {
                    // TODO: Открыть WebView с политикой
                }, linkColor),
                privacyStart,
                privacyStart + privacyPolicy.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            println("Текст 'политика конфиденциальности' не найден")
        }

        // Пользовательское соглашение
        val termsOfService = "пользовательское соглашение"
        val termsStart = policyText.indexOf(termsOfService)
        if (termsStart != -1) {
            spannable.setSpan(
                CustomClickableSpan(View.OnClickListener {
                    // TODO: Открыть WebView с соглашением
                }, linkColor),
                termsStart,
                termsStart + termsOfService.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            println("Текст 'пользовательское соглашение' не найден")
        }

        binding.policyText.apply {
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }
}
