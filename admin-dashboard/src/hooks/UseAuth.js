// Arquivo: src/hooks/useAuth.js
import { useState, useEffect } from 'react';
import { signInWithEmailAndPassword, signOut, onAuthStateChanged } from 'firebase/auth';
import { doc, getDoc } from 'firebase/firestore';
import { auth, db } from '../services/firebaseConfig';

/**
 * Hook de autenticação com verificação de admin.
 *
 * - user: usuário autenticado E verificado como admin (null se não logado ou não for admin)
 * - loading: true enquanto verifica sessão salva ou consulta o Firestore
 * - erro: mensagem de erro do último login falhado
 * - entrar(email, senha): faz login e verifica se é admin
 * - sair(): faz logout
 */
export function useAuth() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    const cancelarListener = onAuthStateChanged(auth, async (usuarioLogado) => {
      if (usuarioLogado) {
        // Usuário autenticado -- verifica se é admin no Firestore
        const isAdmin = await verificarAdmin(usuarioLogado.uid);
        if (isAdmin) {
          setUser(usuarioLogado);
        } else {
          // Autenticou mas não é admin -- faz logout silencioso
          await signOut(auth);
          setUser(null);
          setErro('Esta conta não tem permissão de acesso ao painel.');
        }
      } else {
        setUser(null);
      }
      setLoading(false);
    });

    return cancelarListener;
  }, []);

  async function entrar(email, senha) {
    setErro('');
    try {
      await signInWithEmailAndPassword(auth, email, senha);
      // O onAuthStateChanged acima já cuida da verificação de admin
      // e do redirecionamento -- não precisa fazer nada aqui além do login.
    } catch (e) {
      setErro(traduzirErroFirebase(e.code));
    }
  }

  async function sair() {
    try {
      await signOut(auth);
    } catch (e) {
      console.warn('Erro ao fazer logout:', e);
    }
  }

  return { user, loading, erro, entrar, sair };
}

// Consulta a collection admins no Firestore e retorna true se o uid existir lá
async function verificarAdmin(uid) {
  try {
    const docRef = doc(db, 'admins', uid);
    const docSnap = await getDoc(docRef);
    return docSnap.exists();
  } catch (e) {
    console.warn('Erro ao verificar admin:', e);
    return false;
  }
}

function traduzirErroFirebase(code) {
  switch (code) {
    case 'auth/invalid-email':
      return 'E-mail inválido.';
    case 'auth/user-not-found':
    case 'auth/wrong-password':
    case 'auth/invalid-credential':
      return 'E-mail ou senha incorretos.';
    case 'auth/too-many-requests':
      return 'Muitas tentativas. Tente novamente mais tarde.';
    case 'auth/network-request-failed':
      return 'Sem conexão com a internet.';
    default:
      return 'Erro ao fazer login. Tente novamente.';
  }
}