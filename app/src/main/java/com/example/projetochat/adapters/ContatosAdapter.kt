package com.example.projetochat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetochat.databinding.ItemContatosBinding
import com.example.projetochat.model.Usuario
import com.squareup.picasso.Picasso

class ContatosAdapter(
    private val onClick: (Usuario) -> Unit
) : RecyclerView.Adapter<ContatosAdapter.ContatosViewHolder>() {

    private var listaContatos = emptyList<Usuario>()
    @SuppressLint("NotifyDataSetChanged")
    fun adicionarLista(lista: List<Usuario> ){
        if( lista.isNotEmpty() ) {
            listaContatos = lista
            notifyDataSetChanged()
        }
    }

    inner class ContatosViewHolder(
        private val binding: ItemContatosBinding
    ) : RecyclerView.ViewHolder( binding.root ){

        fun bind( usuario: Usuario ){

            binding.textContatoNome.text = usuario.nome
            if ( usuario.foto.isNotEmpty() ) {
                Picasso.get()
                    .load(usuario.foto)
                    .into(binding.imageContatoFoto)

                //Evento de clique
                binding.clItemContato.setOnClickListener {
                    onClick(usuario)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatosViewHolder {

        val inflater = LayoutInflater.from( parent.context )
        val itemView = ItemContatosBinding.inflate(
            inflater, parent, false
        )
        return ContatosViewHolder( itemView )

    }

    override fun onBindViewHolder(holder: ContatosViewHolder, position: Int) {
        val usuario = listaContatos[position]
        holder.bind( usuario )
    }

    override fun getItemCount(): Int {
        return listaContatos.size
    }

}