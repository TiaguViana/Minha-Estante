// Arquivo: src/components/UserTable.js
import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Image } from 'react-native';
import ConfirmModal from './ConfirmModal';
import { colors, typography } from '../styles';


/**
 * Tabela de usuários do Dashboard admin.
 *
 * @param {Array} data - lista de usuários. Cada item deve ter:
 *   { id, name, status ('ativo'|'inativo'), cadastro, livros, estantes }
 * @param {number} currentPage - página atual (começa em 1)
 * @param {number} totalPages - total de páginas
 * @param {number} totalRegistros - total de registros (ex: 231)
 * @param {number} pageSize - registros por página (ex: 10)
 * @param {function} onPageChange - chamada com o novo número de página
 * @param {function} onDesativar - chamada com o id do usuário a desativar
 * @param {ImageSourcePropType} [trashIcon] - ícone de lixeira. Se não passar,
 *   usa o caractere "🗑️" como fallback.
 */
export default function UserTable({
  data = [],
  currentPage = 1,
  totalPages = 1,
  totalRegistros = 0,
  pageSize = 10,
  onPageChange,
  onDesativar,
  trashIcon,
}) {
  const [usuarioSelecionado, setUsuarioSelecionado] = useState(null);

  function abrirModal(usuario) {
    setUsuarioSelecionado(usuario);
  }

  function fecharModal() {
    setUsuarioSelecionado(null);
  }

  function confirmarDesativacao() {
    if (usuarioSelecionado && onDesativar) {
      onDesativar(usuarioSelecionado.id);
    }
    fecharModal();
  }

  const inicioRegistro = (currentPage - 1) * pageSize + 1;
  const fimRegistro = Math.min(currentPage * pageSize, totalRegistros);

  // Evita o ?. (optional chaining) que pode não ser suportado em configs antigas
  const nomeUsuarioSelecionado = usuarioSelecionado ? usuarioSelecionado.name : '';

  return (
    <View style={styles.card}>
      {/* Título da seção */}
      <Text style={styles.titulo}>Lista de Usuários</Text>

      {/* Header das colunas */}
      <View style={styles.headerRow}>
        <Text style={[styles.headerCell, styles.colUsuario]}>Usuário</Text>
        <Text style={[styles.headerCell, styles.colStatus]}>Status</Text>
        <Text style={[styles.headerCell, styles.colCadastro]}>Cadastro</Text>
        <Text style={[styles.headerCell, styles.colLivros]}>Livros</Text>
        <Text style={[styles.headerCell, styles.colEstantes]}>Estantes Secretas</Text>
        <Text style={[styles.headerCell, styles.colAcoes]}>Ações</Text>
      </View>

      {/* Linhas de dados */}
      {data.map(function(usuario, index) {
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

            {/* Status com bolinha colorida */}
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

            {/* Botão de ação (lixeira) */}
            <View style={[styles.dataCell, styles.colAcoes]}>
              <TouchableOpacity
                onPress={function() { abrirModal(usuario); }}
                style={styles.trashButton}
                accessibilityLabel={'Desativar ' + usuario.name}
              >
                {trashIcon ? (
                  <Image
                    source={trashIcon}
                    style={styles.trashIcon}
                    resizeMode="contain"
                  />
                ) : (
                  <Text style={styles.trashEmoji}>🗑️</Text>
                )}
              </TouchableOpacity>
            </View>
          </View>
        );
      })}

      {/* Rodapé de paginação */}
      <View style={styles.paginacaoRow}>
        <Text style={styles.paginacaoInfo}>
          {totalRegistros > 0
            ? inicioRegistro + ' a ' + fimRegistro + ' de ' + totalRegistros + ' registros'
            : 'Nenhum registro'}
        </Text>

        <View style={styles.paginacaoControles}>
          <TouchableOpacity
            style={[
              styles.paginacaoBotao,
              currentPage <= 1 && styles.paginacaoBotaoDesabilitado,
            ]}
            onPress={function() { if (onPageChange) onPageChange(currentPage - 1); }}
            disabled={currentPage <= 1}
          >
            <Text style={styles.paginacaoSeta}>{'<'}</Text>
          </TouchableOpacity>

          <Text style={styles.paginacaoTexto}>
            {'Página '}
            <Text style={styles.paginacaoDestaque}>
              {String(currentPage).padStart(2, '0')}
            </Text>
            {' de '}
            <Text style={styles.paginacaoDestaque}>
              {String(totalPages).padStart(2, '0')}
            </Text>
          </Text>

          <TouchableOpacity
            style={[
              styles.paginacaoBotao,
              currentPage >= totalPages && styles.paginacaoBotaoDesabilitado,
            ]}
            onPress={function() { if (onPageChange) onPageChange(currentPage + 1); }}
            disabled={currentPage >= totalPages}
          >
            <Text style={styles.paginacaoSeta}>{'>'}</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Modal de confirmação */}
      <ConfirmModal
        visible={usuarioSelecionado !== null}
        userName={nomeUsuarioSelecionado}
        onConfirmar={confirmarDesativacao}
        onCancelar={fecharModal}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    borderWidth: 1,
    borderColor: colors.tableCardBorder,
    borderRadius: 16,
    overflow: 'hidden',
    backgroundColor: '#E8F4F8',
  },
  titulo: {
    fontSize: 18,
    color: '#2F3036',
    fontWeight: '500',
    paddingHorizontal: 24,
    paddingVertical: 18,
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
  dataRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 14,
    paddingHorizontal: 24,
    backgroundColor: '#FFFFFF',
  },
  dataRowBorder: {
    borderBottomWidth: 1,
    borderBottomColor: colors.tableRowSeparator,
  },
  dataCell: {
    fontSize: 13,
    color: colors.tableRowText,
    ...typography.users,
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
    ...typography.users,

  },
  trashButton: {
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
  colUsuario:  { flex: 2.5 },
  colStatus:   { flex: 1.5 },
  colCadastro: { flex: 1.5 },
  colLivros:   { flex: 1 },
  colEstantes: { flex: 2 },
  colAcoes:    { flex: 1, alignItems: 'center' },
  paginacaoRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end',
    paddingHorizontal: 24,
    paddingVertical: 14,
    gap: 16,
    borderTopWidth: 1,
    borderTopColor: colors.tableRowSeparator,
    backgroundColor: '#FFFFFF',
  },
  paginacaoInfo: {
    fontSize: 12,
    color: colors.tablePaginacaoText,
  },
  paginacaoControles: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    borderWidth: 1,
    borderColor: colors.tablePaginacaoBorder,
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 6,
  },
  paginacaoBotao: {
    paddingHorizontal: 6,
  },
  paginacaoBotaoDesabilitado: {
    opacity: 0.3,
  },
  paginacaoSeta: {
    fontSize: 14,
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