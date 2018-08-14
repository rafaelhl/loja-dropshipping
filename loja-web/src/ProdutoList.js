import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { fetchWithAuthorization } from '.';

class ProdutoList extends Component {

    constructor(props) {
        super(props);
        this.state = {page: {}, isLoading: true};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetchWithAuthorization('loja-api/produtos')
            .then(response => response.json())
            .then(data => this.setState({page: data, isLoading: false}));
    }

    async remove(id) {
        await fetchWithAuthorization(`/loja-api/produtos/${id}`, 'DELETE').then(() => {
            let updatedProdutos = [...this.state.page.elements].filter(p => p.idProduto !== id);
            this.setState({page: {...this.state.page, elements: updatedProdutos}});
        });
    }

    render() {
        const {page, isLoading} = this.state;
        
        if (isLoading) {
            return <p>Carregando...</p>;
        }

        const produtoList = page.elements.map(produto => {
            return <tr key={produto.idProduto}>
                <td style={{whiteSpace: 'nowrap'}}>{produto.nome}</td>
                <td>{produto.descricao}</td>
                <td><img alt={produto.nome} src={"data:image/png;base64, " + produto.imagem} width="10%" /></td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/produtos/" + produto.idProduto}>Editar</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(produto.idProduto)}>Remover</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/produtos/new">Criar Produto</Button>
                    </div>
                    <h3>Cadastro de Produtos</h3>
                    <Table className="mt-4">
                        <thead>
                            <tr>
                                <th width="20%">Nome</th>
                                <th>Descrição</th>
                                <th>Imagem</th>
                                <th width="10%">Ações</th> 
                            </tr>
                        </thead>
                        <tbody>
                            {produtoList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );

    }

}

export default ProdutoList;
