// Arquivo: src/navigation/AppNavigator.js
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { View, ActivityIndicator } from "react-native";

import SplashScreen from "../screens/SplashScreen";
import LoginScreen from "../screens/LoginScreen";
import DashboardScreen from "../screens/DashboardScreen";
import PlaygroundScreen from "../screens/PlaygroundScreen";

import { useAuth } from "../hooks/UseAuth";
import { useTheme } from "../context/ThemeContext";

const Stack = createNativeStackNavigator();

// Stack de telas públicas (não precisa estar logado)
function AuthStack() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Splash" component={SplashScreen} />
      <Stack.Screen name="Login" component={LoginScreen} />
    </Stack.Navigator>
  );
}

// Stack de telas privadas (precisa estar logado)
function AppStack() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Dashboard" component={DashboardScreen} />
      <Stack.Screen name="Playground" component={PlaygroundScreen} />
    </Stack.Navigator>
  );
}

export default function AppNavigator() {
  const { user, loading } = useAuth();
  const { colors } = useTheme();

  // Enquanto o Firebase verifica se existe sessão salva, mostra um
  // indicador de carregamento em vez de piscar a tela de login por
  // um instante antes de redirecionar pra Dashboard.
  if (loading) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: colors.background }}>
        <ActivityIndicator size="large" color={colors.blue1} />
      </View>
    );
  }

  return (
    <NavigationContainer>
      {/* Se user existir, mostra o app. Se não, mostra login.
          Quando o login der certo, o onAuthStateChanged atualiza
          o user e o Navigator troca de stack automaticamente,
          sem precisar de navigation.navigate() nenhum. */}
      {user ? <AppStack /> : <AuthStack />}
    </NavigationContainer>
  );
}