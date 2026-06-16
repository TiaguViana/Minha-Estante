/*
import { View, Text, Button } from "react-native";

export default function PlaygroundScreen() {
  return (
    <View
      style={{
        flex: 1,
        backgroundColor: "#dcdcdc",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <View
        style={{
          width: 300,
          height: 300,
          backgroundColor: "white",
          borderRadius: 20,
          padding: 20,
          justifyContent: "space-around",
          alignItems: "center"
        }}
      >
        <Text
          style={{
            fontSize: 30,
            fontWeight: "bold"
          }}
        >
          Minha Estante
        </Text>

        <Text
          style={{
            fontSize: 16
          }}
        >
          Testando componentes
        </Text>

        <Button
          title="Clique aqui"
          onPress={() => alert("Funcionou")}
        />
      </View>
    </View>
  );
}
  */

/* teste1 
import { View, Text } from "react-native";

export default function PlaygroundScreen() {

  const meuEstilo = {
    flex:1,
    backgroundColor:"#dcdcdc",
    justifyContent:"center",
    alignItems:"center"
  };

  return(

    <View style={meuEstilo}>

      <Text>Olá</Text>

    </View>

  );

} */
import { View, Text, Button } from "react-native";

export default function PlaygroundScreen() {
  return (
    <View
      style={
        {
        flex: 1,
        backgroundColor: "#dcdcdc",
        justifyContent: "center",
        alignItems: "center",
      }
    }
    >
      <View
        style={
          {
          width: 300,
          height: 300,
          backgroundColor: "white",
          borderRadius: 20,
          padding: 20,
          justifyContent: "space-around",
          alignItems: "center"
        }
      }
      >
        <Text
          style={
            {
            fontSize: 30,
            fontWeight: "bold"
          }
        }
        >
          Minha Estante
        </Text>

        <Text
          style={
            {
            fontSize: 16
          }
        }
        >
          Testando componentes
        </Text>

        <Button
          title="Clique aqui"
          onPress={() => alert("Funcionou")}
        />
      </View>


      <View
      style={
        {
          flex:1,
          backgroundColor: "#dcdcdc",
          justifyContent: "center",
          alignItems: "center",
        }
      }
      >
        <View
        style={
          {
          width: 300,
          height: 300,
          backgroundColor: "white",
          borderRadius: 20,
          padding: 20,
          justifyContent: "space-around",
          alignItems: "center"
          }
        }
      >
        <Text> texto2 </Text>
      </View>

    </View>
</View> 

  );
}
