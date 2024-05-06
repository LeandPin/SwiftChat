package com.example.projetochat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projetochat.databinding.ActivityCadastroBinding
import com.example.projetochat.model.Usuario
import com.example.projetochat.utils.exibirMensagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroBinding.inflate( layoutInflater )
    }

    private lateinit var nome: String
    private lateinit var email: String
    private lateinit var senha: String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )
        inicializarToolbar()
        inicializarEventosClique()
    }

    private fun inicializarEventosClique() {
        binding.btnCadastrar.setOnClickListener {
            if( validarCampos() ){
                cadastrarUsuario(nome, email, senha)
            }
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String) {

        firebaseAuth.createUserWithEmailAndPassword(
            email, senha,
        ).addOnCompleteListener { resultado ->
            if( resultado.isSuccessful ){
                //Salvar dados FireStore
                val idUsuario = resultado.result.user?.uid
                if( idUsuario != null){
                    val usuario = Usuario(
                        idUsuario, nome, email
                    )
                    salvarUsuarioFirestore( usuario )
                }
            }
        }.addOnFailureListener{ erro ->
            try {
                throw erro
            }catch ( erroSenhaFraca: FirebaseAuthWeakPasswordException ){
                erroSenhaFraca.printStackTrace()
                exibirMensagem("Senha fraca, digite outra com letras, números e caracteres especiais")
            }catch ( erroUsuarioExistente: FirebaseAuthUserCollisionException){
                erroUsuarioExistente.printStackTrace()
                exibirMensagem("E-mail já pertence a outro usuário")
            }catch ( erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException ){
                erroCredenciaisInvalidas.printStackTrace()
                exibirMensagem("E-mail inválido, digite um outro e-mail")
            }
        }
    }

    private fun salvarUsuarioFirestore(usuario: Usuario) {

        val firestore = FirebaseFirestore.getInstance()
        firestore
            .collection("usuarios")
            .document( usuario.id )
            .set( usuario )
            .addOnSuccessListener {
                exibirMensagem("Sucesso ao fazer seu cadastro")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            }.addOnFailureListener{
                exibirMensagem("Erro ao fazer seu cadastro")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            }
    }

    private fun validarCampos(): Boolean {

        nome = binding.editNome.text.toString()
        email = binding.editEmail.text.toString()
        senha = binding.editSenha.text.toString()

        if( nome.isNotEmpty() ){

            binding.textInputLayoutNome.error = null
            if( email.isNotEmpty() ){

                binding.textInputLayoutEmail.error = null
                if( senha.isNotEmpty() ){
                    binding.textInputLayoutSenha.error = null
                    return true
                }else{
                    binding.textInputLayoutSenha.error = "Preencha sua senha!"
                    return false
                }
            }else{
                binding.textInputLayoutEmail.error = "Preencha seu e-mail!"
                return false
            }
        }else{
            binding.textInputLayoutNome.error = "Preencha seu nome!"
            return false
        }
    }

    private fun inicializarToolbar(){
        val toolbar = binding.includeToolbarPerfil.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Faça o seu cadastro"
            setDisplayHomeAsUpEnabled(true)
        }
    }

}