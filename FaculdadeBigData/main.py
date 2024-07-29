import pandas as pd
import os
import tkinter as tk
from tkinter import ttk, messagebox


# Função para obter o caminho do arquivo
def obter_caminho_arquivo(mes: str, ano: str) -> str:
    if not os.path.exists('inventario'):
        os.makedirs('inventario')
    return f'inventario/estoque_{mes}_{ano}.xlsx'


# Função para limpar os campos de entrada
def limpar_campos():
    nome.delete(0, tk.END)
    tipo.delete(0, tk.END)
    estoque_inicial.delete(0, tk.END)
    preco_unitario.delete(0, tk.END)
    quantidade_vendida.delete(0, tk.END)
    bebidas_faltantes_vencidas.delete(0, tk.END)
    mes_selecionado.set('Selecione o mês')
    ano_selecionado.set('2024')


# Função para inserir ou atualizar os dados no Excel
def atualizar_excel():
    dados = {
        'Nome': nome.get(),
        'Tipo': tipo.get(),
        'Estoque Inicial': estoque_inicial.get(),
        'Preço Unitário': preco_unitario.get(),
        'Quantidade Vendida': quantidade_vendida.get(),
        'Bebidas faltantes ou vencidas': bebidas_faltantes_vencidas.get(),
        'Mês': mes_selecionado.get(),
        'Ano': ano_selecionado.get()
    }

    if dados['Mês'] == 'Selecione o mês':
        messagebox.showwarning('Aviso', 'Por favor, selecione o mês.')
        return

    if not all(dados.values()):
        messagebox.showwarning('Aviso', 'Por favor, preencha todos os campos.')
        return

    df_novo = pd.DataFrame([dados])

    caminho_arquivo = obter_caminho_arquivo(dados['Mês'], dados['Ano'])
    if os.path.exists(caminho_arquivo):
        df_existente = pd.read_excel(caminho_arquivo)
        df_combinado = pd.concat([df_existente, df_novo], ignore_index=True)
    else:
        df_combinado = df_novo

    df_combinado.to_excel(caminho_arquivo, index=False)
    messagebox.showinfo('Sucesso', 'Dados inseridos/atualizados com sucesso!')
    atualizar_visualizacao(df_combinado)
    limpar_campos()


# Função para deletar um registro selecionado
def deletar_registro():
    selecionado = tree.selection()
    if not selecionado:
        messagebox.showwarning('Aviso', 'Nenhum registro selecionado.')
        return

    item = tree.item(selecionado)
    valores = item['values']

    mes = valores[6]
    ano = valores[7]
    caminho_arquivo = obter_caminho_arquivo(mes, ano)

    if os.path.exists(caminho_arquivo):
        df = pd.read_excel(caminho_arquivo)
        df = df.drop(df[(df['Nome'] == valores[0]) & (df['Tipo'] == valores[1])].index)
        df.to_excel(caminho_arquivo, index=False)
        atualizar_visualizacao(df)
        messagebox.showinfo('Sucesso', 'Registro deletado com sucesso!')
    else:
        messagebox.showerror('Erro', 'Registro não encontrado.')


# Função para abrir a janela de entrada de análise de dados
def abrir_janela_analise():
    janela_analise = tk.Toplevel(root)
    janela_analise.title("Análise de Dados")

    tk.Label(janela_analise, text="Mês").grid(row=0, column=0, padx=10, pady=5, sticky='w')
    meses = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro',
             'Novembro', 'Dezembro']
    mes_selecionado_analise = tk.StringVar(value='Janeiro')
    mes_menu_analise = ttk.Combobox(janela_analise, textvariable=mes_selecionado_analise, values=meses)
    mes_menu_analise.grid(row=0, column=1, padx=10, pady=5)

    tk.Label(janela_analise, text="Ano").grid(row=1, column=0, padx=10, pady=5, sticky='w')
    ano_selecionado_analise = tk.StringVar(value='2024')
    ano_entry_analise = tk.Entry(janela_analise, textvariable=ano_selecionado_analise)
    ano_entry_analise.grid(row=1, column=1, padx=10, pady=5)

    btn_analisar = tk.Button(janela_analise, text="Analisar",
                             command=lambda: analisar_dados(mes_selecionado_analise.get(),
                                                            ano_selecionado_analise.get(), janela_analise))
    btn_analisar.grid(row=2, column=0, columnspan=2, padx=10, pady=10)


