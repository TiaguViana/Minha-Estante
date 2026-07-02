import React from 'react';
import { StyleSheet, View, ScrollView, Text, ActivityIndicator } from 'react-native';
import { useTranslation } from 'react-i18next';
import MetricCard from '../components/MetricCard';
import HeaderAdmin from '../components/HeaderAdmin';
import UserTable from '../components/UserTable';
import { typography } from '../styles';
import { useTheme } from '../context/ThemeContext';
import { useDashboardData } from '../hooks/useDashboardData';

export default function DashboardScreen() {
  const { colors } = useTheme();
  const { t } = useTranslation();
  const styles = getStyles(colors);

  const {
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
  } = useDashboardData();

  function handlePageChange(novaPagina) {
    if (novaPagina > currentPage) proximaPagina();
    else paginaAnterior();
  }

  return (
    <ScrollView style={styles.container} bounces={false}>
      <HeaderAdmin
        searchValue={termoBusca}
        onSearchChange={setTermoBusca}
      />

      <View style={styles.contentLayout}>
        <Text style={styles.mainTitle}>{t('dashboard.titulo')}</Text>

        <View style={styles.metricsRow}>
          <MetricCard
            variant="purple"
            icon={require('../../assets/icons/lucide_users.png')}
            value={String(totalUsuarios)}
            label={t('dashboard.usuariosNoSistema')}
          />
          <MetricCard
            variant="green"
            icon={require('../../assets/icons/tabler_book.png')}
            value={String(totalLivros)}
            label={t('dashboard.livrosCadastrados')}
          />
          <MetricCard
            variant="gold"
            icon={require('../../assets/icons/book.png')}
            tintIcon={false}
            value={String(totalEstantesSecretas)}
            label={t('dashboard.estantesSecretasAtivas')}
          />
        </View>

        {erro ? <Text style={styles.erroText}>{erro}</Text> : null}
        {erroExport ? <Text style={styles.erroText}>{erroExport}</Text> : null}

        {loading && usuarios.length === 0 ? (
          <View style={styles.loadingContainer}>
            <ActivityIndicator size="large" color={colors.blue1} />
          </View>
        ) : (
          <UserTable
            data={usuarios}
            currentPage={currentPage}
            totalPages={totalPages}
            totalRegistros={totalRegistros}
            pageSize={10}
            onPageChange={handlePageChange}
            ordenacao={ordenacao}
            onOrdenar={alternarOrdenacao}
            ordenacaoDesabilitada={buscando}
            onExportarCSV={exportarCSV}
            exportando={exportando}
          />
        )}
      </View>
    </ScrollView>
  );
}

function getStyles(colors) {
  return StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: colors.background,
      width: '100%',
    },
    contentLayout: {
      paddingHorizontal: 80,
      marginTop: 40,
      paddingBottom: 60,
    },
    mainTitle: {
      ...typography.h2,
      color: colors.textbrown,
      marginBottom: 35,
    },
    metricsRow: {
      flexDirection: 'row',
      gap: 36,
      marginBottom: 40,
    },
    loadingContainer: {
      paddingVertical: 60,
      alignItems: 'center',
    },
    erroText: {
      color: '#C0392B',
      fontSize: 14,
      marginBottom: 20,
    },
  });
}