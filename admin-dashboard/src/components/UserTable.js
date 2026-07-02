// Arquivo: src/components/UserTable.js
import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { useTranslation } from 'react-i18next';
import UserDetailsModal from './UserDetailsModal';
import { useTheme } from '../context/ThemeContext';

export default function UserTable({
  data = [],
  currentPage = 1,
  totalPages = 1,
  totalRegistros = 0,
  pageSize = 10,
  onPageChange,
  ordenacao,
  onOrdenar,
  ordenacaoDesabilitada = false,
  onExportarCSV,
  exportando = false,
}) {
  const { t } = useTranslation();
  const { colors } = useTheme();
  const styles = getStyles(colors);

  const [usuarioDetalhes, setUsuarioDetalhes] = useState(null);

  const inicioRegistro = totalRegistros > 0 ? (currentPage - 1) * pageSize + 1 : 0;
  const fimRegistro = Math.min(currentPage * pageSize, totalRegistros);

  function IconeOrdenacao({ campo }) {
    if (!ordenacao || ordenacao.campo !== campo) return null;
    return <Text style={styles.iconeOrdenacao}>{ordenacao.direcao === 'asc' ? ' ▲' : ' ▼'}</Text>;
  }

  return (
    <View style={styles.card}>

      <View style={styles.tituloRow}>
        <Text style={styles.titulo}>{t('table.listaUsuarios')}</Text>

        <TouchableOpacity
          style={[styles.exportarBotao, exportando && styles.exportarBotaoDesabilitado]}
          onPress={onExportarCSV}
          disabled={exportando}
          accessibilityRole="button"
          accessibilityLabel={t('table.exportarA11y')}
        >
          {exportando ? (
            <ActivityIndicator size="small" color={colors.tableHeaderText} />
          ) : (
            <Text style={styles.exportarTexto}>⬇ {t('table.exportarCsv')}</Text>
          )}
        </TouchableOpacity>
      </View>

      <View style={styles.headerRow}>
        <TouchableOpacity
          style={[styles.colUsuario]}
          onPress={() => onOrdenar && onOrdenar('nome')}
          disabled={ordenacaoDesabilitada}
        >
          <Text style={styles.headerCell}>{t('table.colUsuario')}<IconeOrdenacao campo="nome" /></Text>
        </TouchableOpacity>
        <Text style={[styles.headerCell, styles.colStatus]}>{t('table.colStatus')}</Text>
        <TouchableOpacity
          style={[styles.colCadastro]}
          onPress={() => onOrdenar && onOrdenar('cadastro')}
          disabled={ordenacaoDesabilitada}
        >
          <Text style={styles.headerCell}>{t('table.colCadastro')}<IconeOrdenacao campo="cadastro" /></Text>
        </TouchableOpacity>
        <Text style={[styles.headerCell, styles.colLivros]}>{t('table.colLivros')}</Text>
        <Text style={[styles.headerCell, styles.colEstantes]}>{t('table.colEstantes')}</Text>
        <Text style={[styles.headerCell, styles.colAcoes]}>{t('table.colAcoes')}</Text>
      </View>

      {data.length === 0 ? (
        <View style={styles.vazioContainer}>
          <Text style={styles.vazioTexto}>{t('table.nenhumUsuario')}</Text>
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
                  {isAtivo ? t('table.ativo') : t('table.inativo')}
                </Text>
              </View>

              <Text style={[styles.dataCell, styles.colCadastro]}>{usuario.cadastro}</Text>
              <Text style={[styles.dataCell, styles.colLivros]}>{usuario.livros}</Text>
              <Text style={[styles.dataCell, styles.colEstantes]}>{usuario.estantes}</Text>

              <View style={[styles.dataCell, styles.colAcoes]}>
                <TouchableOpacity
                  onPress={function () { setUsuarioDetalhes(usuario); }}
                  style={styles.detalhesButton}
                  accessibilityLabel={t('table.verDetalhesA11y', { nome: usuario.name })}
                  accessibilityRole="button"
                >
                  <Text style={styles.detalhesTexto}>👁 {t('table.verDetalhes')}</Text>
                </TouchableOpacity>
              </View>
            </View>
          );
        })
      )}

      <View style={styles.paginacaoRow}>
        <Text style={styles.paginacaoInfo}>
          {totalRegistros > 0
            ? t('table.registrosInfo', { inicio: inicioRegistro, fim: fimRegistro, total: totalRegistros })
            : t('table.nenhumRegistro')}
        </Text>

        <View style={styles.paginacaoControles}>
          <TouchableOpacity
            style={[
              styles.paginacaoBotaoCirculo,
              currentPage <= 1 && styles.paginacaoBotaoDesabilitado,
            ]}
            onPress={function () { if (onPageChange) onPageChange(currentPage - 1); }}
            disabled={currentPage <= 1}
            accessibilityLabel={t('table.paginaAnteriorA11y')}
          >
            <Text style={styles.paginacaoSeta}>{'<'}</Text>
          </TouchableOpacity>

          <Text style={styles.paginacaoTexto}>
            {t('table.pagina')}{' '}
            <Text style={styles.paginacaoDestaque}>
              {String(currentPage).padStart(2, '0')}
            </Text>
            {'  '}{t('table.de')}{'  '}
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
            accessibilityLabel={t('table.proximaPaginaA11y')}
          >
            <Text style={styles.paginacaoSeta}>{'>'}</Text>
          </TouchableOpacity>
        </View>
      </View>

      <UserDetailsModal
        visible={usuarioDetalhes !== null}
        usuario={usuarioDetalhes}
        onFechar={() => setUsuarioDetalhes(null)}
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
    exportarBotao: {
      paddingVertical: 6,
      paddingHorizontal: 14,
      borderRadius: 8,
      borderWidth: 1,
      borderColor: colors.tableHeaderText,
      minWidth: 130,
      alignItems: 'center',
    },
    exportarBotaoDesabilitado: {
      opacity: 0.6,
    },
    exportarTexto: {
      fontSize: 13,
      color: colors.tableHeaderText,
      fontWeight: '600',
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
    detalhesButton: {
      padding: 4,
    },
    detalhesTexto: {
      fontSize: 12,
      fontWeight: '600',
      color: colors.tableHeaderText,
    },
    colUsuario: { flex: 2.5 },
    colStatus: { flex: 1.5 },
    colCadastro: { flex: 1.5 },
    colLivros: { flex: 1 },
    colEstantes: { flex: 2 },
    colAcoes: { flex: 1.4, alignItems: 'center' },
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