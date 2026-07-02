// Arquivo: src/screens/LoginScreen.js
import { StyleSheet, Text, TextInput, TouchableOpacity, View, Image } from "react-native";
import React, { useState } from 'react';
import { sendPasswordResetEmail } from 'firebase/auth';
import { auth } from '../services/firebaseConfig';
import { typography, spacing } from '../styles/index';
import { useTheme } from '../context/ThemeContext';
import { useAuth } from '../hooks/UseAuth';

export default function LoginScreen() {
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
      setMensagemReset('Digite seu e-mail no campo acima antes de redefinir a senha.');
      return;
    }

    setEnviandoReset(true);
    try {
      await sendPasswordResetEmail(auth, email);
      setTipoMensagemReset('sucesso');
      setMensagemReset('E-mail de redefinição enviado! Confira sua caixa de entrada (e o spam).');
    } catch (e) {
      setTipoMensagemReset('erro');
      if (e.code === 'auth/invalid-email') {
        setMensagemReset('Esse e-mail não parece válido. Confira e tente de novo.');
      } else if (e.code === 'auth/too-many-requests') {
        setMensagemReset('Muitas tentativas seguidas. Aguarde um pouco antes de tentar novamente.');
      } else {
        setMensagemReset('Não foi possível enviar o e-mail agora. Tente novamente em instantes.');
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
        accessibilityLabel="Minha Estante — logotipo"
        accessibilityRole="image"
      />

      <View style={styles.card}>

        <Text style={styles.textoLogin}>E-mail</Text>
        <View style={[styles.inputContainer, { marginBottom: spacing.md }]}>
          <TextInput
            style={styles.input}
            placeholder="Insira seu E-mail"
            placeholderTextColor={colors.textFaded}
            value={email}
            onChangeText={setEmail}
            keyboardType="email-address"
            autoCapitalize="none"
            returnKeyType="next"
            accessibilityLabel="Campo de e-mail"
            accessibilityHint="Digite seu endereço de e-mail"
          />
          <Image
            source={require('../../assets/icons/Vector.png')}
            style={styles.inputIcon}
            accessible={false}
          />
        </View>

        <Text style={styles.textoLogin}>Senha</Text>
        <View style={[styles.inputContainer, { marginBottom: spacing.sm }]}>
          <TextInput
            style={styles.input}
            placeholder="Insira sua senha"
            placeholderTextColor={colors.textFaded}
            value={password}
            onChangeText={setPassword}
            secureTextEntry={!mostrarSenha}
            autoCapitalize="none"
            returnKeyType="go"
            onSubmitEditing={handleEntrar}
            accessibilityLabel="Campo de senha"
            accessibilityHint="Digite sua senha de acesso"
          />
          <TouchableOpacity
            onPress={() => setMostrarSenha((prev) => !prev)}
            activeOpacity={0.7}
            hitSlop={{ top: 10, bottom: 10, left: 10, right: 10 }}
            accessibilityRole="button"
            accessibilityLabel={mostrarSenha ? 'Ocultar senha' : 'Mostrar senha'}
            accessibilityHint="Alterna a visibilidade da senha digitada"
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
          accessibilityLabel={enviandoReset ? 'Enviando e-mail de redefinição' : 'Redefinir senha'}
          accessibilityRole="link"
          accessibilityState={{ disabled: enviandoReset }}
        >
          <Text style={[styles.esqueceuSenha, enviandoReset && styles.esqueceuSenhaDesabilitada]}>
            {enviandoReset ? 'Enviando...' : 'Redefinir Senha'}
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.botao, loading && styles.botaoDesabilitado]}
          onPress={handleEntrar}
          disabled={loading}
          accessibilityLabel={loading ? 'Entrando, aguarde' : 'Entrar no sistema'}
          accessibilityRole="button"
          accessibilityState={{ disabled: loading }}
        >
          <Text style={styles.botaoText}>{loading ? 'Entrando...' : 'Entrar'}</Text>
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