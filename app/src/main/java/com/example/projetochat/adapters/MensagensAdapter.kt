package com.example.projetochat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.projetochat.databinding.ItemMensagensDestinatarioBinding
import com.example.projetochat.databinding.ItemMensagensRemetenteBinding
import com.example.projetochat.model.Mensagem
import com.example.projetochat.utils.Constantes
import com.google.firebase.auth.FirebaseAuth

class MensagensAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var listaMensagens = emptyList<Mensagem>()
    @SuppressLint("NotifyDataSetChanged")
    fun adicionarLista(lista: List<Mensagem> ){
        if( lista.isNotEmpty() ) {
            listaMensagens = lista
            notifyDataSetChanged()
        }
    }

    class MensagensRemetenteViewHolder(
        private val binding: ItemMensagensRemetenteBinding
    ) : ViewHolder( binding.root ){

        fun bind( mensagem: Mensagem ){
            binding.textMensagemRemetente.text = mensagem.mensagem
        }

        companion object {
            fun inflarLayout( parent: ViewGroup ) : MensagensRemetenteViewHolder {

                val inflater = LayoutInflater.from( parent.context )
                val itemView = ItemMensagensRemetenteBinding.inflate(
                    inflater, parent, false
                )
                return MensagensRemetenteViewHolder( itemView )
            }
        }
    }

    class MensagensDestinatarioViewHolder(
        private val binding: ItemMensagensDestinatarioBinding
    ) : ViewHolder( binding.root ){

        fun bind( mensagem: Mensagem ){
            binding.textMensagemDestinatario.text = mensagem.mensagem
        }

        companion object {

            fun inflarLayout( parent: ViewGroup ) : MensagensDestinatarioViewHolder {

                val inflater = LayoutInflater.from( parent.context )
                val itemView = ItemMensagensDestinatarioBinding.inflate(
                    inflater, parent, false
                )
                return MensagensDestinatarioViewHolder( itemView )
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        val mensagem = listaMensagens[position]
        val idUsuarioLogado = FirebaseAuth.getInstance().currentUser?.uid.toString()

        return if( idUsuarioLogado == mensagem.idUsuario ){
            Constantes.TIPO_REMETENTE
        }else {
            Constantes.TIPO_DESTINATARIO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if ( viewType == Constantes.TIPO_REMETENTE){
            return MensagensRemetenteViewHolder.inflarLayout( parent )
        }
        return MensagensDestinatarioViewHolder.inflarLayout( parent )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mensagem = listaMensagens[position]
        when( holder ){
            is MensagensRemetenteViewHolder -> holder.bind(mensagem)
            is MensagensDestinatarioViewHolder -> holder.bind(mensagem)
        }
    }

    override fun getItemCount(): Int {
        return listaMensagens.size
    }
}