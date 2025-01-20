package principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestaoAlunos {

    private JFrame frame;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private List<Aluno> alunos;
    private static final String FILE_NAME = "alunos.csv";
    private static final String[] COLUMN_NAMES = {"Nome", "Idade", "Horário", "Língua", "Dias Disponíveis", "Turma", "Professor", "Ativo"};

    public GestaoAlunos() {
        alunos = new ArrayList<>();
        frame = new JFrame("Gestão de Alunos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(995, 700);
        frame.setLayout(new BorderLayout());

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField(20);
        JLabel idadeLabel = new JLabel("Idade:");
        JTextField idadeField = new JTextField(5);
        JLabel horarioLabel = new JLabel("Horário:");
        JComboBox<String> horarioComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "Manhã [8:00 a 9:30]", "Noite [19:00 a 20:30]"});
        JLabel linguaLabel = new JLabel("Língua:");
        JComboBox<String> linguaComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "Inglês", "Espanhol"});
        JLabel diasLabel = new JLabel("Dias Disponíveis:");
        JComboBox<String> diasComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "Segunda", "Quarta", "Sexta"});
        JLabel turmaLabel = new JLabel("Turma:");
        JComboBox<String> turmaComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "A", "B"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nomeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(idadeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idadeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(horarioLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(horarioComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(linguaLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(linguaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(diasLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(diasComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(turmaLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(turmaComboBox, gbc);

        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                int idade = Integer.parseInt(idadeField.getText());
                String horario = (String) horarioComboBox.getSelectedItem();
                String lingua = (String) linguaComboBox.getSelectedItem();
                String dias = (String) diasComboBox.getSelectedItem();
                String turma = (String) turmaComboBox.getSelectedItem();

                // Verificar se o usuário selecionou uma opção válida
                if ("Escolha a opção aqui".equals(horario) || "Escolha a opção aqui".equals(lingua) ||
                        "Escolha a opção aqui".equals(dias) || "Escolha a opção aqui".equals(turma)) {
                    JOptionPane.showMessageDialog(frame, "Por favor, selecione todas as opções corretamente.");
                    return;
                }

                String professor = getProfessor(turma, lingua);
                Aluno aluno = new Aluno(nome, idade, horario, lingua, dias, turma, professor);
                alunos.add(aluno);
                tableModel.addRow(new Object[]{nome, idade, horario, lingua, dias, turma, professor, aluno.isAtivo() ? "Ativo" : "Inativo"});
            }
        });

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarAluno();
            }
        });

        JButton excluirButton = new JButton("Excluir");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirAluno();
            }
        });

        JButton salvarButton = new JButton("Salvar CSV");
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarEmCSV();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(adicionarButton);
        buttonPanel.add(atualizarButton);
        buttonPanel.add(excluirButton);
        buttonPanel.add(salvarButton);

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
        tabela = new JTable(tableModel);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(1100, 400));

        // Ajustar largura das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(200); // Nome
        tabela.getColumnModel().getColumn(1).setPreferredWidth(50);  // Idade
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150); // Horário
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100); // Língua
        tabela.getColumnModel().getColumn(4).setPreferredWidth(150); // Dias Disponíveis
        tabela.getColumnModel().getColumn(5).setPreferredWidth(50);  // Turma
        tabela.getColumnModel().getColumn(6).setPreferredWidth(200); // Professor
        tabela.getColumnModel().getColumn(7).setPreferredWidth(50);  // Ativo

        frame.add(formPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        carregarDeCSV();
        frame.setVisible(true);
    }

    private String getProfessor(String turma, String lingua) {
        if ("Inglês".equals(lingua)) {
            return "Matheus Silva Pereira";
        } else if ("Espanhol".equals(lingua)) {
            return "Sandra Maria de Albuquerque";
        }
        return "";
    }

    private void atualizarAluno() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Nenhum aluno selecionado para atualização.");
            return;
        }

        Aluno alunoSelecionado = alunos.get(selectedRow);

        JDialog updateDialog = new JDialog(frame, "Atualizar Aluno", true);
        updateDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nomeField = new JTextField(alunoSelecionado.getNome(), 20);
        JTextField idadeField = new JTextField(String.valueOf(alunoSelecionado.getIdade()), 5);
        JComboBox<String> horarioComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "Manhã [8:00 a 9:30]", "Noite [19:00 a 20:30]"});
        horarioComboBox.setSelectedItem(alunoSelecionado.getHorario());

        JComboBox<String> linguaComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "Inglês", "Espanhol"});
        linguaComboBox.setSelectedItem(alunoSelecionado.getLingua());

        JComboBox<String> diasComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "Segunda", "Quarta", "Sexta"});
        diasComboBox.setSelectedItem(alunoSelecionado.getDiasDisponiveis());

        JComboBox<String> turmaComboBox = new JComboBox<>(new String[]{"Escolha a opção aqui", "A", "B"});
        turmaComboBox.setSelectedItem(alunoSelecionado.getTurma());

        JCheckBox ativoCheckBox = new JCheckBox("Ativo", alunoSelecionado.isAtivo());

        gbc.gridx = 0;
        gbc.gridy = 0;
        updateDialog.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        updateDialog.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        updateDialog.add(new JLabel("Idade:"), gbc);
        gbc.gridx = 1;
        updateDialog.add(idadeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        updateDialog.add(new JLabel("Horário:"), gbc);
        gbc.gridx = 1;
        updateDialog.add(horarioComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        updateDialog.add(new JLabel("Língua:"), gbc);
        gbc.gridx = 1;
        updateDialog.add(linguaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        updateDialog.add(new JLabel("Dias Disponíveis:"), gbc);
        gbc.gridx = 1;
        updateDialog.add(diasComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        updateDialog.add(new JLabel("Turma:"), gbc);
        gbc.gridx = 1;
        updateDialog.add(turmaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        updateDialog.add(new JLabel("Ativo:"), gbc);
        gbc.gridx = 1;
        updateDialog.add(ativoCheckBox, gbc);

        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alunoSelecionado.setNome(nomeField.getText());
                alunoSelecionado.setIdade(Integer.parseInt(idadeField.getText()));
                alunoSelecionado.setHorario((String) horarioComboBox.getSelectedItem());
                alunoSelecionado.setLingua((String) linguaComboBox.getSelectedItem());
                alunoSelecionado.setDiasDisponiveis((String) diasComboBox.getSelectedItem());
                alunoSelecionado.setTurma((String) turmaComboBox.getSelectedItem());
                alunoSelecionado.setAtivo(ativoCheckBox.isSelected());

                tableModel.setValueAt(alunoSelecionado.getNome(), selectedRow, 0);
                tableModel.setValueAt(alunoSelecionado.getIdade(), selectedRow, 1);
                tableModel.setValueAt(alunoSelecionado.getHorario(), selectedRow, 2);
                tableModel.setValueAt(alunoSelecionado.getLingua(), selectedRow, 3);
                tableModel.setValueAt(alunoSelecionado.getDiasDisponiveis(), selectedRow, 4);
                tableModel.setValueAt(alunoSelecionado.getTurma(), selectedRow, 5);
                tableModel.setValueAt(alunoSelecionado.getProfessor(), selectedRow, 6);
                tableModel.setValueAt(alunoSelecionado.isAtivo() ? "Ativo" : "Inativo", selectedRow, 7);

                updateDialog.dispose();
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 7;
        updateDialog.add(salvarButton, gbc);

        updateDialog.pack();
        updateDialog.setVisible(true);
    }

    private void excluirAluno() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Nenhum aluno selecionado para exclusão.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Você tem certeza que deseja excluir este aluno?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            alunos.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        }
    }

    private void salvarEmCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Aluno aluno : alunos) {
                pw.println(aluno.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDeCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                Aluno aluno = new Aluno(campos[0], Integer.parseInt(campos[1]), campos[2], campos[3], campos[4], campos[5], campos[6], Boolean.parseBoolean(campos[7]));
                alunos.add(aluno);
                tableModel.addRow(new Object[]{campos[0], campos[1], campos[2], campos[3], campos[4], campos[5], campos[6], campos[7].equals("true") ? "Ativo" : "Inativo"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestaoAlunos();
            }
        });
    }
}

class Aluno {
    private String nome;
    private int idade;
    private String horario;
    private String lingua;
    private String diasDisponiveis;
    private String turma;
    private String professor;
    private boolean ativo;

    public Aluno(String nome, int idade, String horario, String lingua, String diasDisponiveis, String turma, String professor) {
        this.nome = nome;
        this.idade = idade;
        this.horario = horario;
        this.lingua = lingua;
        this.diasDisponiveis = diasDisponiveis;
        this.turma = turma;
        this.professor = professor;
        this.ativo = true;
    }

    public Aluno(String nome, int idade, String horario, String lingua, String diasDisponiveis, String turma, String professor, boolean ativo) {
        this(nome, idade, horario, lingua, diasDisponiveis, turma, professor);
        this.ativo = ativo;
    }

    // Getters and Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public String getDiasDisponiveis() {
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(String diasDisponiveis) {
        this.diasDisponiveis = diasDisponiveis;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getProfessor() {
        return professor;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String toCSV() {
        return String.join(",", nome, String.valueOf(idade), horario, lingua, diasDisponiveis, turma, professor, String.valueOf(ativo));
    }
}
