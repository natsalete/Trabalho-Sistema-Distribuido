package com.poo2.nsr_carros.servlets;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Servlet responsável pelo gerenciamento de dados dos carros.
 * @author natsa
 */
@WebServlet(name = "Nsr_GerenciarDados", urlPatterns = {"/Nsr_GerenciarDados"})
public class Nsr_GerenciarDados extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param Nsr_request servlet request
     * @param Nsr_response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void Nsr_processRequest(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response)
            throws ServletException, IOException {
        Nsr_response.setContentType("text/html;charset=UTF-8");
        String Nsr_acao = Nsr_request.getParameter("acao");
        
        // Verifica se ação está vazia ou nula
        if (Nsr_acao == null || Nsr_acao.isEmpty()) {
            // Redireciona para a página principal sem ação definida
            String Nsr_path = "/TestePaginaServlet.jsp";
            RequestDispatcher Nsr_dispatcher = Nsr_request.getRequestDispatcher(Nsr_path);
            Nsr_dispatcher.forward(Nsr_request, Nsr_response);
            return;
        }
        
        String Nsr_parteAcao[] = Nsr_acao.split("-");
        int Nsr_index = 0;
        if(Nsr_parteAcao.length > 1){
            if(Nsr_parteAcao[0].equals("Mostra")) {
                Nsr_acao = "Mostra_Obj";
                Nsr_index = Integer.parseInt(Nsr_parteAcao[1].trim())-1;
            } else if(Nsr_parteAcao[0].equals("Editar")) {
                Nsr_acao = "Editar_Obj";
                Nsr_index = Integer.parseInt(Nsr_parteAcao[1].trim())-1;
            } else if(Nsr_parteAcao[0].equals("Excluir")) {
                Nsr_acao = "Excluir_Obj";
                Nsr_index = Integer.parseInt(Nsr_parteAcao[1].trim())-1;
            }
        }
        System.out.println("Ação: " + Nsr_acao);
        System.out.println("Índice: " + Nsr_index);
        
        try{
            switch(Nsr_acao){
                case "Salvar":
                    Nsr_salvarCarro(Nsr_request, Nsr_response);
                    break;
                case "Atualizar":
                    Nsr_atualizarCarro(Nsr_request, Nsr_response);
                    break;
                case "Listar":
                    Nsr_getCarros(Nsr_request, Nsr_response);
                    break;
                case "Mostra_Obj":
                    Nsr_getCarros(Nsr_request, Nsr_response, Nsr_index);
                    break;
                case "Editar_Obj":
                    Nsr_editarCarro(Nsr_request, Nsr_response, Nsr_index);
                    break;
                case "Excluir_Obj":
                    Nsr_excluirCarro(Nsr_request, Nsr_response, Nsr_index);
                    break;
                default:
                    // Redireciona para a página principal para ações não reconhecidas
                    String Nsr_path = "/TestePaginaServlet.jsp";
                    RequestDispatcher Nsr_dispatcher = Nsr_request.getRequestDispatcher(Nsr_path);
                    Nsr_dispatcher.forward(Nsr_request, Nsr_response);
            }
        } catch(Exception Nsr_ex){
            System.err.println("Erro ao processar requisição: " + Nsr_ex.getMessage());
            Nsr_ex.printStackTrace();
            throw new ServletException(Nsr_ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param Nsr_request servlet request
     * @param Nsr_response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response)
            throws ServletException, IOException {
        Nsr_processRequest(Nsr_request, Nsr_response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param Nsr_request servlet request
     * @param Nsr_response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response)
            throws ServletException, IOException {
        Nsr_processRequest(Nsr_request, Nsr_response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Salva um novo carro no sistema.
     */
    private void Nsr_salvarCarro(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response)
            throws ServletException, IOException {
       Nsr_Carros Nsr_carro = new Nsr_Carros();
       Nsr_carro.Nsr_setPlaca(Nsr_request.getParameter("placa"));
       Nsr_carro.Nsr_setModelo(Nsr_request.getParameter("modelo"));
       Nsr_carro.Nsr_setMarca(Nsr_request.getParameter("marca"));
       Nsr_carro.Nsr_setAno(Integer.parseInt(Nsr_request.getParameter("ano")));
       Nsr_carro.Nsr_setCombustivel(Nsr_request.getParameter("combustivel"));
       Nsr_carro.Nsr_setCor(Nsr_request.getParameter("cor"));
       Nsr_carro.Nsr_setQuilometragem(Double.parseDouble(Nsr_request.getParameter("quilometragem")));
       Nsr_carro.Nsr_setCategoria(Nsr_request.getParameter("categoria"));
       Nsr_carro.Nsr_setDisponivel(Boolean.parseBoolean(Nsr_request.getParameter("disponivel")));
       Nsr_carro.Nsr_setValorCompra(Double.parseDouble(Nsr_request.getParameter("valorCompra")));
       Nsr_carro.Nsr_setValorVenda(Double.parseDouble(Nsr_request.getParameter("valorVenda")));
       
       // Obter a próxima ID disponível
       Nsr_BuscarArquivo Nsr_buscarArq = new Nsr_BuscarArquivo();
       List<Nsr_Carros> Nsr_lstCarros = Nsr_buscarArq.Nsr_lerArqCarros(getServletContext());
       int Nsr_proximoId = 1;
       if (Nsr_lstCarros != null && !Nsr_lstCarros.isEmpty()) {
           // Encontre o maior ID atual e adicione 1
           for (Nsr_Carros Nsr_c : Nsr_lstCarros) {
               if (Nsr_c.Nsr_getId() >= Nsr_proximoId) {
                   Nsr_proximoId = Nsr_c.Nsr_getId() + 1;
               }
           }
       }
       Nsr_carro.Nsr_setId(Nsr_proximoId);
       
       // Passa o ServletContext para a classe GravarArquivo
       Nsr_GravarArquivo Nsr_gravarArq = new Nsr_GravarArquivo();
       Nsr_gravarArq.Nsr_escreverArqCarro(Nsr_carro, getServletContext());
       
       // Redirecionar para listagem após salvar
       Nsr_getCarros(Nsr_request, Nsr_response);
    }
    
    /**
     * Busca todos os carros e exibe na página.
     */
    private void Nsr_getCarros(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response)
            throws ServletException, IOException {
        // Passa o ServletContext para a classe BuscarArquivo
        Nsr_BuscarArquivo Nsr_buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> Nsr_lstCarros = Nsr_buscarArq.Nsr_lerArqCarros(getServletContext());
        
        System.out.println("Carros encontrados: " + (Nsr_lstCarros != null ? Nsr_lstCarros.size() : 0));
        Nsr_request.setAttribute("lstCarros", Nsr_lstCarros);
        String Nsr_path = "/TestePaginaServlet.jsp";
        RequestDispatcher Nsr_dispatcher = Nsr_request.getRequestDispatcher(Nsr_path);
        Nsr_dispatcher.forward(Nsr_request, Nsr_response);
    }
    
    /**
     * Busca um carro específico e exibe na página.
     */
    private void Nsr_getCarros(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response, int Nsr_index)
            throws ServletException, IOException {
        // Passa o ServletContext para a classe BuscarArquivo
        Nsr_BuscarArquivo Nsr_buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> Nsr_lstCarros = Nsr_buscarArq.Nsr_lerArqCarros(getServletContext());
        
        if (Nsr_index >= 0 && Nsr_index < Nsr_lstCarros.size()) {
            Nsr_Carros Nsr_carro = Nsr_lstCarros.get(Nsr_index);
            Nsr_request.setAttribute("carro", Nsr_carro);
        }
        
        // Exibe também a lista completa
        Nsr_request.setAttribute("lstCarros", Nsr_lstCarros);
        String Nsr_path = "/TestePaginaServlet.jsp";
        RequestDispatcher Nsr_dispatcher = Nsr_request.getRequestDispatcher(Nsr_path);
        Nsr_dispatcher.forward(Nsr_request, Nsr_response);
    }
    
    /**
     * Prepara um carro para edição.
     */
    private void Nsr_editarCarro(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response, int Nsr_index)
            throws ServletException, IOException {
        // Buscar a lista atual de carros
        Nsr_BuscarArquivo Nsr_buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> Nsr_lstCarros = Nsr_buscarArq.Nsr_lerArqCarros(getServletContext());
        
        // Obter o carro a ser editado se o índice for válido
        if (Nsr_index >= 0 && Nsr_index < Nsr_lstCarros.size()) {
            Nsr_Carros Nsr_carro = Nsr_lstCarros.get(Nsr_index);
            
            // Adicionar o carro e o índice ao request para o formulário de edição
            Nsr_request.setAttribute("carroEditar", Nsr_carro);
            Nsr_request.setAttribute("indexEditar", Nsr_index);
            Nsr_request.setAttribute("modoEdicao", true);
        }
        
        // Exibe também a lista completa
        Nsr_request.setAttribute("lstCarros", Nsr_lstCarros);
        
        // Redirecionar para a página JSP
        String Nsr_path = "/TestePaginaServlet.jsp";
        RequestDispatcher Nsr_dispatcher = Nsr_request.getRequestDispatcher(Nsr_path);
        Nsr_dispatcher.forward(Nsr_request, Nsr_response);
    }
    
    /**
     * Atualiza os dados de um carro existente.
     */
    private void Nsr_atualizarCarro(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response)
            throws ServletException, IOException {
        // Obter o índice do carro a ser atualizado
        int Nsr_index = Integer.parseInt(Nsr_request.getParameter("indexEditar"));
        
        // Buscar a lista atual de carros
        Nsr_BuscarArquivo Nsr_buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> Nsr_lstCarros = Nsr_buscarArq.Nsr_lerArqCarros(getServletContext());
        
        // Atualizar os dados do carro no índice especificado
        if (Nsr_index >= 0 && Nsr_index < Nsr_lstCarros.size()) {
            Nsr_Carros Nsr_carro = Nsr_lstCarros.get(Nsr_index);
            Nsr_carro.Nsr_setPlaca(Nsr_request.getParameter("placa"));
            Nsr_carro.Nsr_setModelo(Nsr_request.getParameter("modelo"));
            Nsr_carro.Nsr_setMarca(Nsr_request.getParameter("marca"));
            Nsr_carro.Nsr_setAno(Integer.parseInt(Nsr_request.getParameter("ano")));
            Nsr_carro.Nsr_setCombustivel(Nsr_request.getParameter("combustivel"));
            Nsr_carro.Nsr_setCor(Nsr_request.getParameter("cor"));
            Nsr_carro.Nsr_setQuilometragem(Double.parseDouble(Nsr_request.getParameter("quilometragem")));
            Nsr_carro.Nsr_setCategoria(Nsr_request.getParameter("categoria"));
            Nsr_carro.Nsr_setDisponivel(Boolean.parseBoolean(Nsr_request.getParameter("disponivel")));
            Nsr_carro.Nsr_setValorCompra(Double.parseDouble(Nsr_request.getParameter("valorCompra")));
            Nsr_carro.Nsr_setValorVenda(Double.parseDouble(Nsr_request.getParameter("valorVenda")));
            
            // Reescrever todo o arquivo com a lista atualizada
            Nsr_GravarArquivo Nsr_gravarArq = new Nsr_GravarArquivo();
            Nsr_gravarArq.Nsr_reescreverArquivo(Nsr_lstCarros, getServletContext());
        }
        
        // Redirecionar para a listagem
        Nsr_request.setAttribute("lstCarros", Nsr_lstCarros);
        String Nsr_path = "/TestePaginaServlet.jsp";
        RequestDispatcher Nsr_dispatcher = Nsr_request.getRequestDispatcher(Nsr_path);
        Nsr_dispatcher.forward(Nsr_request, Nsr_response);
    }
    
    /**
     * Exclui um carro do sistema.
     */
    private void Nsr_excluirCarro(HttpServletRequest Nsr_request, HttpServletResponse Nsr_response, int Nsr_index)
            throws ServletException, IOException {
        // Buscar a lista atual de carros
        Nsr_BuscarArquivo Nsr_buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> Nsr_lstCarros = Nsr_buscarArq.Nsr_lerArqCarros(getServletContext());
        
        // Remover o carro com o índice especificado se válido
        if (Nsr_index >= 0 && Nsr_index < Nsr_lstCarros.size()) {
            Nsr_lstCarros.remove(Nsr_index);
            
            // Reescrever todo o arquivo com a lista atualizada
            Nsr_GravarArquivo Nsr_gravarArq = new Nsr_GravarArquivo();
            Nsr_gravarArq.Nsr_reescreverArquivo(Nsr_lstCarros, getServletContext());
        }
        
        // Redirecionar para a listagem
        Nsr_request.setAttribute("lstCarros", Nsr_lstCarros);
        String Nsr_path = "/TestePaginaServlet.jsp";
        RequestDispatcher Nsr_dispatcher = Nsr_request.getRequestDispatcher(Nsr_path);
        Nsr_dispatcher.forward(Nsr_request, Nsr_response);
    }
}