# 📌 Trabalho - Sistema Distribuído  
**Curso:** Análise e Desenvolvimento de Sistemas – 5° Período  
**Disciplina:** Sistemas Distribuídos  
**Professor:** Cícero Lima Costa  
**Tema da Aluna:** Cadastro de Carros 🚗  

---

## 📄 Descrição
Este repositório contém o desenvolvimento do trabalho/prova da disciplina **Sistemas Distribuídos**, cujo objetivo é implementar, em **Java Sockets** e **Java Web**, um sistema de **Cadastro de Carros**, seguindo a arquitetura especificada pelo professor e apresentada na Figura 1 da prova.

O sistema permite a comunicação entre **dois computadores distintos**, sincronização de dados entre servidores e integração entre cliente desktop e página web.

---

## 📂 Estrutura do Repositório

```
├── 📂 TrabalhoProva       
│   ├── 📂 Nsr_Carros                     # Código do módulo principal do cadastro de carros
│   ├── 📂 Nsr_ClienteSocket1_Carros      # Cliente desktop conectado via socket
│   ├── 📂 Nsr_Servidor1_Carros           # Servidor de comunicação e controle
│   ├── 📄 cadastro_carros                # Arquivo de dados persistente
```


---

## ⚙️ Funcionalidades Implementadas
- **Cadastro de Carros** (salvar, consultar, editar e excluir).
- **Comunicação Cliente-Servidor** via **Java Sockets**.
- **Sincronização** entre os servidores (unilateral e bilateral).
- **Atualização em tempo real** dos dados na janela do cliente desktop.
- **Interface Web** para manipulação dos dados.
- **Integração** entre alterações feitas na página web e no cliente desktop.

---

## 🎯 Requisitos da Prova
- Variáveis e métodos devem iniciar com as iniciais do nome do aluno.  
  - Exemplo para Natália Salete Rodrigues: `nsr_Variavel`, `Nsr_Metodo()`.
- Comunicação entre dois computadores reais.
- Sincronização completa entre servidores e clientes.
- Persistência de dados em arquivo.
- Funcionalidades disponíveis tanto no cliente desktop quanto na página web.

---

## 📝 Distribuição de Pontos
| Critério | Pontos |
|----------|--------|
| Sincronização entre servidores (unilateral e bilateral) | **13 pts** |
| Cliente desktop sincronizado em tempo real | **8 pts** |
| Conexão de clientes a qualquer servidor | **3 pts** |
| Sincronização das alterações da página web com o desktop | **6 pts** |
| **Total** | **30 pts** |

---

## 🚀 Tecnologias Utilizadas
- **Java SE** (Sockets, Threads, IO)
- **Java Web** (Servlets, JSP)
- **HTML / CSS**
- **Arquitetura Cliente-Servidor**
- **Persistência em Arquivo**

---

## 📎 Documento da Prova
📄 [Prova1-SistDistribuidos.pdf](https://github.com/user-attachments/files/20266534/Prova1-SistDistribuidos.pdf)

---

## 📜 Licença
Este projeto está licenciado sob os termos da **Licença MIT**.  
Você pode utilizar, modificar e distribuir este código livremente, desde que mantenha os créditos originais.  
📄 [Clique aqui para ler a Licença MIT](https://opensource.org/licenses/MIT)  
