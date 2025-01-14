package com.example.projetochat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetochat.activities.MensagensActivity
import com.example.projetochat.adapters.ContatosAdapter
import com.example.projetochat.databinding.FragmentContatosBinding
import com.example.projetochat.model.Usuario
import com.example.projetochat.utils.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ContatosFragment : Fragment() {

    private lateinit var binding: FragmentContatosBinding
    private lateinit var eventoSnapshot: ListenerRegistration
    private lateinit var contatosAdapter: ContatosAdapter

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContatosBinding.inflate(
            inflater, container, false
        )

        contatosAdapter = ContatosAdapter { usuario ->
            val intent = Intent(context, MensagensActivity::class.java)
            intent.putExtra( "dadosDestinatario", usuario)
            //intent.putExtra("origem", Constantes.ORIGEM_CONTATO)
            startActivity( intent )
        }
        binding.rvContatos.adapter = contatosAdapter
        binding.rvContatos.layoutManager = LinearLayoutManager(context)
        binding.rvContatos.addItemDecoration(
            DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
        )

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adicionarListenerContatos()
    }

    private fun adicionarListenerContatos() {

        eventoSnapshot = firestore
            .collection( Constantes.USUARIOS )
            .addSnapshotListener { querySnapshot, erro ->

                val listaContatos = mutableListOf<Usuario>()
                val documentos = querySnapshot?.documents

                documentos?.forEach{ documentSnapshot ->

                    val idUsuarioLogado = firebaseAuth.currentUser?.uid
                    val usuario = documentSnapshot.toObject( Usuario::class.java )

                    if ( usuario != null && idUsuarioLogado != null){
                        if( idUsuarioLogado != usuario.id ){
                            listaContatos.add( usuario )
                        }
                    }
                }

                //Lista de contatos
                if ( listaContatos.isNotEmpty() ){
                    contatosAdapter.adicionarLista( listaContatos )
                }

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventoSnapshot.remove()
    }
}