# Função para analisar os dados
def analisar_dados(mes, ano, janela_analise):
    try:
        file_path = obter_caminho_arquivo(mes, ano)

        if not os.path.exists(file_path):
            messagebox.showerror('Erro', 'Planilha não encontrada.')
            return

        df = pd.read_excel(file_path)

        # Verifica se as colunas necessárias estão presentes
        required_columns = ['Estoque Inicial', 'Preço Unitário', 'Quantidade Vendida', 'Bebidas faltantes ou vencidas']
        if not all(col in df.columns for col in required_columns):
            messagebox.showerror('Erro', 'Colunas necessárias ausentes na planilha.')
            return

        # Converte os dados das colunas para float e faz a análise
        df['Estoque Inicial'] = pd.to_numeric(df['Estoque Inicial'], errors='coerce')
        df['Preço Unitário'] = pd.to_numeric(df['Preço Unitário'], errors='coerce')
        df['Quantidade Vendida'] = pd.to_numeric(df['Quantidade Vendida'], errors='coerce')
        df['Bebidas faltantes ou vencidas'] = pd.to_numeric(df['Bebidas faltantes ou vencidas'], errors='coerce')

        valor_investido = (df['Estoque Inicial'] * df['Preço Unitário']).sum()
        receita_total_vendas = (df['Quantidade Vendida'] * df['Preço Unitário']).sum()
        lucro_final = receita_total_vendas - valor_investido

        valores_perdidos = df['Bebidas faltantes ou vencidas'].sum()
        total_perdido = valores_perdidos * df['Preço Unitário'].mean()  # Valor perdido em reais

        messagebox.showinfo(
            'Análise de Dados',
            f'Valor Inicial Investido: R${valor_investido:.2f}\n'
            f'Receita Total das Vendas: R${receita_total_vendas:.2f}\n'
            f'Lucro Final das Vendas: R${lucro_final:.2f}\n'
            f'Valores Perdidos: R${valores_perdidos:.2f}\n'
            f'Total de Valores Perdidos: R${total_perdido:.2f}'
        )

        janela_analise.destroy()

    except Exception as e:
        messagebox.showerror('Erro', f'Ocorreu um erro durante a análise: {str(e)}')


# Função para atualizar a visualização dos dados
def atualizar_visualizacao(df):
    for row in tree.get_children():
        tree.delete(row)

    for _, row in df.iterrows():
        tree.insert('', 'end', values=list(row))


# Criação da janela principal
root = tk.Tk()
root.title("Sistema de Controle de Estoque")

# Campos de entrada
tk.Label(root, text="Nome").grid(row=0, column=0, padx=10, pady=5, sticky='w')
nome = tk.Entry(root)
nome.grid(row=0, column=1, padx=10, pady=5)

tk.Label(root, text="Tipo").grid(row=1, column=0, padx=10, pady=5, sticky='w')
tipo = tk.Entry(root)
tipo.grid(row=1, column=1, padx=10, pady=5)

tk.Label(root, text="Estoque Inicial").grid(row=2, column=0, padx=10, pady=5, sticky='w')
estoque_inicial = tk.Entry(root)
estoque_inicial.grid(row=2, column=1, padx=10, pady=5)

tk.Label(root, text="Preço Unitário").grid(row=3, column=0, padx=10, pady=5, sticky='w')
preco_unitario = tk.Entry(root)
preco_unitario.grid(row=3, column=1, padx=10, pady=5)

tk.Label(root, text="Quantidade Vendida").grid(row=4, column=0, padx=10, pady=5, sticky='w')
quantidade_vendida = tk.Entry(root)
quantidade_vendida.grid(row=4, column=1, padx=10, pady=5)

tk.Label(root, text="Bebidas Faltantes ou Vencidas").grid(row=5, column=0, padx=10, pady=5, sticky='w')
bebidas_faltantes_vencidas = tk.Entry(root)
bebidas_faltantes_vencidas.grid(row=5, column=1, padx=10, pady=5)

tk.Label(root, text="Mês").grid(row=6, column=0, padx=10, pady=5, sticky='w')
mes_selecionado = tk.StringVar(value='Selecione o mês')
meses = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro',
         'Novembro', 'Dezembro']
mes_menu = ttk.Combobox(root, textvariable=mes_selecionado, values=meses)
mes_menu.grid(row=6, column=1, padx=10, pady=5)

tk.Label(root, text="Ano").grid(row=7, column=0, padx=10, pady=5, sticky='w')
ano_selecionado = tk.StringVar(value='2024')
ano_entry = tk.Entry(root, textvariable=ano_selecionado)
ano_entry.grid(row=7, column=1, padx=10, pady=5)

# Botões de ação
btn_inserir = tk.Button(root, text="Inserir/Atualizar", command=atualizar_excel)
btn_inserir.grid(row=8, column=0, padx=10, pady=10, sticky='w')

btn_deletar = tk.Button(root, text="Deletar", command=deletar_registro)
btn_deletar.grid(row=8, column=1, padx=10, pady=10, sticky='w')

btn_analisar = tk.Button(root, text="Analisar Dados", command=abrir_janela_analise)
btn_analisar.grid(row=8, column=2, padx=10, pady=10, sticky='w')

# Área de visualização
tk.Label(root, text="Dados Inseridos").grid(row=9, column=0, columnspan=3, padx=10, pady=5, sticky='w')

columns = ['Nome', 'Tipo', 'Estoque Inicial', 'Preço Unitário', 'Quantidade Vendida', 'Bebidas faltantes ou vencidas',
           'Mês', 'Ano']
tree = ttk.Treeview(root, columns=columns, show='headings')
tree.grid(row=10, column=0, columnspan=3, padx=10, pady=5)

for col in columns:
    tree.heading(col, text=col)
    tree.column(col, width=100)

# Inicializa a visualização com dados do mês e ano atuais
atualizar_visualizacao(pd.DataFrame(columns=columns))

root.mainloop()
