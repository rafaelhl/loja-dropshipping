import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import ProdutoFornecedorList from './ProdutoFornecedorList';
import { fetchWithAuthorization } from '.';

class FornecedorList extends Component {

    constructor(props) {
        super(props);
        this.state = {page: {}, isLoading: true};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetchWithAuthorization('loja-api/fornecedores')
            .then(response => response.json())
            .then(data => this.setState({page: data, isLoading: false}));
    }

    async remove(id) {
        await fetchWithAuthorization(`/loja-api/fornecedores/${id}`, 'DELETE').then((response) => {
            if (!response.ok) {
                return;
            }
            let updatedFornecedores = [...this.state.page.elements].filter(p => p.idFornecedor !== id)
            this.setState({page: {...this.state.page, elements: updatedFornecedores}});
        });
    }

    renderFornecedorProduto(page) {
        return page.elements.map(fornecedorProduto => {
            return <tr key={fornecedorProduto.idFornecedor}>
                <td>{fornecedorProduto.produto.nome}</td>
                <td>{fornecedorProduto.preco.toLocaleString()}</td>
            </tr>    
        });        
    }

    render() {
        const {page, isLoading} = this.state;

        if (isLoading) {
            return <p>Carregando...</p>;
        }

        const fornecedorList = page.elements.map(fornecedor => {
            const pessoa = fornecedor.pessoa;
            return <tr key={fornecedor.idFornecedor}>
                <td>{pessoa ? pessoa.cpfCnpj : ''}</td>
                <td style={{whiteSpace: 'nowrap'}}>{pessoa ? pessoa.nome : ''}</td>
                <td>
                    <ProdutoFornecedorList idFornecedor={fornecedor.idFornecedor} render={this.renderFornecedorProduto} />
                </td>
                <td>
                    <tr>
                        <ButtonGroup>
                            <Button size="sm" color="primary" tag={Link} to={"/fornecedores/" + fornecedor.idFornecedor}>Editar</Button>
                            <Button size="sm" color="danger" onClick={() => this.remove(fornecedor.idFornecedor)}>Remover</Button>
                        </ButtonGroup>
                    </tr>
                    <tr>
                        <ButtonGroup>
                            <Button size="sm" color="info" tag={Link} to={"/integracoes/fornecedor/" + fornecedor.idFornecedor}>Configurar Integração</Button>
                        </ButtonGroup>
                    </tr>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/fornecedores/new">Cadastrar Fornecedor</Button>
                    </div>
                    <h3>Cadastro de Fornecedores</h3>
                    <Table className="mt-4">
                        <thead>
                            <tr>
                                <th>CNPJ</th>
                                <th width="20%">Nome</th>
                                <th>Produtos</th>
                                <th width="10%">Ações</th> 
                            </tr>
                        </thead>
                        <tbody>
                            {fornecedorList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );

    }

}

export default FornecedorList;