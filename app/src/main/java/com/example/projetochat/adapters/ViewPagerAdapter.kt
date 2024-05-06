package com.example.projetochat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projetochat.fragments.ContatosFragment
import com.example.projetochat.fragments.ConversasFragment

class ViewPagerAdapter(
    private val abas: List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return abas.size //listOf(0 -> "Conversas", 1 -> "Contatos")
    }

    override fun createFragment(position: Int): Fragment {
        when( position ){
            1 -> return ContatosFragment()

        }
        return ConversasFragment()
    }
}