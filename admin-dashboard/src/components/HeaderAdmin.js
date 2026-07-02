import React, { useState } from 'react';
import { StyleSheet, View, Image, TextInput, Text, TouchableOpacity } from 'react-native';
import { useTranslation } from 'react-i18next';
import { typography } from '../styles';
import { useTheme } from '../context/ThemeContext';
import { useAuth } from '../hooks/UseAuth';
import ConfirmModal from './ConfirmModal';

export default function HeaderAdmin({
  avatarSource = require('../../assets/icons/avatar.png'),
  searchValue,
  onSearchChange,
  onSearchSubmit,
}) {
  const { t } = useTranslation();
  const { darkMode, toggleTheme, colors } = useTheme();
  const { user, sair } = useAuth();
  const styles = getStyles(colors);

  const [internalSearch, setInternalSearch] = useState('');
  const [modalLogoutVisivel, setModalLogoutVisivel] = useState(false);

  const valorDaBusca = searchValue !== undefined ? searchValue : internalSearch;
  const aoMudarBusca = onSearchChange || setInternalSearch;
  const adminName = user?.displayName || user?.email || t('header.admin');
  const adminRole = t('header.admin');

  return (
    <View style={styles.header}>
      <View style={styles.headerContent}>

        <Image
          style={styles.logo}
          source={
            darkMode
              ? require('../../assets/logo/logo2.png')
              : require('../../assets/logo/logoD.png')
          }
          accessible
          accessibilityLabel={t('login.logoA11y')}
          accessibilityRole="image"
        />

        <View style={styles.searchBar}>
          <Image
            source={require('../../assets/icons/Search.png')}
            style={styles.inputIcon}
            accessible
            accessibilityLabel={t('header.buscaA11yLabel')}
            accessibilityRole="image"
          />
          <TextInput
            placeholder={t('header.buscarPlaceholder')}
            placeholderTextColor={colors.searchbarText}
            style={styles.searchInput}
            value={valorDaBusca}
            onChangeText={aoMudarBusca}
            onSubmitEditing={onSearchSubmit}
            returnKeyType="search"
            accessibilityLabel={t('header.buscaA11yLabel')}
            accessibilityHint={t('header.buscaA11yHint')}
          />
        </View>

        <View style={styles.adminArea}>

          <View
            style={styles.profileBox}
            accessible
            accessibilityLabel={adminRole + ' ' + adminName}
            accessibilityRole="text"
          >
            <View style={styles.avatarCircle}>
              <Image
                source={avatarSource}
                style={styles.avatarIcon}
                accessible
                accessibilityLabel={'Foto de perfil de ' + adminName}
                accessibilityRole="image"
              />
            </View>
            <View style={styles.adminTexts}>
              <Text style={styles.roleText}>{adminRole}</Text>
              <Text style={styles.nameText} numberOfLines={1}>{adminName}</Text>
            </View>

            <TouchableOpacity
              style={styles.logoutButton}
              onPress={() => setModalLogoutVisivel(true)}
              activeOpacity={0.7}
              hitSlop={{ top: 8, bottom: 8, left: 8, right: 8 }}
              accessible
              accessibilityRole="button"
              accessibilityLabel={t('header.sairConta')}
              accessibilityHint={t('header.sairA11yHint')}
            >
              <Image
                source={require('../../assets/icons/logout1.png')}
                style={styles.logoutIcon}
                accessible={false}
              />
            </TouchableOpacity>
          </View>

          <TouchableOpacity
            style={[styles.toggleTrack, darkMode && styles.toggleTrackActive]}
            onPress={toggleTheme}
            activeOpacity={0.8}
            accessible
            accessibilityRole="switch"
            accessibilityLabel={t('header.alternarModoEscuro')}
            accessibilityState={{ checked: darkMode }}
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
                accessible={false}
              />
            </View>
          </TouchableOpacity>

        </View>
      </View>

      <ConfirmModal
        visible={modalLogoutVisivel}
        mensagem={t('header.logoutMensagem')}
        onConfirmar={() => {
          setModalLogoutVisivel(false);
          sair();
        }}
        onCancelar={() => setModalLogoutVisivel(false)}
      />
    </View>
  );
}

function getStyles(colors) {
  return StyleSheet.create({
    header: {
      height: 115,
      borderBottomWidth: 1,
      borderColor: colors.hboarder,
      width: '100%',
      backgroundColor: colors.background,
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
      backgroundColor: colors.background,
      overflow: 'hidden',
    },
    searchInput: {
      flex: 1,
      fontSize: 14,
      outlineStyle: 'none',
      width: '100%',
      minWidth: 0,
      color: colors.hboarder,
    },
    inputIcon: {
      width: 20,
      height: 20,
      resizeMode: 'contain',
      marginRight: 12,
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
      maxWidth: 220,
    },
    avatarCircle: {
      width: 40,
      height: 40,
      borderRadius: 20,
      backgroundColor: colors.background,
      borderWidth: 1,
      borderColor: '#7FA1B8',
      justifyContent: 'center',
      alignItems: 'center',
    },
    avatarIcon: {
      width: 16,
      height: 18,
      resizeMode: 'contain',
      alignSelf: 'center',
    },
    adminTexts: {
      justifyContent: 'center',
      flexShrink: 1,
    },
    roleText: {
      color: colors.hboarder,
      textTransform: 'uppercase',
      ...typography.admrole,
    },
    nameText: {
      color: colors.hboarder,
      ...typography.admtext,
    },
    logoutButton: {
      marginLeft: 4,
      padding: 4,
    },
    logoutIcon: {
      width: 18,
      height: 18,
      resizeMode: 'contain',
    },
    toggleTrack: {
      width: 50,
      height: 26,
      borderRadius: 13,
      backgroundColor: colors.background,
      padding: 2,
      justifyContent: 'center',
      borderWidth: 1,
      borderColor: colors.blue1,
    },
    toggleTrackActive: {
      backgroundColor: colors.darkmode,
    },
    toggleCircle: {
      width: 22,
      height: 22,
      borderRadius: 11,
      backgroundColor: colors.blue1,
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
      tintColor: '#ffffff',
    },
    toggleIconDark: {
      tintColor: '#000000',
    },
  });
}