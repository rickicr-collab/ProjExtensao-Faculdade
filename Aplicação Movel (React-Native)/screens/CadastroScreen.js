import React, { useState } from 'react';
import { View, TextInput, Button, StyleSheet, Alert, Text } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as MailComposer from 'expo-mail-composer';

export default function CadastroScreen({ navigation }) {
  const [nome, setNome] = useState('');
  const [telefone, setTelefone] = useState('');
  const [turno, setTurno] = useState('');

  const validarDados = () => {
    if (!nome.trim()) {
      Alert.alert('Erro', 'O campo Nome não pode estar vazio.');
      return false;
    }
    if (!/^[0-9]{10,11}$/.test(telefone)) {
      Alert.alert('Erro', 'Telefone inválido. Deve conter apenas números com 10 ou 11 dígitos.');
      return false;
    }
    if (turno.toLowerCase() !== 'manhã' && turno.toLowerCase() !== 'noite') {
      Alert.alert('Erro', 'Turno inválido. Escolha entre "Manhã" ou "Noite".');
      return false;
    }
    return true;
  };

  const salvarDadosLocalmente = async () => {
    try {
      const dados = { nome, telefone, turno };
      await AsyncStorage.setItem('dadosAluno', JSON.stringify(dados));
      Alert.alert('Sucesso', 'Dados salvos localmente!');
    } catch (error) {
      Alert.alert('Erro', 'Não foi possível salvar os dados localmente.');
    }
  };

  const enviarEmail = () => {
    if (!validarDados()) {
      return;
    }

    const emailBody = `Segue os dados para cadastro no curso de música abaixo:\n\nNome: ${nome}\nTelefone: ${telefone}\nTurno: ${turno}\n\nAtenciosamente.`;
    MailComposer.composeAsync({
      recipients: ['congregacaoflorescer@gmail.com'],
      subject: 'Dados de Cadastro - Curso Música Florescer',
      body: emailBody,
    })
      .then((result) => {
        if (result.status === 'sent') {
          Alert.alert('Sucesso', 'E-mail enviado com sucesso!');
          salvarDadosLocalmente();
        } else {
          Alert.alert('Ação Cancelada', 'Envio de e-mail cancelado.');
        }
      })
      .catch((error) => {
        Alert.alert('Erro', 'Não foi possível enviar o e-mail.');
      });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Cadastro de Aluno</Text>
      <TextInput
        style={styles.input}
        placeholder="Nome do aluno"
        value={nome}
        onChangeText={setNome}
      />
      <TextInput
        style={styles.input}
        placeholder="Telefone"
        value={telefone}
        onChangeText={setTelefone}
        keyboardType="phone-pad"
      />
      <TextInput
        style={styles.input}
        placeholder="Turno (Manhã ou Noite)"
        value={turno}
        onChangeText={setTurno}
      />
      <View style={styles.buttonSpacing}>
        <Button title="Cadastrar e Enviar E-mail" onPress={enviarEmail} />
      </View>
      <View style={styles.buttonSpacing}>
        <Button title="Voltar" onPress={() => navigation.goBack()} />
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
    backgroundColor: '#f5f5f5',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 20,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 5,
    padding: 10,
    marginBottom: 15,
    width: '80%',
    backgroundColor: '#fff',
  },
  buttonSpacing: {
    marginBottom: 15,
    width: '80%',
  },
});
