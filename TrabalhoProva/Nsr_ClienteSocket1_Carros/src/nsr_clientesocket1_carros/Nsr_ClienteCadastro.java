package nsr_clientesocket1_carros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Painel para cadastro de veículos
 */
public class Nsr_ClienteCadastro extends JPanel {
    private Nsr_SocketManager socketManager;
    
    // Campos de formulário
    private JTextField idField;
    private JTextField placaField;
    private JTextField modeloField;
    private JTextField marcaField;
    private JTextField anoField;
    private JComboBox<String> combustivelCombo;
    private JTextField corField;
    private JTextField quilometragemField;
    private JComboBox<String> categoriaCombo;
    private JCheckBox disponivelCheck;
    private JTextField valorCompraField;
    private JTextField valorVendaField;
    
    // Botões
    private JButton salvarButton;
    private JButton limparButton;
    private JButton buscarButton;
    
    /**
     * Construtor
     */
    public Nsr_ClienteCadastro(Nsr_SocketManager socketManager) {
        this.socketManager = socketManager;
        initComponents();
    }
    
    /**
     * Inicializa os componentes da interface
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID
        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);
        
        // Placa
        formPanel.add(new JLabel("Placa:"));
        placaField = new JTextField();
        formPanel.add(placaField);
        
        // Modelo
        formPanel.add(new JLabel("Modelo:"));
        modeloField = new JTextField();
        formPanel.add(modeloField);
        
        // Marca
        formPanel.add(new JLabel("Marca:"));
        marcaField = new JTextField();
        formPanel.add(marcaField);
        
        // Ano
        formPanel.add(new JLabel("Ano:"));
        anoField = new JTextField();
        formPanel.add(anoField);
        
        // Combustível
        formPanel.add(new JLabel("Combustível:"));
        combustivelCombo = new JComboBox<>(new String[] {
            "GASOLINA", "ALCOOL", "FLEX", "DIESEL", "ELETRICO", "HIBRIDO"
        });
        formPanel.add(combustivelCombo);
        
        // Cor
        formPanel.add(new JLabel("Cor:"));
        corField = new JTextField();
        formPanel.add(corField);
        
        // Quilometragem
        formPanel.add(new JLabel("Quilometragem:"));
        quilometragemField = new JTextField();
        formPanel.add(quilometragemField);
        
        // Categoria
        formPanel.add(new JLabel("Categoria:"));
        categoriaCombo = new JComboBox<>(new String[] {
            "SEDAN", "HATCHBACK", "SUV", "PICKUP", "ESPORTIVO", "UTILITARIO"
        });
        formPanel.add(categoriaCombo);
        
        // Disponível
        formPanel.add(new JLabel("Disponível:"));
        disponivelCheck = new JCheckBox();
        disponivelCheck.setSelected(true);
        formPanel.add(disponivelCheck);
        
        // Valor de Compra
        formPanel.add(new JLabel("Valor de Compra (R$):"));
        valorCompraField = new JTextField();
        formPanel.add(valorCompraField);
        
        // Valor de Venda
        formPanel.add(new JLabel("Valor de Venda (R$):"));
        valorVendaField = new JTextField();
        formPanel.add(valorVendaField);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        buscarButton = new JButton("Buscar por ID");
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarVeiculo();
            }
        });
        botoesPanel.add(buscarButton);
        
        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarVeiculo();
            }
        });
        botoesPanel.add(salvarButton);
        
        limparButton = new JButton("Limpar");
        limparButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparFormulario();
            }
        });
        botoesPanel.add(limparButton);
        
        // Adicionar ao painel principal
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Busca um veículo pelo ID
     */
    private void buscarVeiculo() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            Nsr_Carro carro = socketManager.solicitarVeiculoPorId(id);
            
