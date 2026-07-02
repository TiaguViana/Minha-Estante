// Arquivo: src/components/UserTable.js
import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Image } from 'react-native';
import ConfirmModal from './ConfirmModal';
import { useTheme } from '../context/ThemeContext';

const OPCOES_STATUS = [
  { valor: null, label: 'Todos' },
  { valor: 'ativo', label: 'Ativos' },
  { valor: 'inativo', label: 'Inativos' },
];

export default function UserTable({
  data = [],
  currentPage = 1,
  totalPages = 1,
  totalRegistros = 0,
  pageSize = 10,
  onPageChange,
  onDesativar,
  onReativar,
  trashIcon,
  statusFiltro,
  onStatusFiltroChange,
  ordenacao,
  onOrdenar,
  ordenacaoDesabilitada = false,
}) {
  const { colors } = useTheme();
  const styles = getStyles(colors);

  const [usuarioSelecionado, setUsuarioSelecionado] = useState(null);

  function abrirModal(usuario) {
    setUsuarioSelecionado(usuario);
  }

  function fecharModal() {
    setUsuarioSelecionado(null);
  }

  function confirmarAcao() {
    if (!usuarioSelecionado) return;
    const isAtivo = usuarioSelecionado.status === 'ativo';
    if (isAtivo && onDesativar) onDesativar(usuarioSelecionado.id);
    if (!isAtivo && onReativar) onReativar(usuarioSelecionado.id);
    fecharModal();
  }

  const inicioRegistro = totalRegistros > 0 ? (currentPage - 1) * pageSize + 1 : 0;
  const fimRegistro = Math.min(currentPage * pageSize, totalRegistros);
  const nomeUsuarioSelecionado = usuarioSelecionado ? usuarioSelecionado.name : '';
  const acaoEhDesativar = usuarioSelecionado?.status === 'ativo';

  function IconeOrdenacao({ campo }) {
    if (!ordenacao || ordenacao.campo !== campo) return null;
    return <Text style={styles.iconeOrdenacao}>{ordenacao.direcao === 'asc' ? ' ▲' : ' ▼'}</Text>;
  }

  return (
    <View style={styles.card}>

      <View style={styles.tituloRow}>
        <Text style={styles.titulo}>Lista de Usuários</Text>

        {/* Filtro de status */}
        <View style={styles.filtroStatusGrupo}>
          {OPCOES_STATUS.map((opcao) => (
            <TouchableOpacity
              key={opcao.label}
              style={[
                styles.filtroStatusBotao,
                statusFiltro === opcao.valor && styles.filtroStatusBotaoAtivo,
              ]}
              onPress={() => onStatusFiltroChange && onStatusFiltroChange(opcao.valor)}
              accessibilityRole="button"
              accessibilityLabel={`Filtrar por ${opcao.label}`}
              accessibilityState={{ selected: statusFiltro === opcao.valor }}
            >
              <Text
                style={[
                  styles.filtroStatusTexto,
                  statusFiltro === opcao.valor && styles.filtroStatusTextoAtivo,
                ]}
              >
                {opcao.label}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>

      <View style={styles.headerRow}>
        <TouchableOpacity
          style={[styles.colUsuario]}
          onPress={() => onOrdenar && onOrdenar('nome')}
          disabled={ordenacaoDesabilitada}
        >
          <Text style={styles.headerCell}>Usuário<IconeOrdenacao campo="nome" /></Text>
        </TouchableOpacity>
        <Text style={[styles.headerCell, styles.colStatus]}>Status</Text>
        <TouchableOpacity
          style={[styles.colCadastro]}
          onPress={() => onOrdenar && onOrdenar('cadastro')}
          disabled={ordenacaoDesabilitada}
        >
          <Text style={styles.headerCell}>Cadastro<IconeOrdenacao campo="cadastro" /></Text>
        </TouchableOpacity>
        <Text style={[styles.headerCell, styles.colLivros]}>Livros</Text>
        <Text style={[styles.headerCell, styles.colEstantes]}>Estantes Secretas</Text>
        <Text style={[styles.headerCell, styles.colAcoes]}>Ações</Text>
      </View>

      {data.length === 0 ? (
        <View style={styles.vazioContainer}>
          <Text style={styles.vazioTexto}>Nenhum usuário encontrado.</Text>
        </View>
      ) : (
        data.map(function (usuario, index) {
          var isAtivo = usuario.status === 'ativo';

          return (
            <View
              key={usuario.id || index}
              style={[
                styles.dataRow,
                index < data.length - 1 && styles.dataRowBorder,
              ]}
            >
              <Text style={[styles.dataCell, styles.colUsuario]}>{usuario.name}</Text>

              <View style={[styles.dataCell, styles.colStatus, styles.statusCell]}>
                <View
                  style={[
                    styles.statusDot,
                    { backgroundColor: isAtivo ? colors.tableDotAtivo : colors.tableDotInativo },
                  ]}
                />
                <Text style={styles.statusText}>
                  {isAtivo ? 'Ativo' : 'Inativo'}
                </Text>
              </View>

              <Text style={[styles.dataCell, styles.colCadastro]}>{usuario.cadastro}</Text>
              <Text style={[styles.dataCell, styles.colLivros]}>{usuario.livros}</Text>
              <Text style={[styles.dataCell, styles.colEstantes]}>{usuario.estantes}</Text>

              <View style={[styles.dataCell, styles.colAcoes]}>
                <TouchableOpacity
                  onPress={function () { abrirModal(usuario); }}
                  style={styles.acaoButton}
                  accessibilityLabel={isAtivo ? 'Desativar ' + usuario.name : 'Reativar ' + usuario.name}
                >
                  {isAtivo ? (
                    trashIcon ? (
                      <Image source={trashIcon} style={styles.trashIcon} resizeMode="contain" />
                    ) : (
                      <Text style={styles.trashEmoji}>🗑️</Text>
                    )
                  ) : (
                    <Text style={styles.reativarTexto}>↩️ Reativar</Text>
                  )}
                </TouchableOpacity>
              </View>
            </View>
          );
        })
      )}

      {/* Paginação —  */}
      <View style={styles.paginacaoRow}>
        <Text style={styles.paginacaoInfo}>
          {totalRegistros > 0
            ? inicioRegistro + ' a ' + fimRegistro + ' de ' + totalRegistros + ' registros'
            : 'Nenhum registro'}
        </Text>

        <View style={styles.paginacaoControles}>
          <TouchableOpacity
            style={[
              styles.paginacaoBotaoCirculo,
              currentPage <= 1 && styles.paginacaoBotaoDesabilitado,
            ]}
            onPress={function () { if (onPageChange) onPageChange(currentPage - 1); }}
            disabled={currentPage <= 1}
            accessibilityLabel="Página anterior"
          >
            <Text style={styles.paginacaoSeta}>{'<'}</Text>
          </TouchableOpacity>

          <Text style={styles.paginacaoTexto}>
            {'Página '}
            <Text style={styles.paginacaoDestaque}>
              {String(currentPage).padStart(2, '0')}
            </Text>
            {'  de  '}
            <Text style={styles.paginacaoDestaque}>
              {String(totalPages).padStart(2, '0')}
            </Text>
          </Text>

          <TouchableOpacity
            style={[
              styles.paginacaoBotaoCirculo,
              currentPage >= totalPages && styles.paginacaoBotaoDesabilitado,
            ]}
            onPress={function () { if (onPageChange) onPageChange(currentPage + 1); }}
            disabled={currentPage >= totalPages}
            accessibilityLabel="Próxima página"
          >
            <Text style={styles.paginacaoSeta}>{'>'}</Text>
          </TouchableOpacity>
        </View>
      </View>

      <ConfirmModal
        visible={usuarioSelecionado !== null}
        userName={nomeUsuarioSelecionado}
        mensagem={acaoEhDesativar ? undefined : `Deseja reativar ${nomeUsuarioSelecionado}?`}
        onConfirmar={confirmarAcao}
        onCancelar={fecharModal}
      />
    </View>
  );
}

function getStyles(colors) {
  return StyleSheet.create({
    card: {
      borderWidth: 1,
      borderColor: colors.tableCardBorder,
      borderRadius: 16,
      overflow: 'hidden',
      backgroundColor: colors.tableCardBg,
    },
    tituloRow: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      paddingHorizontal: 24,
      paddingVertical: 14,
    },
    titulo: {
      fontSize: 18,
      color: colors.tableHeaderText,
      fontWeight: '500',
    },
    filtroStatusGrupo: {
      flexDirection: 'row',
      gap: 8,
    },
    filtroStatusBotao: {
      paddingVertical: 4,
      paddingHorizontal: 12,
      borderRadius: 16,
      borderWidth: 1,
      borderColor: colors.tableHeaderText,
    },
    filtroStatusBotaoAtivo: {
      backgroundColor: colors.tableHeaderText,
    },
    filtroStatusTexto: {
      fontSize: 13,
      color: colors.tableHeaderText,
    },
    filtroStatusTextoAtivo: {
      color: colors.tableCardBg,
    },
    headerRow: {
      flexDirection: 'row',
      backgroundColor: colors.tableHeaderBg,
      paddingVertical: 10,
      paddingHorizontal: 24,
    },
    headerCell: {
      fontSize: 13,
      color: colors.tableHeaderText,
      fontWeight: '500',
    },
    iconeOrdenacao: {
      fontSize: 11,
    },
    dataRow: {
      flexDirection: 'row',
      alignItems: 'center',
      paddingVertical: 14,
      paddingHorizontal: 24,
      backgroundColor: colors.tableRowBg,
    },
    dataRowBorder: {
      borderBottomWidth: 1,
      borderBottomColor: colors.tableRowSeparator,
    },
    vazioContainer: {
      paddingVertical: 32,
      alignItems: 'center',
      backgroundColor: colors.tableRowBg,
    },
    vazioTexto: {
      fontSize: 13,
      color: colors.tablePaginacaoText,
    },
    dataCell: {
      fontSize: 13,
      color: colors.tableRowText,
    },
    statusCell: {
      flexDirection: 'row',
      alignItems: 'center',
      gap: 6,
    },
    statusDot: {
      width: 8,
      height: 8,
      borderRadius: 4,
    },
    statusText: {
      fontSize: 13,
      color: colors.tableRowText,
    },
    acaoButton: {
      padding: 4,
    },
    trashIcon: {
      width: 18,
      height: 18,
      tintColor: '#6B8C98',
    },
    trashEmoji: {
      fontSize: 16,
    },
    reativarTexto: {
      fontSize: 12,
      fontWeight: '600',
      color: colors.tableDotAtivo,
    },
    colUsuario: { flex: 2.5 },
    colStatus: { flex: 1.5 },
    colCadastro: { flex: 1.5 },
    colLivros: { flex: 1 },
    colEstantes: { flex: 2 },
    colAcoes: { flex: 1, alignItems: 'center' },

    paginacaoRow: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingHorizontal: 24,
      paddingVertical: 14,
      borderTopWidth: 1,
      borderTopColor: colors.tableRowSeparator,
      backgroundColor: colors.tableRowBg,
    },
    paginacaoInfo: {
      fontSize: 12,
      color: colors.tablePaginacaoText,
    },
    paginacaoControles: {
      flexDirection: 'row',
      alignItems: 'center',
      gap: 12,
    },
    paginacaoBotaoCirculo: {
      width: 28,
      height: 28,
      borderRadius: 14,
      borderWidth: 1,
      borderColor: colors.tablePaginacaoBorder,
      justifyContent: 'center',
      alignItems: 'center',
    },
    paginacaoBotaoDesabilitado: {
      opacity: 0.3,
    },
    paginacaoSeta: {
      fontSize: 13,
      color: colors.tablePaginacaoText,
      fontWeight: '600',
    },
    paginacaoTexto: {
      fontSize: 13,
      color: colors.tablePaginacaoText,
    },
    paginacaoDestaque: {
      fontWeight: '700',
    },
  });
}