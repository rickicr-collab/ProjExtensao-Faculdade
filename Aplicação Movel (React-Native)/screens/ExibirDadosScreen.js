import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function ExibirDadosScreen() {
  const [aluno, setAluno] = useState(null);

  useEffect(() => {
    const fetchAluno = async () => {
      const data = await AsyncStorage.getItem('aluno');
      setAluno(JSON.parse(data));
    };
    fetchAluno();
  }, []);

  if (!aluno) {
    return (
      <View style={styles.container}>
        <Text>Nenhum aluno cadastrado!</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Dados do Aluno</Text>
      <Text>Nome: {aluno.nome}</Text>
      <Text>Telefone: {aluno.telefone}</Text>
      <Text>Turno: {aluno.turno}</Text>
      <Text>Celular: {aluno.celular}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 10,
  },
});
