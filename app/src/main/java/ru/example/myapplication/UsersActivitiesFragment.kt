package ru.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView

class UsersActivitiesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_users_activities, container, false)

        val cardView1: CardView = view.findViewById(R.id.card)
        val cardView2: CardView = view.findViewById(R.id.card2)
        val cardView3: CardView = view.findViewById(R.id.card3)

        val clickListener = View.OnClickListener {
            // Переход на экран детальной информации
            startActivity(Intent(context, DetailActivity::class.java))
        }

        cardView1.setOnClickListener(clickListener)
        cardView2.setOnClickListener(clickListener)
        cardView3.setOnClickListener(clickListener)

        return view
    }
}