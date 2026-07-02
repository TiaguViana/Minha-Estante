// Arquivo: src/components/MetricCard.js
import React from 'react';
import { StyleSheet, View, Text, Image } from 'react-native';
import { useTheme } from '../context/ThemeContext';

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

export default function MetricCard({
  variant = 'purple',
  icon,
  tintIcon = true,
  value,
  label,
  style,
}) {
  const { colors } = useTheme();
  const theme = VARIANTS[variant] || VARIANTS.purple;
  const styles = getStyles(colors);

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

function getStyles(colors) {
  return StyleSheet.create({
    card: {
      flex: 1,
      minWidth: 220,
      maxWidth: 299,
      height: 173,
      backgroundColor: colors.background,
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
      width: 30,  
      height: 27, 
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
      color: colors.tableRowText,
    },
  });
}