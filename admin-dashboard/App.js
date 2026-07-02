import AppNavigator from "./src/navigation/AppNavigator";
import { useFonts } from "expo-font";
import { ThemeProvider } from "./src/context/ThemeContext";

import {
  LibreBaskerville_400Regular,
  LibreBaskerville_700Bold,
} from "@expo-google-fonts/libre-baskerville";

import {
  AtkinsonHyperlegible_400Regular,
  AtkinsonHyperlegible_700Bold,
} from "@expo-google-fonts/atkinson-hyperlegible";

export default function App() {
    const [fontsLoaded] = useFonts({
    LibreBaskerville_400Regular,
    LibreBaskerville_700Bold,

    AtkinsonHyperlegible_400Regular,
    AtkinsonHyperlegible_700Bold,
  });

  if (!fontsLoaded) {
    return null; // aguarda carregar as fontes
  }

  return (
    <ThemeProvider>
      <AppNavigator />
    </ThemeProvider>
  );
}