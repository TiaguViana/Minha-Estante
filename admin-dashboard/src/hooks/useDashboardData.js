// Arquivo: src/hooks/useDashboardData.js
import { useState, useEffect } from 'react';
import {
  collection,
  collectionGroup,
  getDocs,
  doc,
  updateDoc,
  query,
  orderBy,
  limit,
  startAfter,
  getCountFromServer,
  where,
} from 'firebase/firestore';
import { db } from '../services/firebaseConfig';

const PAGE_SIZE = 10;
const LIMITE_BUSCA = 50;

export function useDashboardData() {
  const [usuarios, setUsuarios] = useState([]);

  const [totalUsuarios, setTotalUsuarios] = useState(0);
  const [totalLivros, setTotalLivros] = useState(0);
  const [totalEstantesSecretas, setTotalEstantesSecretas] = useState(0);

  const [totalRegistrosTabela, setTotalRegistrosTabela] = useState(0);

  const [currentPage, setCurrentPage] = useState(1);
  const [cursores, setCursores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');

  const [termoBusca, setTermoBusca] = useState('');
  const [statusFiltro, setStatusFiltro] = useState(null);
  const [ordenacao, setOrdenacao] = useState({ campo: 'cadastro', direcao: 'desc' });

  const buscando = termoBusca.trim().length > 0;
  const totalPages = buscando ? 1 : (Math.ceil(totalRegistrosTabela / PAGE_SIZE) || 1);

  useEffect(() => {
    buscarMetricas();
  }, []);

  useEffect(() => {
    setCurrentPage(1);
    setCursores([]);
    buscarPagina(1);
    if (!buscando) buscarContagemFiltrada();
  }, [termoBusca, statusFiltro, ordenacao]);

  useEffect(() => {
    if (!buscando) buscarPagina(currentPage);
  }, [currentPage]);

  async function buscarMetricas() {
    try {
      const snapUsuarios = await getCountFromServer(collection(db, 'usuarios'));
      setTotalUsuarios(snapUsuarios.data().count);

      const snapLivros = await getCountFromServer(collectionGroup(db, 'livros'));
      setTotalLivros(snapLivros.data().count);

      const snapSecretas = await getCountFromServer(
        query(collectionGroup(db, 'livros'), where('section', '==', 'ESTANTE_SECRETA'))
      );
      setTotalEstantesSecretas(snapSecretas.data().count);
    } catch (e) {
      console.warn('Erro ao buscar métricas:', e);
    }
  }

  // CORRIGIDO: consulta a contagem real no Firestore, em vez de depender
  // do estado `totalUsuarios` (que pode ainda não ter chegado quando esta
  // função roda, causando o bug do "Nenhum registro" incorreto).
  async function buscarContagemFiltrada() {
    try {
      const clausulas = statusFiltro ? [where('status', '==', statusFiltro)] : [];
      const snap = await getCountFromServer(query(collection(db, 'usuarios'), ...clausulas));
      setTotalRegistrosTabela(snap.data().count);
    } catch (e) {
      console.warn('Erro ao contar usuários filtrados:', e);
    }
  }

  async function buscarPagina(pagina) {
    setLoading(true);
    setErro('');
    try {
      const termo = termoBusca.trim();
      const emBusca = termo.length > 0;

      const campoOrdenacao = emBusca ? 'nome' : ordenacao.campo;
      const direcaoOrdenacao = emBusca ? 'asc' : ordenacao.direcao;

      const clausulas = [];

      if (emBusca) {
        clausulas.push(where('nome', '>=', termo));
        clausulas.push(where('nome', '<=', termo + '\uf8ff'));
      }

      if (statusFiltro) {
        clausulas.push(where('status', '==', statusFiltro));
      }

      clausulas.push(orderBy(campoOrdenacao, direcaoOrdenacao));

      if (emBusca) {
        clausulas.push(limit(LIMITE_BUSCA));
      } else {
        if (pagina > 1) {
          const cursorAnterior = cursores[pagina - 2];
          if (cursorAnterior) clausulas.push(startAfter(cursorAnterior));
        }
        clausulas.push(limit(PAGE_SIZE));
      }

      const q = query(collection(db, 'usuarios'), ...clausulas);
      const snap = await getDocs(q);
      const docs = snap.docs;

      if (!emBusca && docs.length > 0) {
        const novosCursores = [...cursores];
        novosCursores[pagina - 1] = docs[docs.length - 1];
        setCursores(novosCursores);
      }

      const lista = await Promise.all(docs.map(async d => {
        const data = d.data();

        const snapLivros = await getCountFromServer(
          collection(db, 'usuarios', d.id, 'livros')
        );
        const snapSecretas = await getCountFromServer(
          query(
            collection(db, 'usuarios', d.id, 'livros'),
            where('section', '==', 'ESTANTE_SECRETA')
          )
        );

        return {
          id: d.id,
          name: data.nome || data.email || 'Sem nome',
          status: data.status || 'ativo',
          cadastro: formatarData(data.cadastro),
          livros: String(snapLivros.data().count),
          estantes: String(snapSecretas.data().count),
        };
      }));

      setUsuarios(lista);


      if (emBusca) {
        setTotalRegistrosTabela(lista.length);
      }
    } catch (e) {
      console.warn('Erro ao buscar usuários:', e);
      setErro('Não foi possível carregar os usuários.');
    } finally {
      setLoading(false);
    }
  }

  async function desativarUsuario(id) {
    try {
      await updateDoc(doc(db, 'usuarios', id), { status: 'inativo' });
      setUsuarios(prev =>
        prev.map(u => u.id === id ? { ...u, status: 'inativo' } : u)
      );
    } catch (e) {
      console.warn('Erro ao desativar usuário:', e);
    }
  }

  async function reativarUsuario(id) {
    try {
      await updateDoc(doc(db, 'usuarios', id), { status: 'ativo' });
      setUsuarios(prev =>
        prev.map(u => u.id === id ? { ...u, status: 'ativo' } : u)
      );
    } catch (e) {
      console.warn('Erro ao reativar usuário:', e);
    }
  }

  function alternarOrdenacao(campo) {
    if (buscando) return;
    setOrdenacao(atual => ({
      campo,
      direcao: atual.campo === campo && atual.direcao === 'asc' ? 'desc' : 'asc',
    }));
  }

  function proximaPagina() {
    if (currentPage < totalPages) setCurrentPage(p => p + 1);
  }

  function paginaAnterior() {
    if (currentPage > 1) setCurrentPage(p => p - 1);
  }

  return {
    usuarios,
    totalUsuarios,
    totalLivros,
    totalEstantesSecretas,
    totalRegistrosTabela,
    currentPage,
    totalPages,
    loading,
    erro,
    proximaPagina,
    paginaAnterior,
    desativarUsuario,
    reativarUsuario,
    termoBusca,
    setTermoBusca,
    statusFiltro,
    setStatusFiltro,
    ordenacao,
    alternarOrdenacao,
    buscando,
  };
}

function formatarData(timestamp) {
  if (!timestamp) return '—';
  try {
    const date = timestamp.toDate();
    return date.toLocaleDateString('pt-BR');
  } catch {
    return '—';
  }
}