import React, { useState } from 'react';
import { View, Text, Button, StyleSheet, Alert, ScrollView } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function MeusDadosScreen() {
  const [dados, setDados] = useState(null);

  const carregarDados = async () => {
    try {
      const dadosSalvos = await AsyncStorage.getItem('dadosAluno');
      if (dadosSalvos) {
        setDados(JSON.parse(dadosSalvos));
      } else {
        Alert.alert('Informação', 'Nenhum dado cadastrado encontrado.');
      }
    } catch (error) {
      Alert.alert('Erro', 'Não foi possível carregar os dados.');
    }
  };

  const obterProfessor = (turno) => {
    if (turno.toLowerCase() === 'manhã') {
      return 'Tiago Campos';
    }
    if (turno.toLowerCase() === 'noite') {
      return 'Rubens Araújo';
    }
    return 'Indefinido';
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Meus Dados</Text>
      {dados ? (
        <View>
          <Text style={styles.label}>Nome: {dados.nome}</Text>
          <Text style={styles.label}>Telefone: {dados.telefone}</Text>
          <Text style={styles.label}>Turno: {dados.turno}</Text>
          <Text style={styles.label}>Professor: {obterProfessor(dados.turno)}</Text>
        </View>
      ) : (
        <Text style={styles.label}>Carregue os dados para exibir informações.</Text>
      )}
      <View style={styles.buttonSpacing}>
        <Button title="Carregar Dados" onPress={carregarDados} />
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#f5f5f5',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 20,
  },
  label: {
    fontSize: 18,
    color: '#555',
    marginBottom: 10,
  },
  buttonSpacing: {
    marginBottom: 15,
    width: '80%',
  },
});
