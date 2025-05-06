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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String acao = request.getParameter("acao");
        
        // Verifica se ação está vazia ou nula
        if (acao == null || acao.isEmpty()) {
            // Redireciona para a página principal sem ação definida
            String path = "/TestePaginaServlet.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
            return;
        }
        
        String parteAcao[] = acao.split("-");
        int index = 0;
        if(parteAcao.length > 1){
            if(parteAcao[0].equals("Mostra")) {
                acao = "Mostra_Obj";
                index = Integer.parseInt(parteAcao[1].trim())-1;
            } else if(parteAcao[0].equals("Editar")) {
                acao = "Editar_Obj";
                index = Integer.parseInt(parteAcao[1].trim())-1;
            } else if(parteAcao[0].equals("Excluir")) {
                acao = "Excluir_Obj";
                index = Integer.parseInt(parteAcao[1].trim())-1;
            }
        }
        System.out.println("Ação: " + acao);
        System.out.println("Índice: " + index);
        
        try{
            switch(acao){
                case "Salvar":
                    salvarCarro(request, response);
                    break;
                case "Atualizar":
                    atualizarCarro(request, response);
                    break;
                case "Listar":
                    getCarros(request, response);
                    break;
                case "Mostra_Obj":
                    getCarros(request, response, index);
                    break;
                case "Editar_Obj":
                    editarCarro(request, response, index);
                    break;
                case "Excluir_Obj":
                    excluirCarro(request, response, index);
                    break;
                default:
                    // Redireciona para a página principal para ações não reconhecidas
                    String path = "/TestePaginaServlet.jsp";
                    RequestDispatcher dispatcher = request.getRequestDispatcher(path);
                    dispatcher.forward(request, response);
            }
        } catch(Exception ex){
            System.err.println("Erro ao processar requisição: " + ex.getMessage());
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
    private void salvarCarro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       Nsr_Carros carro = new Nsr_Carros();
       carro.setPlaca(request.getParameter("placa"));
       carro.setModelo(request.getParameter("modelo"));
       carro.setMarca(request.getParameter("marca"));
       carro.setAno(Integer.parseInt(request.getParameter("ano")));
       carro.setCombustivel(request.getParameter("combustivel"));
       carro.setCor(request.getParameter("cor"));
       carro.setQuilometragem(Double.parseDouble(request.getParameter("quilometragem")));
       carro.setCategoria(request.getParameter("categoria"));
       carro.setDisponivel(Boolean.parseBoolean(request.getParameter("disponivel")));
       carro.setValorCompra(Double.parseDouble(request.getParameter("valorCompra")));
       carro.setValorVenda(Double.parseDouble(request.getParameter("valorVenda")));
       
       // Obter a próxima ID disponível
       Nsr_BuscarArquivo buscarArq = new Nsr_BuscarArquivo();
       List<Nsr_Carros> lstCarros = buscarArq.lerArqCarros(getServletContext());
       int proximoId = 1;
       if (lstCarros != null && !lstCarros.isEmpty()) {
           // Encontre o maior ID atual e adicione 1
           for (Nsr_Carros c : lstCarros) {
               if (c.getId() >= proximoId) {
                   proximoId = c.getId() + 1;
               }
           }
       }
       carro.setId(proximoId);
       
       // Passa o ServletContext para a classe GravarArquivo
       Nsr_GravarArquivo gravarArq = new Nsr_GravarArquivo();
       gravarArq.escreverArqCarro(carro, getServletContext());
       
       // Redirecionar para listagem após salvar
       getCarros(request, response);
    }
    
    /**
     * Busca todos os carros e exibe na página.
     */
    private void getCarros(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Passa o ServletContext para a classe BuscarArquivo
        Nsr_BuscarArquivo buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> lstCarros = buscarArq.lerArqCarros(getServletContext());
        
        System.out.println("Carros encontrados: " + (lstCarros != null ? lstCarros.size() : 0));
        request.setAttribute("lstCarros", lstCarros);
        String path = "/TestePaginaServlet.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
    
    /**
     * Busca um carro específico e exibe na página.
     */
    private void getCarros(HttpServletRequest request, HttpServletResponse response, int index)
            throws ServletException, IOException {
        // Passa o ServletContext para a classe BuscarArquivo
        Nsr_BuscarArquivo buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> lstCarros = buscarArq.lerArqCarros(getServletContext());
        
        if (index >= 0 && index < lstCarros.size()) {
            Nsr_Carros carro = lstCarros.get(index);
            request.setAttribute("carro", carro);
        }
        
        // Exibe também a lista completa
        request.setAttribute("lstCarros", lstCarros);
        String path = "/TestePaginaServlet.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
    
    /**
     * Prepara um carro para edição.
     */
    private void editarCarro(HttpServletRequest request, HttpServletResponse response, int index)
            throws ServletException, IOException {
        // Buscar a lista atual de carros
        Nsr_BuscarArquivo buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> lstCarros = buscarArq.lerArqCarros(getServletContext());
        
        // Obter o carro a ser editado se o índice for válido
        if (index >= 0 && index < lstCarros.size()) {
            Nsr_Carros carro = lstCarros.get(index);
            
            // Adicionar o carro e o índice ao request para o formulário de edição
            request.setAttribute("carroEditar", carro);
            request.setAttribute("indexEditar", index);
            request.setAttribute("modoEdicao", true);
        }
        
        // Exibe também a lista completa
        request.setAttribute("lstCarros", lstCarros);
        
        // Redirecionar para a página JSP
        String path = "/TestePaginaServlet.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
    
    /**
     * Atualiza os dados de um carro existente.
     */
    private void atualizarCarro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obter o índice do carro a ser atualizado
        int index = Integer.parseInt(request.getParameter("indexEditar"));
        
        // Buscar a lista atual de carros
        Nsr_BuscarArquivo buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> lstCarros = buscarArq.lerArqCarros(getServletContext());
        
        // Atualizar os dados do carro no índice especificado
        if (index >= 0 && index < lstCarros.size()) {
            Nsr_Carros carro = lstCarros.get(index);
            carro.setPlaca(request.getParameter("placa"));
            carro.setModelo(request.getParameter("modelo"));
            carro.setMarca(request.getParameter("marca"));
            carro.setAno(Integer.parseInt(request.getParameter("ano")));
            carro.setCombustivel(request.getParameter("combustivel"));
            carro.setCor(request.getParameter("cor"));
            carro.setQuilometragem(Double.parseDouble(request.getParameter("quilometragem")));
            carro.setCategoria(request.getParameter("categoria"));
            carro.setDisponivel(Boolean.parseBoolean(request.getParameter("disponivel")));
            carro.setValorCompra(Double.parseDouble(request.getParameter("valorCompra")));
            carro.setValorVenda(Double.parseDouble(request.getParameter("valorVenda")));
            
            // Reescrever todo o arquivo com a lista atualizada
            Nsr_GravarArquivo gravarArq = new Nsr_GravarArquivo();
            gravarArq.reescreverArquivo(lstCarros, getServletContext());
        }
        
        // Redirecionar para a listagem
        request.setAttribute("lstCarros", lstCarros);
        String path = "/TestePaginaServlet.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
    
    /**
     * Exclui um carro do sistema.
     */
    private void excluirCarro(HttpServletRequest request, HttpServletResponse response, int index)
            throws ServletException, IOException {
        // Buscar a lista atual de carros
        Nsr_BuscarArquivo buscarArq = new Nsr_BuscarArquivo();
        List<Nsr_Carros> lstCarros = buscarArq.lerArqCarros(getServletContext());
        
        // Remover o carro com o índice especificado se válido
        if (index >= 0 && index < lstCarros.size()) {
            lstCarros.remove(index);
            
            // Reescrever todo o arquivo com a lista atualizada
            Nsr_GravarArquivo gravarArq = new Nsr_GravarArquivo();
            gravarArq.reescreverArquivo(lstCarros, getServletContext());
        }
        
        // Redirecionar para a listagem
        request.setAttribute("lstCarros", lstCarros);
        String path = "/TestePaginaServlet.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
}