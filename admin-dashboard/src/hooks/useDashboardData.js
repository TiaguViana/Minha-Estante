// Arquivo: src/hooks/useDashboardData.js
import { useState, useEffect } from 'react';
import {
  collection,
  collectionGroup,
  getDocs,
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

  const [currentPage, setCurrentPage] = useState(1);
  const [cursores, setCursores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');

  const [termoBusca, setTermoBusca] = useState('');
  const [ordenacao, setOrdenacao] = useState({ campo: 'cadastro', direcao: 'desc' });

  const [exportando, setExportando] = useState(false);
  const [erroExport, setErroExport] = useState('');

  const buscando = termoBusca.trim().length > 0;

  // Sem filtro de status, o total exibido é sempre o total real de usuários
  // (ou, durante busca, a quantidade de resultados encontrados). Como isso
  // é lido direto do state, não existe mais o risco de "congelar" um valor
  // desatualizado (bug que tive antes com a contagem filtrada).
  const totalRegistros = buscando ? usuarios.length : totalUsuarios;
  const totalPages = buscando ? 1 : (Math.ceil(totalUsuarios / PAGE_SIZE) || 1);

  useEffect(() => {
    buscarMetricas();
  }, []);

  useEffect(() => {
    setCurrentPage(1);
    setCursores([]);
    buscarPagina(1);
  }, [termoBusca, ordenacao]);

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

  async function buscarPagina(pagina) {
    setLoading(true);
    setErro('');
    try {
      const termo = termoBusca.trim();
      const emBusca = termo.length > 0;

      // Regra do Firestore: filtro de intervalo (busca por prefixo) exige
      // que o orderBy comece pelo mesmo campo — então durante a busca a
      // ordenação fica travada em 'nome'.
      const campoOrdenacao = emBusca ? 'nome' : ordenacao.campo;
      const direcaoOrdenacao = emBusca ? 'asc' : ordenacao.direcao;

      const clausulas = [];

      if (emBusca) {
        clausulas.push(where('nome', '>=', termo));
        clausulas.push(where('nome', '<=', termo + '\uf8ff'));
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
          email: data.email || '',
          status: data.status || 'ativo',
          cadastro: formatarData(data.cadastro),
          livros: String(snapLivros.data().count),
          estantes: String(snapSecretas.data().count),
        };
      }));

      setUsuarios(lista);
    } catch (e) {
      console.warn('Erro ao buscar usuários:', e);
      setErro('Não foi possível carregar os usuários.');
    } finally {
      setLoading(false);
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

  // Exporta TODOS os usuários (não só a página atual) como CSV.
  // Só campos básicos, sem contagem de livros por usuário, pra manter
  // rápido mesmo com muitos usuários (evita 1 consulta extra por linha).
  async function exportarCSV() {
    setExportando(true);
    setErroExport('');
    try {
      const q = query(collection(db, 'usuarios'), orderBy('cadastro', 'desc'));
      const snap = await getDocs(q);

      const linhas = snap.docs.map(d => {
        const data = d.data();
        return {
          nome: data.nome || data.email || 'Sem nome',
          email: data.email || '',
          status: data.status || 'ativo',
          cadastro: formatarData(data.cadastro),
        };
      });

      baixarComoCSV(linhas);
    } catch (e) {
      console.warn('Erro ao exportar CSV:', e);
      setErroExport('Não foi possível exportar os usuários agora.');
    } finally {
      setExportando(false);
    }
  }

  return {
    usuarios,
    totalUsuarios,
    totalLivros,
    totalEstantesSecretas,
    totalRegistros,
    currentPage,
    totalPages,
    loading,
    erro,
    proximaPagina,
    paginaAnterior,
    termoBusca,
    setTermoBusca,
    ordenacao,
    alternarOrdenacao,
    buscando,
    exportarCSV,
    exportando,
    erroExport,
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

function escaparCampoCSV(valor) {
  const texto = String(valor ?? '');
  // Se tiver vírgula, aspas ou quebra de linha, precisa envolver em aspas
  if (texto.includes(',') || texto.includes('"') || texto.includes('\n')) {
    return '"' + texto.replace(/"/g, '""') + '"';
  }
  return texto;
}

function baixarComoCSV(linhas) {
  if (typeof document === 'undefined') return; // segurança, caso rode fora do navegador

  const cabecalho = ['Nome', 'E-mail', 'Status', 'Cadastro'];
  const corpo = linhas.map(l => [l.nome, l.email, l.status, l.cadastro].map(escaparCampoCSV).join(','));
  const csv = [cabecalho.join(','), ...corpo].join('\n');

  // \uFEFF (BOM) garante que o Excel reconheça acentuação corretamente
  const blob = new Blob(['\uFEFF' + csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);

  const link = document.createElement('a');
  link.href = url;
  link.download = 'usuarios_minha_estante.csv';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}