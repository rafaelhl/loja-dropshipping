import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Container, Card, CardImg, CardBody, CardTitle, CardText, Button, Row, Col, CardSubtitle } from 'reactstrap';
import ProdutoFornecedorList from './ProdutoFornecedorList';
import VendaForm from './VendaForm';
import { getUsuarioAutenticado } from '.';
import Login from './Login';

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {produtos: [], produtosSelecionados: [], isLoading: true};
        this.renderProdutos = this.renderProdutos.bind(this);
        this.comprarProduto = this.comprarProduto.bind(this);
        this.removerProdutoSelecionado = this.removerProdutoSelecionado.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});
        fetch('/loja-api/produtos')
            .then(response => response.json())
            .then(data => this.setState({produtos: data.elements, isLoading: false}));
        this.setState({showLogin: this.props.match.params.path === 'login'});
    }

    comprarProduto(produto) {
        const {produtosSelecionados} = this.state;
        let updatedProdutosSelecionados = produtosSelecionados.slice();
        let produtoAdicionado = updatedProdutosSelecionados.find(p => p.idProduto === produto.idProduto);
        let produtoSelecionado = {};
        if (produtoAdicionado) {
            produtoSelecionado = {...produtoAdicionado, quantidade: produtoAdicionado.quantidade + 1};
            updatedProdutosSelecionados = [...updatedProdutosSelecionados].filter(p => p.idProduto !== produtoAdicionado.idProduto);
        } else {
            produtoSelecionado = {...produto, quantidade: 1};
        }
        updatedProdutosSelecionados.push(produtoSelecionado);
        this.setState({produtosSelecionados: updatedProdutosSelecionados});
    }

    renderFornecedorProdutoPrecos(page) {
        const precosRounded = [...page.elements].map(fornecedorProduto => Math.round(fornecedorProduto.preco));
        const precoMin = [...page.elements].find(fornecedorProduto => Math.round(fornecedorProduto.preco) === Math.min(...precosRounded)).preco;
        const precoMax = [...page.elements].find(fornecedorProduto => Math.round(fornecedorProduto.preco) === Math.max(...precosRounded)).preco;
        if (precoMin === precoMax) {
            return `R$ ${precoMax.toLocaleString()}`;
        }
        const faixaDePreco = [precoMin.toLocaleString(), precoMax.toLocaleString()].join('...');
        return `R$ ${faixaDePreco}`;
    }

    removerProdutoSelecionado(produto) {
        const {produtosSelecionados} = this.state;
        let updatedProdutosSelecionados = [...produtosSelecionados].filter(p => p.idProduto !== produto.idProduto);
        this.setState({produtosSelecionados: updatedProdutosSelecionados});
    }

    componentWillReceiveProps() {
        if (!getUsuarioAutenticado()) {
            this.setState({produtosSelecionados: []});
        }
    }

    renderProdutos() {
        const {produtos, produtosSelecionados, isLoading, showLogin} = this.state;
        if (isLoading) {
            return <p>Carregando...</p>;
        }
        if (produtos.length === 0 && produtosSelecionados.length === 0) {
            return <p>Nenhum produto disponível para compra.</p>;
        }
        return <div>
            <Login show={showLogin}/>
            <Row>
                { 
                    this.state.produtos.map(produto => {
                        return <Col>
                            <Card id={produto.idProduto}>
                                <CardImg top width="100%" src={"data:image/png;base64, " + produto.imagem} alt={produto.nome}/>
                                <CardBody>
                                    <CardTitle>{produto.nome}</CardTitle>
                                    <CardSubtitle><ProdutoFornecedorList idProduto={produto.idProduto} render={this.renderFornecedorProdutoPrecos} /></CardSubtitle>
                                    <CardText>{produto.descricao}</CardText>
                                    <Button color="info" onClick={() => this.comprarProduto(produto)} >Comprar</Button>
                                </CardBody>
                            </Card>
                        </Col>
                    })
                }
            </Row>
            <Row>
                <Col>
                    <VendaForm produtosSelecionados={produtosSelecionados} removeAction={this.removerProdutoSelecionado} />
                </Col>
            </Row>
        </div>
    }

    render() {        
        return (
            <div>
                <AppNavbar/>
                <Container>{this.renderProdutos()}</Container>
            </div>
        )
    }
}
   
export default Home;
