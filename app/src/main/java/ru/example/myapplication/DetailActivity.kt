package ru.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

// Activity — компонент, представляющий собой отдельный экран с пользовательским
// интерфейсом.

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            // Возвращаемся в прошлый экран
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Убераем аппБар
        supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        // Возращаем аппБар, если экран не используется
        supportActionBar?.show()
    }
}
