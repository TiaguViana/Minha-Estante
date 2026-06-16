import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";

import SplashScreen from "../screens/SplashScreen";
import LoginScreen from "../screens/LoginScreen";
import DashboardScreen from "../screens/DashboardScreen";

//tela de teste//
import PlaygroundScreen from "../screens/PlaygroundScreen";


const Stack = createNativeStackNavigator();

export default function AppNavigator(){

return(

<NavigationContainer>

<Stack.Navigator
initialRouteName="Login"
screenOptions={{
headerShown:false
}}
>

<Stack.Screen
   name="Playground"
   component={PlaygroundScreen}
/>

<Stack.Screen
name="Splash"
component={SplashScreen}
/>

<Stack.Screen
name="Login"
component={LoginScreen}
/>

<Stack.Screen
name="Dashboard"
component={DashboardScreen}
/>

</Stack.Navigator>

</NavigationContainer>

)

}