import React from 'react';
import { View, Button, StyleSheet, Text } from 'react-native';

export default function HomeScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Igreja Congregacional Batista Renovada</Text>
      <Text style={styles.subtitle}>Curso Música Florescer</Text>

      <View style={styles.buttonSpacing}>
        <Button title="Cadastrar Aluno" onPress={() => navigation.navigate('Cadastro')} />
      </View>

      <View style={styles.buttonSpacing}>
        <Button title="Meus Dados" onPress={() => navigation.navigate('MeusDados')} />
      </View>

      <View style={styles.buttonSpacing}>
        <Button
          title="Informações do Local"
          onPress={() => navigation.navigate('LocalInfo')}
        />
      </View>

      <View style={styles.buttonSpacing}>
        <Button
          title="Informações Importantes"
          onPress={() => navigation.navigate('InformacoesImportantes')}
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#f0f8ff',
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#4682b4',
    marginBottom: 10,
  },
  subtitle: {
    fontSize: 20,
    fontWeight: '600',
    color: '#5f9ea0',
    marginBottom: 20,
  },
  buttonSpacing: {
    marginBottom: 15,
    width: '80%',
  },
});
