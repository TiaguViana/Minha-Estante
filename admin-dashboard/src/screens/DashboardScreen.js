// Arquivo: src/screens/DashboardScreen.js
import React from 'react';
import { StyleSheet, View } from 'react-native';
import HeaderAdmin from '../components/HeaderAdmin'; // Importa o componente que criamos
import { colors } from '../styles'; // Ajuste o caminho do seu arquivo de estilos se necessário

export default function DashboardScreen() {
  return (
    <View style={styles.container}> 
      
      {/* Chamando o Header limpo aqui */}
      <HeaderAdmin />

      {/* Próximos passos (Cards, Tabelas) entrarão aqui embaixo */}

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.background,
    width: '100%',
  },
});