// Arquivo: src/screens/SplashScreen.js
import { View, Image } from "react-native";
import { useEffect } from "react";
import { useTheme } from "../context/ThemeContext";

export default function SplashScreen({ navigation }) {
  const { colors, darkMode } = useTheme();

  useEffect(() => {
    const timer = setTimeout(() => {
      navigation.replace("Login");
    }, 5000);

    return () => clearTimeout(timer);
  }, []);

  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: colors.background,
      }}
    >
      <Image
        source={
          darkMode
            ? require("../../assets/logo/logo2.png")
            : require("../../assets/logo/logoD.png")
        }
        style={{ width: 237, height: 109 }}
        resizeMode="contain"
        accessible
        accessibilityLabel="Minha Estante — logotipo"
        accessibilityRole="image"
      />
    </View>
  );
}