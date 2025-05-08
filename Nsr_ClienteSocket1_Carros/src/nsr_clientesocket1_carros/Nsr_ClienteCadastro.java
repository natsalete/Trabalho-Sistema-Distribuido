package nsr_clientesocket1_carros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Painel para cadastro de veículos
 */
public class Nsr_ClienteCadastro extends JPanel {
    private Nsr_SocketManager Nsr_socketManager;
    
    // Campos de formulário
    private JTextField Nsr_idField;
    private JTextField Nsr_placaField;
    private JTextField Nsr_modeloField;
    private JTextField Nsr_marcaField;
    private JTextField Nsr_anoField;
    private JComboBox<String> Nsr_combustivelCombo;
    private JTextField Nsr_corField;
    private JTextField Nsr_quilometragemField;
    private JComboBox<String> Nsr_categoriaCombo;
    private JCheckBox Nsr_disponivelCheck;
    private JTextField Nsr_valorCompraField;
    private JTextField Nsr_valorVendaField;
    
    // Botões
    private JButton Nsr_salvarButton;
    private JButton Nsr_limparButton;
    private JButton Nsr_buscarButton;
    
    /**
     * Construtor
     */
    public Nsr_ClienteCadastro(Nsr_SocketManager Nsr_socketManager) {
        this.Nsr_socketManager = Nsr_socketManager;
        Nsr_initComponents();
    }
    
    /**
     * Inicializa os componentes da interface
     */
    private void Nsr_initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel de formulário
        JPanel Nsr_formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        Nsr_formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID
        Nsr_formPanel.add(new JLabel("ID:"));
        Nsr_idField = new JTextField();
        Nsr_formPanel.add(Nsr_idField);
        
        // Placa
        Nsr_formPanel.add(new JLabel("Placa:"));
        Nsr_placaField = new JTextField();
        Nsr_formPanel.add(Nsr_placaField);
        
        // Modelo
        Nsr_formPanel.add(new JLabel("Modelo:"));
        Nsr_modeloField = new JTextField();
        Nsr_formPanel.add(Nsr_modeloField);
        
        // Marca
        Nsr_formPanel.add(new JLabel("Marca:"));
        Nsr_marcaField = new JTextField();
        Nsr_formPanel.add(Nsr_marcaField);
        
        // Ano
        Nsr_formPanel.add(new JLabel("Ano:"));
        Nsr_anoField = new JTextField();
        Nsr_formPanel.add(Nsr_anoField);
        
        // Combustível
        Nsr_formPanel.add(new JLabel("Combustível:"));
        Nsr_combustivelCombo = new JComboBox<>(new String[] {
            "GASOLINA", "ALCOOL", "FLEX", "DIESEL", "ELETRICO", "HIBRIDO"
        });
        Nsr_formPanel.add(Nsr_combustivelCombo);
        
        // Cor
        Nsr_formPanel.add(new JLabel("Cor:"));
        Nsr_corField = new JTextField();
        Nsr_formPanel.add(Nsr_corField);
        
        // Quilometragem
        Nsr_formPanel.add(new JLabel("Quilometragem:"));
        Nsr_quilometragemField = new JTextField();
        Nsr_formPanel.add(Nsr_quilometragemField);
        
        // Categoria
        Nsr_formPanel.add(new JLabel("Categoria:"));
        Nsr_categoriaCombo = new JComboBox<>(new String[] {
            "SEDAN", "HATCHBACK", "SUV", "PICKUP", "ESPORTIVO", "UTILITARIO"
        });
        Nsr_formPanel.add(Nsr_categoriaCombo);
        
        // Disponível
        Nsr_formPanel.add(new JLabel("Disponível:"));
        Nsr_disponivelCheck = new JCheckBox();
        Nsr_disponivelCheck.setSelected(true);
        Nsr_formPanel.add(Nsr_disponivelCheck);
        
        // Valor de Compra
        Nsr_formPanel.add(new JLabel("Valor de Compra (R$):"));
        Nsr_valorCompraField = new JTextField();
        Nsr_formPanel.add(Nsr_valorCompraField);
        
