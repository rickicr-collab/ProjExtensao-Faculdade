# Aplicativo Móvel: Curso de Música Florescer

## Descrição
Este é um aplicativo móvel desenvolvido para auxiliar no cadastro de alunos do **Curso de Música Florescer**, realizado pela **Congregação Florescer**. O objetivo é proporcionar uma experiência simples e eficiente para organizar as informações dos alunos e compartilhar dados relevantes do curso.

## Principais Funcionalidades
- **Tela Inicial**:
  - Botão para cadastrar novos alunos.
  - Botão para visualizar informações sobre o local do curso.
  - Botão para acessar os dados dos alunos cadastrados.
  - Botão com informações importantes dos professores e avisos sobre o curso.

- **Cadastro de Alunos**:
  - Campos para inserir nome, telefone e turno (manhã ou noite).
  - Validação dos dados inseridos.
  - Envio das informações cadastradas por e-mail para a congregação.

- **Informações do Local**:
  - Nome: Igreja Congregacional Batista Renovada.
  - Endereço completo.
  - CNPJ.

- **Meus Dados**:
  - Exibição dos dados cadastrados do aluno, incluindo o professor responsável de acordo com o turno:
    - Turno Manhã: **Tiago Campos**.
    - Turno Noite: **Rubens Araújo**.

- **Avisos dos Professores**:
  - Informativos enviados por e-mail para a congregação com novidades e atualizações do curso.

## Configuração do Projeto
### Dependências
O projeto utiliza as seguintes dependências principais:
- `react-navigation`: Para gerenciamento de rotas.
- `expo-mail-composer`: Para envio de e-mails.
- `react-native`: Base do aplicativo móvel.

### Configuração de Permissões
Certifique-se de configurar as permissões no arquivo **app.json** para envio de e-mails com o Expo:
```json
"android": {
  "permissions": [
    "WRITE_EXTERNAL_STORAGE",
    "READ_EXTERNAL_STORAGE"
  ]
}
```

### Estrutura do Projeto
- **screens/**: Contém as telas principais do aplicativo (Tela Inicial, Cadastro, Informações do Local, Meus Dados).
- **routes/**: Gerenciamento de rotas entre as telas.

## Como Executar
1. Clone o repositório do projeto.
2. Instale as dependências com `npm install`.
3. Inicie o aplicativo com `expo start`.
4. Escaneie o QR Code no terminal usando o aplicativo Expo Go.


