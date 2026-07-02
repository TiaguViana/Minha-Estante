// Arquivo: src/services/firebaseConfig.js
import { initializeApp, getApps, getApp } from 'firebase/app';
import { initializeAuth, getAuth, getReactNativePersistence } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';


const firebaseConfig = {
  apiKey: "AIzaSyD9_4pdzauLbLfnWb_gJeOxdF4P1GRC1VE",
  authDomain: "minha-estante-b5289.firebaseapp.com",
  projectId: "minha-estante-b5289",
  storageBucket: "minha-estante-b5289.firebasestorage.app",
  messagingSenderId: "862743251660",
  appId: "1:862743251660:web:74efcd99aae45226e15069",
};

// Evita inicializar o app duas vezes (acontece com hot reload do Expo)
const app = getApps().length ? getApp() : initializeApp(firebaseConfig);

// O Auth precisa de configuração diferente em cada ambiente:
// - Na web, getAuth() já usa o localStorage do navegador pra persistência.
// - No nativo (iOS/Android), é preciso indicar explicitamente o AsyncStorage,
//   senão o login não persiste entre uma abertura do app e outra.
let auth;
if (Platform.OS === 'web') {
  auth = getAuth(app);
} else {
  try {
    auth = initializeAuth(app, {
      persistence: getReactNativePersistence(AsyncStorage),
    });
  } catch (e) {
    // Em hot reload o initializeAuth pode já ter rodado antes; nesse caso
    // só pega a instância existente em vez de quebrar o app.
    auth = getAuth(app);
  }
}

export { auth };
export const db = getFirestore(app);
export default app;