package nsr_clientesocket1_carros;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para exibir o relatório de veículos
 */
public class Nsr_ClienteRelatorio extends JPanel {
    private Nsr_SocketManager Nsr_socketManager;
    private JTable Nsr_tabelaVeiculos;
    private DefaultTableModel Nsr_modeloTabela;
    private JButton Nsr_atualizarButton;
    private JButton Nsr_excluirButton;
    private JButton Nsr_editarButton;
    
    /**
     * Construtor
     */
    public Nsr_ClienteRelatorio(Nsr_SocketManager Nsr_socketManager) {
        this.Nsr_socketManager = Nsr_socketManager;
        Nsr_initComponents();
    }
    
    /**
     * Inicializa os componentes da interface
     */
    private void Nsr_initComponents() {
        setLayout(new BorderLayout(5, 5));
        
        // Criar modelo de tabela
        Nsr_modeloTabela = new DefaultTableModel();
        Nsr_modeloTabela.addColumn("ID");
        Nsr_modeloTabela.addColumn("Placa");
        Nsr_modeloTabela.addColumn("Modelo");
        Nsr_modeloTabela.addColumn("Marca");
        Nsr_modeloTabela.addColumn("Ano");
        Nsr_modeloTabela.addColumn("Combustível");
        Nsr_modeloTabela.addColumn("Cor");
        Nsr_modeloTabela.addColumn("Quilometragem");
        Nsr_modeloTabela.addColumn("Categoria");
        Nsr_modeloTabela.addColumn("Disponível");
        Nsr_modeloTabela.addColumn("Valor Compra");
        Nsr_modeloTabela.addColumn("Valor Venda");
        
        // Criar tabela
        Nsr_tabelaVeiculos = new JTable(Nsr_modeloTabela);
        Nsr_tabelaVeiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane Nsr_scrollPane = new JScrollPane(Nsr_tabelaVeiculos);
        
        // Painel de botões
        JPanel Nsr_botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        Nsr_atualizarButton = new JButton("Atualizar Dados");
        Nsr_atualizarButton.addActionListener(Nsr_e -> Nsr_carregarDados());
        
        Nsr_editarButton = new JButton("Editar Selecionado");
        Nsr_editarButton.addActionListener(Nsr_e -> Nsr_editarVeiculoSelecionado());
        
        Nsr_excluirButton = new JButton("Excluir Selecionado");
        Nsr_excluirButton.addActionListener(Nsr_e -> Nsr_excluirVeiculoSelecionado());
        
        Nsr_botoesPanel.add(Nsr_atualizarButton);
        Nsr_botoesPanel.add(Nsr_editarButton);
        Nsr_botoesPanel.add(Nsr_excluirButton);
        
        // Adicionar componentes ao painel
        add(Nsr_scrollPane, BorderLayout.CENTER);
        add(Nsr_botoesPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Carrega os dados dos veículos na tabela
     */
    public void Nsr_carregarDados() {
        // Limpar tabela
        while (Nsr_modeloTabela.getRowCount() > 0) {
            Nsr_modeloTabela.removeRow(0);
        }
        
        // Obter lista de veículos
        List<Nsr_Carro> Nsr_veiculos = Nsr_socketManager.Nsr_getListaVeiculos();
        System.out.println("Veículos obtidos do SocketManager: " + Nsr_veiculos.size());
        
        // Preencher tabela
        for (Nsr_Carro Nsr_carro : Nsr_veiculos) {
            Object[] Nsr_rowData = {
                Nsr_carro.Nsr_getId(),
                Nsr_carro.Nsr_getPlaca(),
                Nsr_carro.Nsr_getModelo(),
                Nsr_carro.Nsr_getMarca(),
                Nsr_carro.Nsr_getAno(),
                Nsr_carro.Nsr_getCombustivel(),
                Nsr_carro.Nsr_getCor(),
                Nsr_carro.Nsr_getQuilometragem(),
                Nsr_carro.Nsr_getCategoria(),
                Nsr_carro.Nsr_isDisponivel() ? "Sim" : "Não",
                String.format("R$ %.2f", Nsr_carro.Nsr_getValorCompra()),
                String.format("R$ %.2f", Nsr_carro.Nsr_getValorVenda())
            };
            Nsr_modeloTabela.addRow(Nsr_rowData);
        }
    }
    
    /**
     * Edita o veículo selecionado na tabela
     */
    private void Nsr_editarVeiculoSelecionado() {
        int Nsr_selectedRow = Nsr_tabelaVeiculos.getSelectedRow();
        
        if (Nsr_selectedRow >= 0) {
            // Obter ID do veículo selecionado
            int Nsr_id = (int) Nsr_tabelaVeiculos.getValueAt(Nsr_selectedRow, 0);
            
            // Obter os dados completos do veículo
            Nsr_Carro Nsr_carro = Nsr_socketManager.Nsr_solicitarVeiculoPorId(Nsr_id);
            
            if (Nsr_carro != null) {
                // Aqui você poderia abrir um diálogo de edição ou chamar uma função no ClienteCadastro
                // Para este exemplo, vamos apenas exibir uma mensagem
                JOptionPane.showMessageDialog(this, 
                    "Função de edição para veículo ID " + Nsr_id + " deve ser implementada.",
                    "Editar Veículo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Não foi possível obter os dados do veículo selecionado.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo na tabela para editar.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Exclui o veículo selecionado na tabela
     */
    private void Nsr_excluirVeiculoSelecionado() {
        int Nsr_selectedRow = Nsr_tabelaVeiculos.getSelectedRow();
        
        if (Nsr_selectedRow >= 0) {
            // Obter ID do veículo selecionado
            int Nsr_id = (int) Nsr_tabelaVeiculos.getValueAt(Nsr_selectedRow, 0);
            
            // Confirmar exclusão
            int Nsr_opcao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o veículo ID " + Nsr_id + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            
            if (Nsr_opcao == JOptionPane.YES_OPTION) {
                // Solicitar exclusão ao servidor
                boolean Nsr_sucesso = Nsr_socketManager.Nsr_removerVeiculo(Nsr_id);
                
                if (Nsr_sucesso) {
                    JOptionPane.showMessageDialog(this,
                        "Veículo excluído com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Recarregar dados
                    Nsr_carregarDados();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Não foi possível excluir o veículo.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo na tabela para excluir.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}