        // Valor de Venda
        Nsr_formPanel.add(new JLabel("Valor de Venda (R$):"));
        Nsr_valorVendaField = new JTextField();
        Nsr_formPanel.add(Nsr_valorVendaField);
        
        // Painel de botões
        JPanel Nsr_botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        Nsr_buscarButton = new JButton("Buscar por ID");
        Nsr_buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Nsr_e) {
                Nsr_buscarVeiculo();
            }
        });
        Nsr_botoesPanel.add(Nsr_buscarButton);
        
        Nsr_salvarButton = new JButton("Salvar");
        Nsr_salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Nsr_e) {
                Nsr_salvarVeiculo();
            }
        });
        Nsr_botoesPanel.add(Nsr_salvarButton);
        
        Nsr_limparButton = new JButton("Limpar");
        Nsr_limparButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Nsr_e) {
                Nsr_limparFormulario();
            }
        });
        Nsr_botoesPanel.add(Nsr_limparButton);
        
        // Adicionar ao painel principal
        add(new JScrollPane(Nsr_formPanel), BorderLayout.CENTER);
        add(Nsr_botoesPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Busca um veículo pelo ID
     */
    private void Nsr_buscarVeiculo() {
        try {
            int Nsr_id = Integer.parseInt(Nsr_idField.getText().trim());
            Nsr_Carro Nsr_carro = Nsr_socketManager.Nsr_solicitarVeiculoPorId(Nsr_id);
            
            if (Nsr_carro != null) {
                // Preencher formulário com os dados do carro
                Nsr_preencherFormulario(Nsr_carro);
                JOptionPane.showMessageDialog(this, 
                    "Veículo encontrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Veículo não encontrado com o ID informado.", 
                    "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException Nsr_e) {
            JOptionPane.showMessageDialog(this, 
                "ID inválido. Digite um número inteiro.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Salva um veículo (novo ou existente)
     */
    private void Nsr_salvarVeiculo() {
        try {
            // Validar campos obrigatórios
            if (Nsr_idField.getText().trim().isEmpty() || 
                Nsr_placaField.getText().trim().isEmpty() || 
                Nsr_modeloField.getText().trim().isEmpty() || 
                Nsr_marcaField.getText().trim().isEmpty() || 
                Nsr_anoField.getText().trim().isEmpty() || 
                Nsr_corField.getText().trim().isEmpty() || 
                Nsr_quilometragemField.getText().trim().isEmpty() || 
                Nsr_valorCompraField.getText().trim().isEmpty() || 
                Nsr_valorVendaField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, 
                    "Todos os campos são obrigatórios.", 
                    "Campos Vazios", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Criar objeto Carro com os dados do formulário
            int Nsr_id = Integer.parseInt(Nsr_idField.getText().trim());
            String Nsr_placa = Nsr_placaField.getText().trim();
            String Nsr_modelo = Nsr_modeloField.getText().trim();
            String Nsr_marca = Nsr_marcaField.getText().trim();
            int Nsr_ano = Integer.parseInt(Nsr_anoField.getText().trim());
            String Nsr_combustivel = (String) Nsr_combustivelCombo.getSelectedItem();
            String Nsr_cor = Nsr_corField.getText().trim();
            double Nsr_quilometragem = Double.parseDouble(Nsr_quilometragemField.getText().trim().replace(',', '.'));
            String Nsr_categoria = (String) Nsr_categoriaCombo.getSelectedItem();
            boolean Nsr_disponivel = Nsr_disponivelCheck.isSelected();
            double Nsr_valorCompra = Double.parseDouble(Nsr_valorCompraField.getText().trim().replace(',', '.'));
            double Nsr_valorVenda = Double.parseDouble(Nsr_valorVendaField.getText().trim().replace(',', '.'));
            
            Nsr_Carro Nsr_carro = new Nsr_Carro(Nsr_id, Nsr_placa, Nsr_modelo, Nsr_marca, Nsr_ano, Nsr_combustivel, 
                                    Nsr_cor, Nsr_quilometragem, Nsr_categoria, Nsr_disponivel, 
                                    Nsr_valorCompra, Nsr_valorVenda);
            
            // Verificar se é um novo carro ou atualização
            boolean Nsr_sucesso;
            Nsr_Carro Nsr_existente = Nsr_socketManager.Nsr_solicitarVeiculoPorId(Nsr_id);
            
            if (Nsr_existente != null) {
                // Atualizar carro existente
                Nsr_sucesso = Nsr_socketManager.Nsr_atualizarVeiculo(Nsr_carro);
                if (Nsr_sucesso) {
                    JOptionPane.showMessageDialog(this, 
                        "Veículo atualizado com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    Nsr_limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Não foi possível atualizar o veículo.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Adicionar novo carro
                Nsr_sucesso = Nsr_socketManager.Nsr_adicionarVeiculo(Nsr_carro);
                if (Nsr_sucesso) {
                    JOptionPane.showMessageDialog(this, 
                        "Veículo cadastrado com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    Nsr_limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Não foi possível cadastrar o veículo.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (NumberFormatException Nsr_e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao converter valores numéricos. Verifique os campos de ID, Ano, Quilometragem e Valores.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa o formulário
     */
    private void Nsr_limparFormulario() {
        Nsr_idField.setText("");
        Nsr_placaField.setText("");
        Nsr_modeloField.setText("");
        Nsr_marcaField.setText("");
        Nsr_anoField.setText("");
        Nsr_combustivelCombo.setSelectedIndex(0);
        Nsr_corField.setText("");
        Nsr_quilometragemField.setText("");
        Nsr_categoriaCombo.setSelectedIndex(0);
        Nsr_disponivelCheck.setSelected(true);
        Nsr_valorCompraField.setText("");
        Nsr_valorVendaField.setText("");
        
        Nsr_idField.requestFocus();
    }
    
    /**
     * Preenche o formulário com os dados de um carro
     */
    private void Nsr_preencherFormulario(Nsr_Carro Nsr_carro) {
        Nsr_idField.setText(String.valueOf(Nsr_carro.Nsr_getId()));
        Nsr_placaField.setText(Nsr_carro.Nsr_getPlaca());
        Nsr_modeloField.setText(Nsr_carro.Nsr_getModelo());
        Nsr_marcaField.setText(Nsr_carro.Nsr_getMarca());
        Nsr_anoField.setText(String.valueOf(Nsr_carro.Nsr_getAno()));
        
        // Selecionar o combustível correto no ComboBox
        for (int Nsr_i = 0; Nsr_i < Nsr_combustivelCombo.getItemCount(); Nsr_i++) {
            if (Nsr_combustivelCombo.getItemAt(Nsr_i).equals(Nsr_carro.Nsr_getCombustivel())) {
                Nsr_combustivelCombo.setSelectedIndex(Nsr_i);
                break;
            }
        }
        
        Nsr_corField.setText(Nsr_carro.Nsr_getCor());
        Nsr_quilometragemField.setText(String.format("%.2f", Nsr_carro.Nsr_getQuilometragem()).replace('.', ','));
        
        // Selecionar a categoria correta no ComboBox
        for (int Nsr_i = 0; Nsr_i < Nsr_categoriaCombo.getItemCount(); Nsr_i++) {
            if (Nsr_categoriaCombo.getItemAt(Nsr_i).equals(Nsr_carro.Nsr_getCategoria())) {
                Nsr_categoriaCombo.setSelectedIndex(Nsr_i);
                break;
            }
        }
        
        Nsr_disponivelCheck.setSelected(Nsr_carro.Nsr_isDisponivel());
        Nsr_valorCompraField.setText(String.format("%.2f", Nsr_carro.Nsr_getValorCompra()).replace('.', ','));
        Nsr_valorVendaField.setText(String.format("%.2f", Nsr_carro.Nsr_getValorVenda()).replace('.', ','));
    }
}