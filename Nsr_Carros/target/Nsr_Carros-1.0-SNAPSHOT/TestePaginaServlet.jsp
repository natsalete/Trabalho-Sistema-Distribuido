<%-- 
    Document   : TestePaginaServlet
    Created on : 4 de mai. de 2025, 23:04:33
    Author     : natsa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.poo2.nsr_carros.servlets.Nsr_Carros"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cadastro de Carros</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para ícones -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .card {
            margin-bottom: 20px;
        }
        .btn-action {
            margin-right: 5px;
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <div class="row">
            <div class="col-12">
                <h1 class="mb-4 text-primary">
                    <i class="fas fa-car me-2"></i>Cadastro de Carros
                </h1>
            </div>
        </div>
        
        <%
            // Verificar se estamos em modo de edição
            boolean modoEdicao = request.getAttribute("modoEdicao") != null && (boolean)request.getAttribute("modoEdicao");
            Nsr_Carros carroEditar = null;
            int indexEditar = -1;
            
            if (modoEdicao) {
                carroEditar = (Nsr_Carros)request.getAttribute("carroEditar");
                indexEditar = (int)request.getAttribute("indexEditar");
            }
        %>
        
        <!-- Formulário de cadastro/edição -->
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">
                    <i class="<%= modoEdicao ? "fas fa-edit" : "fas fa-plus" %> me-2"></i>
                    <%= modoEdicao ? "Editar Carro" : "Novo Carro" %>
                </h5>
            </div>
            <div class="card-body">
                <form action="Nsr_GerenciarDados" method="post">
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label for="placa" class="form-label">Placa:</label>
                            <input type="text" class="form-control" id="placa" name="placa" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getPlaca() : "" %>" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="modelo" class="form-label">Modelo:</label>
                            <input type="text" class="form-control" id="modelo" name="modelo" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getModelo() : "" %>" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="marca" class="form-label">Marca:</label>
                            <input type="text" class="form-control" id="marca" name="marca" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getMarca() : "" %>" required>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label for="ano" class="form-label">Ano:</label>
                            <input type="number" class="form-control" id="ano" name="ano" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getAno() : "" %>" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="combustivel" class="form-label">Combustível:</label>
                            <select class="form-select" id="combustivel" name="combustivel" required>
                                <option value="" disabled <%= !modoEdicao ? "selected" : "" %>>Selecione...</option>
                                <option value="Gasolina" <%= modoEdicao && carroEditar.Nsr_getCombustivel().equals("Gasolina") ? "selected" : "" %>>Gasolina</option>
                                <option value="Álcool" <%= modoEdicao && carroEditar.Nsr_getCombustivel().equals("Álcool") ? "selected" : "" %>>Álcool</option>
                                <option value="Diesel" <%= modoEdicao && carroEditar.Nsr_getCombustivel().equals("Diesel") ? "selected" : "" %>>Diesel</option>
                                <option value="Flex" <%= modoEdicao && carroEditar.Nsr_getCombustivel().equals("Flex") ? "selected" : "" %>>Flex</option>
                                <option value="Elétrico" <%= modoEdicao && carroEditar.Nsr_getCombustivel().equals("Elétrico") ? "selected" : "" %>>Elétrico</option>
                                <option value="Híbrido" <%= modoEdicao && carroEditar.Nsr_getCombustivel().equals("Híbrido") ? "selected" : "" %>>Híbrido</option>
                            </select>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="cor" class="form-label">Cor:</label>
                            <input type="text" class="form-control" id="cor" name="cor" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getCor() : "" %>" required>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label for="quilometragem" class="form-label">Quilometragem:</label>
                            <input type="number" step="0.1" class="form-control" id="quilometragem" name="quilometragem" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getQuilometragem() : "" %>" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="categoria" class="form-label">Categoria:</label>
                            <select class="form-select" id="categoria" name="categoria" required>
                                <option value="" disabled <%= !modoEdicao ? "selected" : "" %>>Selecione...</option>
                                <option value="Hatch" <%= modoEdicao && carroEditar.Nsr_getCategoria().equals("Hatch") ? "selected" : "" %>>Hatch</option>
                                <option value="Sedan" <%= modoEdicao && carroEditar.Nsr_getCategoria().equals("Sedan") ? "selected" : "" %>>Sedan</option>
                                <option value="SUV" <%= modoEdicao && carroEditar.Nsr_getCategoria().equals("SUV") ? "selected" : "" %>>SUV</option>
                                <option value="Pickup" <%= modoEdicao && carroEditar.Nsr_getCategoria().equals("Pickup") ? "selected" : "" %>>Pickup</option>
                                <option value="Esportivo" <%= modoEdicao && carroEditar.Nsr_getCategoria().equals("Esportivo") ? "selected" : "" %>>Esportivo</option>
                                <option value="Utilitário" <%= modoEdicao && carroEditar.Nsr_getCategoria().equals("Utilitário") ? "selected" : "" %>>Utilitário</option>
                            </select>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Disponível:</label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="disponivel" id="disponivel_sim" value="true" 
                                       <%= modoEdicao && carroEditar.Nsr_isDisponivel() ? "checked" : "" %> <%= !modoEdicao ? "checked" : "" %>>
                                <label class="form-check-label" for="disponivel_sim">Sim</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="disponivel" id="disponivel_nao" value="false" 
                                       <%= modoEdicao && !carroEditar.Nsr_isDisponivel() ? "checked" : "" %>>
                                <label class="form-check-label" for="disponivel_nao">Não</label>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="valorCompra" class="form-label">Valor de Compra (R$):</label>
                            <input type="number" step="0.01" class="form-control" id="valorCompra" name="valorCompra" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getValorCompra() : "" %>" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="valorVenda" class="form-label">Valor de Venda (R$):</label>
                            <input type="number" step="0.01" class="form-control" id="valorVenda" name="valorVenda" 
                                   value="<%= modoEdicao ? carroEditar.Nsr_getValorVenda() : "" %>" required>
                        </div>
                    </div>
                    
                    <%
                        if (modoEdicao) {
                            // Campos ocultos para edição
                    %>
                            <input type="hidden" name="indexEditar" value="<%= indexEditar %>">
                            <input type="hidden" name="acao" value="Atualizar">
                            <div class="d-flex">
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save me-1"></i> Atualizar
                                </button>
                                <a href="Nsr_GerenciarDados?acao=Listar" class="btn btn-secondary ms-2">
                                    <i class="fas fa-times me-1"></i> Cancelar
                                </a>
                            </div>
                    <%
                        } else {
                            // Novo cadastro
                    %>
                            <input type="hidden" name="acao" value="Salvar">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-1"></i> Salvar
                            </button>
                    <%
                        }
                    %>
                </form>
            </div>
        </div>
        
        <!-- Botão para listar todos os carros -->
        <form action="Nsr_GerenciarDados" method="post" class="mb-4">
            <input type="hidden" name="acao" value="Listar">
            <button type="submit" class="btn btn-info text-white">
                <i class="fas fa-list me-1"></i> Listar Todos os Carros
            </button>
        </form>
        
        <!-- Exibição do carro selecionado -->
        <%
            Nsr_Carros carro = (Nsr_Carros)request.getAttribute("carro");
            if(carro != null) {
        %>
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0"><i class="fas fa-info-circle me-2"></i>Detalhes do Carro</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Placa:</strong> <%= carro.Nsr_getPlaca() %></p>
                            <p><strong>Modelo:</strong> <%= carro.Nsr_getModelo() %></p>
                            <p><strong>Marca:</strong> <%= carro.Nsr_getMarca() %></p>
                            <p><strong>Ano:</strong> <%= carro.Nsr_getAno() %></p>
                            <p><strong>Combustível:</strong> <%= carro.Nsr_getCombustivel() %></p>
                            <p><strong>Cor:</strong> <%= carro.Nsr_getCor() %></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Quilometragem:</strong> <%= carro.Nsr_getQuilometragem() %> km</p>
                            <p><strong>Categoria:</strong> <%= carro.Nsr_getCategoria() %></p>
                            <p><strong>Disponível:</strong> <%= carro.Nsr_isDisponivel() ? "Sim" : "Não" %></p>
                            <p><strong>Valor de Compra:</strong> R$ <%= String.format("%.2f", carro.Nsr_getValorCompra()) %></p>
                            <p><strong>Valor de Venda:</strong> R$ <%= String.format("%.2f", carro.Nsr_getValorVenda()) %></p>
                        </div>
                    </div>
                </div>
            </div>
        <%
            }
        %>
        
        <!-- Listagem de todos os carros -->
        <%
            List<Nsr_Carros> lstCarros = (List<Nsr_Carros>)request.getAttribute("lstCarros");
            if(lstCarros != null && !lstCarros.isEmpty()) {
        %>
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="fas fa-list me-2"></i>Lista de Carros</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Placa</th>
                                    <th>Modelo</th>
                                    <th>Marca</th>
                                    <th>Ano</th>
                                    <th>Cor</th>
                                    <th>Disp.</th>
                                    <th>Valor Venda</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for(int i = 0; i < lstCarros.size(); i++) {
                                        Nsr_Carros c = lstCarros.get(i);
                                %>
                                    <tr>
                                        <td><%= c.Nsr_getId() %></td>
                                        <td><%= c.Nsr_getPlaca() %></td>
                                        <td><%= c.Nsr_getModelo() %></td>
                                        <td><%= c.Nsr_getMarca() %></td>
                                        <td><%= c.Nsr_getAno() %></td>
                                        <td><%= c.Nsr_getCor() %></td>
                                        <td><span class="badge <%= c.Nsr_isDisponivel() ? "bg-success" : "bg-danger" %>">
                                            <%= c.Nsr_isDisponivel() ? "Sim" : "Não" %>
                                        </span></td>
                                        <td>R$ <%= String.format("%.2f", c.Nsr_getValorVenda()) %></td>
                                        <td>
                                            <div class="btn-group" role="group">
                                                <a href="Nsr_GerenciarDados?acao=Mostra-<%= c.Nsr_getId() %>" class="btn btn-sm btn-info text-white btn-action" title="Visualizar">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="Nsr_GerenciarDados?acao=Editar-<%= c.Nsr_getId() %>" class="btn btn-sm btn-warning text-dark btn-action" title="Editar">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="Nsr_GerenciarDados?acao=Excluir-<%= c.Nsr_getId() %>" class="btn btn-sm btn-danger btn-action" 
                                                   onclick="return confirm('Tem certeza que deseja excluir este carro?')" title="Excluir">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-footer text-muted">
                    <small>Total de carros: <%= lstCarros.size() %></small>
                </div>
            </div>
        <%
            } else if(request.getAttribute("lstCarros") != null) {
        %>
            <div class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i> Nenhum carro cadastrado no sistema.
            </div>
        <%
            }
        %>
    </div>
    
    <!-- Bootstrap Bundle com Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>