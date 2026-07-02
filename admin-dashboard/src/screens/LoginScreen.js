import { StyleSheet, Text, TextInput, TouchableOpacity, View, Image } from "react-native";
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { sendPasswordResetEmail } from 'firebase/auth';
import { auth } from '../services/firebaseConfig';
import { typography, spacing } from '../styles/index';
import { useTheme } from '../context/ThemeContext';
import { useAuth } from '../hooks/UseAuth';

export default function LoginScreen() {
  const { t } = useTranslation();
  const { colors, darkMode } = useTheme();
  const { entrar, erro } = useAuth();
  const styles = getStyles(colors);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [mostrarSenha, setMostrarSenha] = useState(false);

  const [enviandoReset, setEnviandoReset] = useState(false);
  const [mensagemReset, setMensagemReset] = useState('');
  const [tipoMensagemReset, setTipoMensagemReset] = useState('');

  async function handleEntrar() {
    if (!email || !password) return;
    setLoading(true);
    await entrar(email, password);
    setLoading(false);
  }

  async function handleRedefinirSenha() {
    setMensagemReset('');

    if (!email) {
      setTipoMensagemReset('erro');
      setMensagemReset(t('login.resetEmailVazio'));
      return;
    }

    setEnviandoReset(true);
    try {
      await sendPasswordResetEmail(auth, email);
      setTipoMensagemReset('sucesso');
      setMensagemReset(t('login.resetSucesso'));
    } catch (e) {
      setTipoMensagemReset('erro');
      if (e.code === 'auth/invalid-email') {
        setMensagemReset(t('login.resetErroInvalido'));
      } else if (e.code === 'auth/too-many-requests') {
        setMensagemReset(t('login.resetErroMuitasTentativas'));
      } else {
        setMensagemReset(t('login.resetErroGenerico'));
      }
    } finally {
      setEnviandoReset(false);
    }
  }

  return (
    <View style={styles.container}>

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

      <View style={styles.card}>

        <Text style={styles.textoLogin}>{t('login.emailLabel')}</Text>
        <View style={[styles.inputContainer, { marginBottom: spacing.md }]}>
          <TextInput
            style={styles.input}
            placeholder={t('login.emailPlaceholder')}
            placeholderTextColor={colors.textFaded}
            value={email}
            onChangeText={setEmail}
            keyboardType="email-address"
            autoCapitalize="none"
            returnKeyType="next"
            accessibilityLabel={t('login.emailA11yLabel')}
            accessibilityHint={t('login.emailA11yHint')}
          />
          <Image
            source={require('../../assets/icons/Vector.png')}
            style={styles.inputIcon}
            accessible={false}
          />
        </View>

        <Text style={styles.textoLogin}>{t('login.senhaLabel')}</Text>
        <View style={[styles.inputContainer, { marginBottom: spacing.sm }]}>
          <TextInput
            style={styles.input}
            placeholder={t('login.senhaPlaceholder')}
            placeholderTextColor={colors.textFaded}
            value={password}
            onChangeText={setPassword}
            secureTextEntry={!mostrarSenha}
            autoCapitalize="none"
            returnKeyType="go"
            onSubmitEditing={handleEntrar}
            accessibilityLabel={t('login.senhaA11yLabel')}
            accessibilityHint={t('login.senhaA11yHint')}
          />
          <TouchableOpacity
            onPress={() => setMostrarSenha((prev) => !prev)}
            activeOpacity={0.7}
            hitSlop={{ top: 10, bottom: 10, left: 10, right: 10 }}
            accessibilityRole="button"
            accessibilityLabel={mostrarSenha ? t('login.ocultarSenha') : t('login.mostrarSenha')}
            accessibilityHint={t('login.alternarVisibilidadeSenha')}
          >
            <Image
              source={require('../../assets/icons/Icon.png')}
              style={styles.inputIcon}
              accessible={false}
            />
          </TouchableOpacity>
        </View>

        {erro ? (
          <Text style={styles.erroText} accessibilityRole="alert">{erro}</Text>
        ) : null}

        {mensagemReset ? (
          <Text
            style={tipoMensagemReset === 'sucesso' ? styles.resetSucessoText : styles.erroText}
            accessibilityRole="alert"
          >
            {mensagemReset}
          </Text>
        ) : null}

        <TouchableOpacity
          onPress={handleRedefinirSenha}
          disabled={enviandoReset}
          accessibilityLabel={enviandoReset ? t('login.enviandoA11y') : t('login.redefinirSenhaA11y')}
          accessibilityRole="link"
          accessibilityState={{ disabled: enviandoReset }}
        >
          <Text style={[styles.esqueceuSenha, enviandoReset && styles.esqueceuSenhaDesabilitada]}>
            {enviandoReset ? t('login.enviando') : t('login.redefinirSenha')}
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.botao, loading && styles.botaoDesabilitado]}
          onPress={handleEntrar}
          disabled={loading}
          accessibilityLabel={loading ? t('login.entrandoA11y') : t('login.entrarSistemaA11y')}
          accessibilityRole="button"
          accessibilityState={{ disabled: loading }}
        >
          <Text style={styles.botaoText}>{loading ? t('login.entrando') : t('login.entrar')}</Text>
        </TouchableOpacity>

      </View>

    </View>
  );
}

function getStyles(colors) {
  return StyleSheet.create({
    logo: {
      height: 121,
      width: 263,
      marginBottom: spacing.logoGap,
      resizeMode: 'contain',
    },
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: colors.background,
      padding: 20,
    },
    card: {
      width: '100%',
      maxWidth: 571,
      backgroundColor: colors.background,
      paddingTop: spacing.xxl,
      borderRadius: 12,
      paddingHorizontal: 36,
      paddingBottom: spacing.xxl,
      shadowColor: '#000',
      shadowOffset: { width: 0, height: 4 },
      shadowOpacity: 0.1,
      shadowRadius: 6,
      elevation: 5,
      borderWidth: 4,
      borderColor: colors.borderColor,
      boxShadow: '4px 4px 0px 0px #380641',
    },
    input: {
      flex: 1,
      color: colors.textFaded,
      fontSize: 16,
      paddingVertical: 10,
      borderWidth: 0,
    },
    botao: {
      backgroundColor: colors.surface,
      padding: 12,
      borderRadius: 12,
      alignItems: 'center',
      borderColor: colors.bttnStroke,
      borderWidth: 2,
    },
    botaoDesabilitado: {
      opacity: 0.6,
    },
    botaoText: {
      color: colors.bttnText,
      ...typography.button,
    },
    textoLogin: {
      color: colors.loginText,
      marginBottom: spacing.sm,
      ...typography.login,
    },
    esqueceuSenha: {
      marginBottom: spacing.xl,
      color: colors.textFadedStroke,
      ...typography.redefinir,
    },
    esqueceuSenhaDesabilitada: {
      opacity: 0.5,
    },
    erroText: {
      color: '#C0392B',
      marginBottom: spacing.sm,
      fontSize: 14,
    },
    resetSucessoText: {
      color: '#2E7D32',
      marginBottom: spacing.sm,
      fontSize: 14,
    },
    inputContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      borderWidth: 2,
      borderColor: colors.textFadedStroke,
      borderRadius: 12,
      paddingHorizontal: 12,
    },
    inputIcon: {
      width: 20,
      height: 20,
      resizeMode: 'contain',
    },
  });
}