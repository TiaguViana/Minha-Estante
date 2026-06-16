// Arquivo: src/components/MetricCard.js
import React from 'react';
import { StyleSheet, View, Text, Image } from 'react-native';

export default function MetricCard({ value, label, icon, badgeColor, iconColor, shadowColor }) {
  return (
    <View style={[styles.card, { borderColor: shadowColor, shadowColor: shadowColor }]}>
      
      {/* Mini quadrado de fundo do ícone */}
      <View style={[styles.iconBadge, { backgroundColor: badgeColor }]}>
        <Image source={icon} style={[styles.cardIcon, { tintColor: iconColor }]} />
      </View>
      
      {/* Valor numérico */}
      <Text style={styles.cardValue}>{value}</Text>
      
      {/* Descrição do card */}
      <Text style={styles.cardLabel}>{label}</Text>

    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderWidth: 2,
    borderRadius: 20,
    padding: 24,
    // Sombra sólida e deslocada idêntica ao seu Figma
    shadowOffset: { width: 5, height: 5 },
    shadowOpacity: 1,
    shadowRadius: 0,
    elevation: 5,
  },
  iconBadge: {
    width: 42,
    height: 42,
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 16,
  },
  cardIcon: {
    width: 24,
    height: 24,
    resizeMode: 'contain',
    alignSelf: 'center',
  },
  cardValue: {
    fontSize: 40,
    fontWeight: 'bold',
    color: '#0066FF', // Azul vibrante do Figma
    marginBottom: 8,
  },
  cardLabel: {
    fontSize: 16,
    color: '#333333',
    fontWeight: '500',
  },
});