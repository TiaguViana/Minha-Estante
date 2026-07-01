import { View, Image } from "react-native";
import { useEffect } from "react";
import { colors, spacing, typography, borderRadius } from "../styles";

export default function SplashScreen({ navigation }) {

  useEffect(() => {

    const timer = setTimeout(() => {

      navigation.replace("Login");

    }, 5000);

    return () => clearTimeout(timer);

  }, []);

  return (

    <View
      style={{
        flex:1,
        justifyContent:"center",
        alignItems:"center",
        backgroundColor: colors.background,
      }}
    >

      <Image
        source={require("../../assets//logo/logoD.png")}
        style={{
          width:237,
          height:109
        }}
        resizeMode="contain"
      />

    </View>

  );

}