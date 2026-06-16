// Arquivo: src/components/HeaderAdmin.js
import React from 'react';
import { StyleSheet, View, Image, TextInput, Text, TouchableOpacity } from 'react-native';
import { colors, typography } from '../styles'; // Ajuste o caminho do seu arquivo de estilos se necessário

export default function HeaderAdmin() {

  const [darkMode, setDarkMode] = React.useState(false); // Estado para controlar o modo noturno
  return (
    <View style={styles.header}>
      <View style={styles.headerContent}>

        {/* 1. Logo */}
        <Image style={styles.logo} source={require('../../assets/logo/logoD.png')} />

        {/* 2. Barra de Busca Única (Contendo o Ícone e o Input lado a lado) */}
        <View style={styles.searchBar}>
          <Image
            source={require('../../assets/icons/Search.png')}
            style={styles.inputIcon}
          />
          <TextInput
            placeholder="Buscar Usuários, Livros..."
            placeholderTextColor={colors.searchbarText}
            style={styles.searchInput}
          />
        </View>

        {/* 3. Área do Admin + Slider Noturno */}
        <View style={styles.adminArea}>

          {/* Bloco de Perfil */}
          <View style={styles.profileBox}>
            <View style={styles.avatarCircle}>
              <Image
                source={require('../../assets/icons/avatar.png')} // <-- Coloque o caminho do seu ícone do Figma aqui
                style={styles.avatarIcon}
              />
            </View>
            <View style={styles.adminTexts}>
              <Text style={styles.roleText}>Admin</Text>
              <Text style={styles.nameText}>Sabrina</Text>
            </View>
          </View>

{/* Slider / Interruptor Modo Noturno */}
<TouchableOpacity 
  style={[styles.toggleTrack, darkMode && styles.toggleTrackActive]} 
  onPress={() => setDarkMode(!darkMode)}
  activeOpacity={0.8}
>
  <View style={[styles.toggleCircle, darkMode && styles.toggleCircleActive]}>
    <Image 
      source={
        darkMode 
          ? require('../../assets/icons/tabler_moon.png') 
          : require('../../assets/icons/tabler_sun.png') 
      }
      style={[
        styles.toggleIcon, 
        darkMode ? styles.toggleIconDark : styles.toggleIconLight
      ]} 
    />
  </View>
</TouchableOpacity>

        </View>

      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  header: {
    height: 115,
    borderBottomWidth: 1,
    borderColor: colors.hboarder,
    width: '100%',
  },
  headerContent: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    paddingLeft: 48,
    paddingRight: 48,
    justifyContent: 'space-between',
  },
  logo: {
    width: 159,
    height: 73,
    resizeMode: 'contain',
    marginRight: 67,
  },
  searchBar: {
    flex: 1,
    maxWidth: 790,
    height: 44,
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: colors.searchbar,
    borderRadius: 16,
    paddingHorizontal: 15,
    marginRight: 67,
    backgroundColor: '#FFFFFF',
    overflow: 'hidden',
  },
  searchInput: {
    flex: 1,
    fontSize: 14,
    outlineStyle: 'none',
    width: '100%',
    minWidth: 0,
  },
  inputIcon: {
    width: 20,
    height: 20,
    resizeMode: 'contain',
    marginRight: 12, // Ajustado para dar espaço do texto
  },


  adminArea: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 67,
  },
  profileBox: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 9,
  },
  avatarCircle: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: '#ffffff',
    borderWidth: 1,
    borderColor: '#7FA1B8',
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatarIcon: {
    width: 16,                // Ajuste o tamanho do ícone conforme o seu Figma (ex: 20x20 ou 24x24)
    height: 18,
    resizeMode: 'contain',
    alignSelf: 'center', // Garante o alinhamento próprio no centro
  },

  adminTexts: {
    justifyContent: 'center',
  },

  roleText: {
    color: '#2F3036',
    textTransform: 'uppercase',
    ...typography.admrole,
  },
  nameText: {
    color: '#2F3036',
    ...typography.admtext,
  },

  toggleTrack: {
    width: 50,
    height: 26,
    borderRadius: 13,
    backgroundColor: '#FFFFFF',
    padding: 2,
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: '#7FA1B8',
  },
  toggleTrackActive: {
    backgroundColor: '#23272D',
  },
  toggleCircle: {
    width: 22,
    height: 22,
    borderRadius: 11,
    backgroundColor: '#6B9BAB',
    justifyContent: 'center', 
    alignItems: 'center',
  },
  toggleCircleActive: {
    alignSelf: 'flex-end',
  },
  toggleIcon: {
    width: 14,
    height: 14,
    resizeMode: 'contain',
    
  },
  toggleIconLight: {
    tintColor: '#ffffff', // Um tom de amarelo/ouro para o Sol no modo claro
  },
  toggleIconDark: {
    tintColor: '#000000', // O roxo escuro do seu app para a Lua no modo noturno
  },
});