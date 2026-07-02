// Arquivo: src/context/ThemeContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { colorsLight, colorsDark } from '../styles/colors';

const THEME_STORAGE_KEY = '@minha_estante_theme';

const ThemeContext = createContext(undefined);


export function ThemeProvider({ children }) {
  const [darkMode, setDarkMode] = useState(false);

  const [carregandoTema, setCarregandoTema] = useState(true);

  // Lê a preferência salva assim que o app abre
  useEffect(() => {
    async function carregarTemaSalvo() {
      try {
        const valorSalvo = await AsyncStorage.getItem(THEME_STORAGE_KEY);
        if (valorSalvo !== null) {
          setDarkMode(valorSalvo === 'dark');
        }
      } catch (erro) {
        // Se der erro na leitura, só segue com o padrão (modo claro)
        console.warn('Não foi possível carregar o tema salvo:', erro);
      } finally {
        setCarregandoTema(false);
      }
    }
    carregarTemaSalvo();
  }, []);

  async function toggleTheme() {
    const novoValor = !darkMode;
    setDarkMode(novoValor);
    try {
      await AsyncStorage.setItem(THEME_STORAGE_KEY, novoValor ? 'dark' : 'light');
    } catch (erro) {
      console.warn('Não foi possível salvar o tema:', erro);
    }
  }

  const colors = darkMode ? colorsDark : colorsLight;

  return (
    <ThemeContext.Provider value={{ darkMode, toggleTheme, colors, carregandoTema }}>
      {children}
    </ThemeContext.Provider>
  );
}


export function useTheme() {
  const context = useContext(ThemeContext);
  if (context === undefined) {
    throw new Error('useTheme precisa ser usado dentro de um <ThemeProvider>');
  }
  return context;
}