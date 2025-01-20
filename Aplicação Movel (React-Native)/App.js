import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import HomeScreen from './screens/HomeScreen';
import LocalInfoScreen from './screens/LocalInfoScreen'; // Importa a tela de informações do local
import CadastroScreen from './screens/CadastroScreen';
import MeusDadosScreen from './screens/MeusDadosScreen';
import InformacoesImportantesScreen from './screens/InformacoesImportantesScreen';

const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Cadastro" component={CadastroScreen} />
        <Stack.Screen name="MeusDados" component={MeusDadosScreen} />
        <Stack.Screen name="InformacoesImportantes" component={InformacoesImportantesScreen} />
        <Stack.Screen
          name="LocalInfo"
          component={LocalInfoScreen}
          options={{ title: 'Informações do Local' }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
