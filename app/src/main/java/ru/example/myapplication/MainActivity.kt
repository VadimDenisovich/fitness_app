package ru.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var hasActivities = false // Временный флаг для демонстрации
    private lateinit var adapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        setupViewPager()
        setupToolbar()

        // Кнопка начала активности
        binding.btnStartActivity.setOnClickListener {
//            startActivity(Intent(this, TrackingActivity::class.java))
        }
    }

    private fun setupViewPager() {
        // Создаём собственный адаптер
        adapter = MainPagerAdapter(this)
        // Присваиваем viewPager свой адаптер
        binding.viewPager.adapter = adapter
    }

    private fun setupToolbar() {
        // Настройка TabLayout с вкладками
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Моя"
                1 -> "Пользователей"
             else -> ""
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> checkActivities() 
                    1 -> {
                        showActivitiesList()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
               
            }
        })

    }

    private fun checkActivities() {
        // TODO: Заменить на реальную проверку из БД
        hasActivities = false

        if (hasActivities) {
            showActivitiesList()
        } else {
            showEmptyState()
        }
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.isVisible = true
        binding.viewPager.isVisible = false
    }

    private fun showActivitiesList() {
        binding.emptyStateLayout.isVisible = false
        binding.viewPager.isVisible = true
    }

    fun setViewPagerItem(position: Int) {
        binding.viewPager.setCurrentItem(position, true)
    }

}
