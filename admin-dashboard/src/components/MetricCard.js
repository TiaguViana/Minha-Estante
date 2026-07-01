// Arquivo: src/components/MetricCard.js
import React from 'react';
import { StyleSheet, View, Text, Image } from 'react-native';

const VARIANTS = {
  purple: {
    border: '#BC86C4',
    iconBg: '#F1E4F5',
    iconTint: '#9B5FA8',
  },
  green: {
    border: '#B4C273',
    iconBg: '#EBF1D7',
    iconTint: '#7C8F3E',
  },
  gold: {
    border: '#E8C989',
    iconBg: '#FBE9C3',
    iconTint: '#C99A3E',
  },
};

/**
 * Card de métrica do Dashboard (ex: "102 Usuários no Sistema").
 *
 * @param {keyof VARIANTS} variant - paleta do card ('purple' | 'green' | 'gold')
 * @param {ImageSourcePropType} icon - imagem do ícone (require(...))
 * @param {boolean} tintIcon - se true, aplica a cor da variante sobre o ícone
 *   (use para ícones de linha/monocromáticos). Deixe false para ícones já
 *   coloridos (como o ícone da "estante", no card dourado do mockup).
 * @param {string|number} value - valor grande exibido (ex: "102", "30")
 * @param {string} label - legenda abaixo do valor
 * @param {object} [style] - estilo extra para o container (ex: flex no row)
 */
export default function MetricCard({
  variant = 'purple',
  icon,
  tintIcon = true,
  value,
  label,
  style,
}) {
  const theme = VARIANTS[variant] || VARIANTS.purple;

  return (
    <View
      style={[
        styles.card,
        {
          borderColor: theme.border,
          boxShadow: '4px 4px 0px 0px #000000',
        },
        style,
      ]}
    >
      <View style={[styles.iconBox, { backgroundColor: theme.iconBg }]}>
        {icon ? (
          <Image
            source={icon}
            style={[styles.icon, tintIcon && { tintColor: theme.iconTint }]}
            resizeMode="contain"
            accessible
            accessibilityLabel={label}
          />
        ) : null}
      </View>

      <Text style={styles.value}>{value}</Text>
      <Text style={styles.label}>{label}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    flex: 1,
    minWidth: 220,
    maxWidth: 299,
    height: 173,
    backgroundColor: '#FFFFFF',
    borderWidth: 2,
    borderRadius: 16,
    paddingVertical: 20,
    paddingHorizontal: 22,
  },
  iconBox: {
    width: 40,
    height: 40,
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 18,
  },
  icon: {
    width: 25,
    height: 22.5,
  },
  value: {
    fontFamily: 'serif',
    fontWeight: '700',
    fontSize: 32,
    color: '#0072FF',
    marginBottom: 6,
  },
  label: {
    fontSize: 15,
    color: '#33333A',
  },
});