import { StyleSheet, Text, TextInput, TouchableOpacity, View, Alert, Image, Button } from "react-native";
import React, { useState } from 'react';
import { colors, typography, spacing } from '../styles/index';


export default function LoginScreen() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const entrar = () => {
    Alert.alert('Dados digitados', `E-mail: ${email}\nSenha: ${password}`);
  };

  return (
    <View style={styles.container}>

      <Image style={styles.logo} source={require('../../assets/logo1.png')} />

      {/* Caixa centralizada (Card) */}
      <View style={styles.card}>



        {/* Bloco do E-mail */}
        <Text style={styles.textoLogin}>E-mail</Text>
        <TextInput
          style={[styles.input , {marginBottom: spacing.md}]}
          placeholder="Insira seu E-mail"
          placeholderTextColor={colors.textFaded}
          value={email}
          onChangeText={setEmail}
          keyboardType="email-address"
          autoCapitalize="none"
        />

        {/* Bloco da Senha */}
        <Text style={styles.textoLogin}>Senha</Text>
        <TextInput
          style={[styles.input , {marginBottom: spacing.sm}]}
          placeholder="Insira sua senha"
          placeholderTextColor={colors.textFaded}
          value={password}
          onChangeText={setPassword}
          secureTextEntry={true}
          autoCapitalize="none"
        />
        <TouchableOpacity>
          <Text style={styles.esqueceuSenha}>
            Redefinir Senha
          </Text>
        </TouchableOpacity>

        {/* Botão de Entrar */}
        <TouchableOpacity style={styles.botao} onPress={entrar}>
          <Text style={styles.botaoText}>Entrar</Text>
        </TouchableOpacity>

      </View>


    </View>
  );
}

const styles = StyleSheet.create({
  logo: {
    height: 121,
    width: 263,
    marginBottom: spacing.logoGap,
    resizeMode: 'contain', // <-- Garante que a imagem se ajuste bem aos limites
  },
  container: {
    flex: 1,
    justifyContent: 'center', // Centraliza verticalmente
    alignItems: 'center',     // Centraliza horizontalmente
    backgroundColor: colors.background, // Fundo cinza para destacar a caixa
    padding: 20,
  },
  card: {
    width: '100%',            // Ocupa a largura disponível
    maxWidth: 571,            // Limita a largura em telas grandes (tablets/web)
    backgroundColor: colors.background,  // Caixa branca
    paddingTop: spacing.xxl,
    borderRadius: 12,         // Bordas arredondadas
    paddingHorizontal: 36,
    paddingBottom: spacing.xxl,

    // Configuração de sombra para iOS
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 6,
    // Configuração de sombra para Android
    elevation: 5,
    borderWidth: 4,
    borderColor: colors.borderColor,
    boxShadow: '4px 4px 0px 0px #380641'
  },
  input: {
    borderWidth: 1,
    color: colors.textFaded,
    borderColor: colors.textFadedStroke,
    borderRadius: 12,
    padding: 10,
    fontSize: 16,
    borderWidth: 2,
  },
  botao: {
    backgroundColor: colors.surface,
    padding: 12,
    borderRadius: 12,
    alignItems: 'center',
    borderColor: colors.bttnStroke,
    borderWidth: 2,
  },
  botaoText: {
    color: colors.bttnText,
    alignItems: 'center',
    ...typography.button
  },
  textoLogin: {
    color: colors.loginText,
    marginBottom: spacing.sm,
    ...typography.login
  },
  esqueceuSenha: {
    marginBottom: spacing.xl,
    color: colors.textFadedStroke,
    ...typography.redefinir

  }
});

