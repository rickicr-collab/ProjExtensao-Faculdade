from flask import Flask, request, render_template, redirect, url_for, send_file
import sqlite3
import pandas as pd
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
from reportlab.lib.units import inch
import requests
import os

app = Flask(__name__)

DATABASE = 'alunos.db'
THINGBOARD_URL = 'http://192.168.1.100:8080/api/v1/abc123/telemetry'

def send_data_to_thingboard(data):
    headers = {'Content-Type': 'application/json'}
    try:
        response = requests.post(THINGBOARD_URL, json=data, headers=headers)
        response.raise_for_status()  # Levanta um erro se o status code não for 200
        print('Dados enviados para ThingBoard com sucesso!')
    except requests.exceptions.HTTPError as errh:
        print('Erro HTTP:', errh)
    except requests.exceptions.ConnectionError as errc:
        print('Erro de Conexão:', errc)
    except requests.exceptions.Timeout as errt:
        print('Erro de Timeout:', errt)
    except requests.exceptions.RequestException as err:
        print('Erro:', err)



def get_db_connection():
    conn = sqlite3.connect(DATABASE)
    conn.row_factory = sqlite3.Row
    return conn


def init_db():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute('''
    CREATE TABLE IF NOT EXISTS alunos (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nome TEXT NOT NULL,
        idade INTEGER NOT NULL,
        horario TEXT NOT NULL,
        data_aulas TEXT NOT NULL,
        professor TEXT NOT NULL
    )
    ''')
    conn.commit()
    conn.close()


init_db()


def send_data_to_thingboard(data):
    headers = {'Content-Type': 'application/json'}
    response = requests.post(THINGBOARD_URL, json=data, headers=headers)
    if response.status_code == 200:
        print('Dados enviados para ThingBoard com sucesso!')
    else:
        print('Falha ao enviar dados para ThingBoard:', response.status_code, response.text)


@app.route('/')
def index():
    conn = get_db_connection()
    alunos = conn.execute('SELECT * FROM alunos').fetchall()
    conn.close()
    return render_template('index.html', alunos=alunos)


@app.route('/add', methods=['POST'])
def add():
    nome = request.form['nome']
    idade = request.form['idade']
    horario = request.form['horario']
    data_aulas = request.form['data_aulas']

    professor = "Danilo Paes Sampaio" if "8:00 a 9:00" in horario else "Bruno Araújo"

    conn = get_db_connection()
    conn.execute('INSERT INTO alunos (nome, idade, horario, data_aulas, professor) VALUES (?, ?, ?, ?, ?)',
                 (nome, idade, horario, data_aulas, professor))
    conn.commit()
    conn.close()

    # Prepare data for ThingBoard
    data = {
        'nome': nome,
        'idade': idade,
        'horario': horario,
        'data_aulas': data_aulas,
        'professor': professor
    }
    send_data_to_thingboard(data)
    return redirect(url_for('index'))


@app.route('/update/<int:id>', methods=['POST'])
def update(id):
    nome = request.form['nome']
    idade = request.form['idade']
    horario = request.form['horario']
    data_aulas = request.form['data_aulas']

    professor = "Danilo Paes Sampaio" if "8:00 a 9:00" in horario else "Bruno Araújo"

    conn = get_db_connection()
    conn.execute('UPDATE alunos SET nome = ?, idade = ?, horario = ?, data_aulas = ?, professor = ? WHERE id = ?',
                 (nome, idade, horario, data_aulas, professor, id))
    conn.commit()
    conn.close()
    return redirect(url_for('index'))


@app.route('/delete/<int:id>', methods=['POST'])
def delete(id):
    conn = get_db_connection()
    conn.execute('DELETE FROM alunos WHERE id = ?', (id,))
    conn.commit()
    conn.close()
    return redirect(url_for('index'))


@app.route('/export_csv')
def export_csv():
    conn = get_db_connection()
    df = pd.read_sql_query("SELECT * FROM alunos", conn)
    df.to_csv('exported_data.csv', index=False)
    conn.close()
    return send_file('exported_data.csv', as_attachment=True)


@app.route('/generate_pdf')
def generate_pdf():
    conn = get_db_connection()
    df = pd.read_sql_query("SELECT * FROM alunos", conn)

    c = canvas.Canvas("relatorio.pdf", pagesize=letter)
    width, height = letter

    # Header
    c.setFont("Helvetica-Bold", 14)
    c.drawString(50, height - 50, "Projeto Musical Igreja Congregacional Batista Renovada")
    c.setFont("Helvetica", 12)
    c.drawString(50, height - 70, "Igreja Congregacional Batista Renovada")
    c.drawString(50, height - 90, "CNPJ : 07.255.776/0001-26")
    c.drawString(50, height - 110,
                 "Endereço: Rua Airton Senna da Silva, 151, Piedade, Jaboatão dos Guararapes–PE, 54430-050")
    c.drawString(50, height - 130, "Website: http://www.instagram.com/IgrejaBatistaRenovada/")
    c.drawString(50, height - 170, "Segue lista de Alunos:")

    # Table Headers
    c.setFont("Helvetica-Bold", 10)
    header_y = height - 200
    c.drawString(40, header_y, "ID")
    c.drawString(80, header_y, "Nome")
    c.drawString(200, header_y, "Idade")
    c.drawString(260, header_y, "Horário")
    c.drawString(370, header_y, "Data das Aulas")
    c.drawString(470, header_y, "Professor")

    # Table Data
    y = header_y - 20
    c.setFont("Helvetica", 10)
    row_height = 40
    for index, row in df.iterrows():
        c.drawString(40, y, str(row['id']))
        c.drawString(80, y, row['nome'])
        c.drawString(200, y, str(row['idade']))
        c.drawString(240, y, row['horario'])
        c.drawString(370, y, row['data_aulas'])
        c.drawString(470, y, row['professor'])
        y -= row_height
        if y < 40:  # If we're too close to the bottom of the page, start a new page
            c.showPage()
            y = height - 40
            # Reprint header
            c.setFont("Helvetica-Bold", 10)
            c.drawString(40, y, "ID")
            c.drawString(80, y, "Nome")
            c.drawString(200, y, "Idade")
            c.drawString(240, y, "Horário")
            c.drawString(340, y, "Data das Aulas")
            c.drawString(440, y, "Professor")
            y -= row_height

    c.save()
    conn.close()
    return send_file('relatorio.pdf', as_attachment=True)


if __name__ == '__main__':
    app.run(debug=True)
