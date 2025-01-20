// LocalInfoScreen.js
import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';

export default function LocalInfoScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Informações do Local</Text>
      <Text style={styles.info}>Nome: Congregação Florescer</Text>
      <Text style={styles.info}>CNPJ: 86.268.887/0001-19</Text>
      <Text style={styles.info}>Endereço: Av Ayrton Senna Nº 151 - Jaboatão dos Guararapes</Text>
      <Text style={styles.info}>Bairro: Candeias</Text>
      <Text style={styles.info}>CEP: 54430-070</Text>
      <Text style={styles.info}>Estado: Pernambuco</Text>
      <Button title="Voltar" onPress={() => navigation.goBack()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  info: {
    fontSize: 18,
    marginBottom: 10,
  },
});
