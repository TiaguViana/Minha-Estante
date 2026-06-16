import React from 'react';
import { StyleSheet, View, ScrollView, Text, Image, TouchableOpacity } from 'react-native';
import MetricCard from '../components/MetricCard'; // <-- Importando o novo card aqui
import HeaderAdmin from '../components/HeaderAdmin'; // Importa o componente que criamos
import { colors, typography } from '../styles'; // Ajuste o caminho do seu arquivo de estilos se necessário


export default function DashboardScreen() {

  const usersData = Array(7).fill({
    name: 'Sabrina Lopes',
    status: 'Ativo',
    cadastro: '10/05/2026',
    livros: '45',
    estantes: '45'
  });
  
  return (
<ScrollView style={styles.container} bounces={false}> 
      {/* 1. O Header */}
      <HeaderAdmin />

      {/* Caixa vazia onde vamos testar os próximos passos */}
      <View style={styles.contentLayout}>
        <Text style={styles.mainTitle}>Gerenciamento de Usuários</Text>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.background,
    width: '100%',
  },
  contentLayout: {
    paddingHorizontal: 80,
    marginTop: 40,
  },
  mainTitle: {
    ...typography.h2,
    color: colors.textbrown, 
    marginBottom: 35,
  },
});