            if (carro != null) {
                // Preencher formulário com os dados do carro
                preencherFormulario(carro);
                JOptionPane.showMessageDialog(this, 
                    "Veículo encontrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Veículo não encontrado com o ID informado.", 
                    "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ID inválido. Digite um número inteiro.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Salva um veículo (novo ou existente)
     */
    private void salvarVeiculo() {
        try {
            // Validar campos obrigatórios
            if (idField.getText().trim().isEmpty() || 
                placaField.getText().trim().isEmpty() || 
                modeloField.getText().trim().isEmpty() || 
                marcaField.getText().trim().isEmpty() || 
                anoField.getText().trim().isEmpty() || 
                corField.getText().trim().isEmpty() || 
                quilometragemField.getText().trim().isEmpty() || 
                valorCompraField.getText().trim().isEmpty() || 
                valorVendaField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, 
                    "Todos os campos são obrigatórios.", 
                    "Campos Vazios", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Criar objeto Carro com os dados do formulário
            int id = Integer.parseInt(idField.getText().trim());
            String placa = placaField.getText().trim();
            String modelo = modeloField.getText().trim();
            String marca = marcaField.getText().trim();
            int ano = Integer.parseInt(anoField.getText().trim());
            String combustivel = (String) combustivelCombo.getSelectedItem();
            String cor = corField.getText().trim();
            double quilometragem = Double.parseDouble(quilometragemField.getText().trim().replace(',', '.'));
            String categoria = (String) categoriaCombo.getSelectedItem();
            boolean disponivel = disponivelCheck.isSelected();
            double valorCompra = Double.parseDouble(valorCompraField.getText().trim().replace(',', '.'));
            double valorVenda = Double.parseDouble(valorVendaField.getText().trim().replace(',', '.'));
            
            Nsr_Carro carro = new Nsr_Carro(id, placa, modelo, marca, ano, combustivel, 
                                    cor, quilometragem, categoria, disponivel, 
                                    valorCompra, valorVenda);
            
            // Verificar se é um novo carro ou atualização
            boolean sucesso;
            Nsr_Carro existente = socketManager.solicitarVeiculoPorId(id);
            
            if (existente != null) {
                // Atualizar carro existente
                sucesso = socketManager.atualizarVeiculo(carro);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, 
                        "Veículo atualizado com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Não foi possível atualizar o veículo.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Adicionar novo carro
                sucesso = socketManager.adicionarVeiculo(carro);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, 
                        "Veículo cadastrado com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Não foi possível cadastrar o veículo.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao converter valores numéricos. Verifique os campos de ID, Ano, Quilometragem e Valores.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa o formulário
     */
    private void limparFormulario() {
        idField.setText("");
        placaField.setText("");
        modeloField.setText("");
        marcaField.setText("");
        anoField.setText("");
        combustivelCombo.setSelectedIndex(0);
        corField.setText("");
        quilometragemField.setText("");
        categoriaCombo.setSelectedIndex(0);
        disponivelCheck.setSelected(true);
        valorCompraField.setText("");
        valorVendaField.setText("");
        
        idField.requestFocus();
    }
    
    /**
     * Preenche o formulário com os dados de um carro
     */
    private void preencherFormulario(Nsr_Carro carro) {
        idField.setText(String.valueOf(carro.getId()));
        placaField.setText(carro.getPlaca());
        modeloField.setText(carro.getModelo());
        marcaField.setText(carro.getMarca());
        anoField.setText(String.valueOf(carro.getAno()));
        
        // Selecionar o combustível correto no ComboBox
        for (int i = 0; i < combustivelCombo.getItemCount(); i++) {
            if (combustivelCombo.getItemAt(i).equals(carro.getCombustivel())) {
                combustivelCombo.setSelectedIndex(i);
                break;
            }
        }
        
        corField.setText(carro.getCor());
        quilometragemField.setText(String.format("%.2f", carro.getQuilometragem()).replace('.', ','));
        
        // Selecionar a categoria correta no ComboBox
        for (int i = 0; i < categoriaCombo.getItemCount(); i++) {
            if (categoriaCombo.getItemAt(i).equals(carro.getCategoria())) {
                categoriaCombo.setSelectedIndex(i);
                break;
            }
        }
        
        disponivelCheck.setSelected(carro.isDisponivel());
        valorCompraField.setText(String.format("%.2f", carro.getValorCompra()).replace('.', ','));
        valorVendaField.setText(String.format("%.2f", carro.getValorVenda()).replace('.', ','));
    }
}