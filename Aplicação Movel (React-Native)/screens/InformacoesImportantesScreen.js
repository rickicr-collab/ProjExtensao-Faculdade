import React from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';

export default function InformacoesImportantesScreen() {
  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Informações Importantes</Text>
      <Text style={styles.label}>
        Professores enviaram novidades e avisos importantes sobre o curso. Confira seu e-mail
        registrado na congregação!
      </Text>
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
    textAlign: 'center',
  },
});
