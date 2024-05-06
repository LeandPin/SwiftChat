package com.example.projetochat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.projetochat.databinding.ItemContatosBinding
import com.example.projetochat.databinding.ItemConversasBinding
import com.example.projetochat.model.Conversa
import com.example.projetochat.model.Usuario
import com.squareup.picasso.Picasso

class ConversasAdapter(
    private val onClick: (Conversa) -> Unit
) : Adapter<ConversasAdapter.ConversasViewHolder>() {

    private var listaConversas = emptyList<Conversa>()
    @SuppressLint("NotifyDataSetChanged")
    fun adicionarLista( lista: List<Conversa> ){
        listaConversas = lista
        notifyDataSetChanged()
    }

    inner class ConversasViewHolder(
        private val binding: ItemConversasBinding
    ) : RecyclerView.ViewHolder( binding.root ){

        fun bind( conversa: Conversa ){

            binding.textConversaNome.text = conversa.nome
            binding.textConversaMensagem.text = conversa.ultimaMensagem
            Picasso.get()
                .load( conversa.foto )
                .into( binding.imageConversaFoto )

            //Evento de clique
            binding.clItemConversa.setOnClickListener {
                onClick( conversa )
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversasAdapter.ConversasViewHolder {
        val inflater = LayoutInflater.from( parent.context )
        val itemView = ItemConversasBinding.inflate(
            inflater, parent, false
        )
        return ConversasViewHolder( itemView )
    }

    override fun onBindViewHolder(holder: ConversasAdapter.ConversasViewHolder, position: Int) {
        val conversa = listaConversas[position]
        holder.bind( conversa )
    }

    override fun getItemCount(): Int {
        return listaConversas.size
    }
}