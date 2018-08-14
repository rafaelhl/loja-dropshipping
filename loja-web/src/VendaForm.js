import React, { Component } from 'react';
import { Table, Button, Input, Alert, Container } from 'reactstrap';
import ProdutoFornecedorList from './ProdutoFornecedorList';
import Login from './Login';
import withRouter from 'react-router-dom/withRouter';
import { getUsuarioAutenticado, fetchWithAuthorization } from '.';
import PessoaEdit from './PessoaEdit';

class VendaForm extends Component {

    constructor(props) {
        super(props);
        this.state = {produtosSelecionados: props.produtosSelecionados, removeAction: props.removeAction, fornecedoresProdutos: []};
        this.renderListaFornecedores = this.renderListaFornecedores.bind(this);
        this.handleChangeSelectFornecedor = this.handleChangeSelectFornecedor.bind(this);
        this.handlePessoa = this.handlePessoa.bind(this);
        this.finalizarCompra = this.finalizarCompra.bind(this);
    }

    handleChangeSelectFornecedor(event, fornecedoresProduto) {
        const {produtosSelecionados} = this.state;
        const target = event.target;
        const idFornecedor = target.options[target.options.selectedIndex].id;
        if (!idFornecedor) {
            return;
        }

        let fornecedorProduto = [...fornecedoresProduto].find(f => `${f.fornecedor.idFornecedor}` === idFornecedor);
        let updatedProdutosSelecionados = [];
        [...produtosSelecionados].map(produto => {
            if (produto.idProduto !== fornecedorProduto.produto.idProduto) {
                updatedProdutosSelecionados.push(produto);
                return;
            }
            updatedProdutosSelecionados.push({...produto, fornecedorProduto: fornecedorProduto});
        })
        this.setState({produtosSelecionados: updatedProdutosSelecionados});
    }

    renderListaFornecedores(page) {
        const forncedorOptions = page.elements.map(fornecedorProduto => {
            return <option id={fornecedorProduto.fornecedor.idFornecedor}>{fornecedorProduto.fornecedor.pessoa.nome} - R$ {fornecedorProduto.preco.toLocaleString()}</option>
        })
        return <Input type="select" name="produtos" id="produtos" onChange={(event) => this.handleChangeSelectFornecedor(event, page.elements)} selectedIndex="0">
            <option>Selecione</option>
            {forncedorOptions}
        </Input>
    }

    async finalizarCompra() {
        let produtosVenda = [];
        this.state.produtosSelecionados.map(produto => {
            produtosVenda.push({
                quantidade: produto.quantidade,
                produto: produto.fornecedorProduto
            });
        });
        await fetchWithAuthorization('/loja-api/vendas', 'POST', JSON.stringify({comprador:this.state.comprador, produtos: produtosVenda}));
        this.setState({operacaoRealizada: true, produtosSelecionados: []});
    }

    componentWillReceiveProps(nextProps) {
        const {produtosSelecionados} = this.state;
        const nextProdutosSelecionados = nextProps.produtosSelecionados;
        let updatedProdutosSelecionados = [];
        nextProdutosSelecionados.map(nextProduto => {
            const produtoJaAdicionado = produtosSelecionados.find(p => p.idProduto === nextProduto.idProduto);
            if (produtoJaAdicionado) {
                updatedProdutosSelecionados.push({...produtoJaAdicionado, quantidade: nextProduto.quantidade});
                return;
            }
            updatedProdutosSelecionados.push(nextProduto);
        });
        this.setState({produtosSelecionados: updatedProdutosSelecionados, operacaoRealizada: undefined});
    }

    renderAlertSuccess() {
        const {operacaoRealizada} = this.state;
        if (!operacaoRealizada) {
            return;
        }
        return <Alert color="info">Operação realizada com sucesso!</Alert>
    }

    handlePessoa(pessoa) {
        this.setState({comprador: pessoa});
    }

    render() {
        const {produtosSelecionados, removeAction, comprador} = this.state;
        if (!produtosSelecionados || produtosSelecionados.length === 0) {
            return <div>{this.renderAlertSuccess()}</div>;
        }

        const usuarioAutenticado = getUsuarioAutenticado();
        if (!usuarioAutenticado) {
            return <Login show={produtosSelecionados && produtosSelecionados.length !== 0}/>
        }

        return <Container>
            {usuarioAutenticado.authorization === 'VENDEDOR' ?
                <Container>
                    Dados do Comprador
                    <PessoaEdit item={comprador} handleItem={this.handlePessoa} />
                </Container>
                : ''
            }
            <Table>
                <thead>
                    <tr>
                        <th>Produto</th>
                        <th>Fornecedor</th>
                        <th>Quantidade</th>
                        <th>Valor (R$)</th>
                        <th>Total (R$)</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {produtosSelecionados.map(produto => {
                        return <tr key={produto.idProduto}>
                            <td>{produto.nome}</td>
                            <td><ProdutoFornecedorList idProduto={produto.idProduto} render={this.renderListaFornecedores} /></td>
                            <td>{produto.quantidade}</td>
                            <td>{produto.fornecedorProduto ? produto.fornecedorProduto.preco.toLocaleString() : new Number().toLocaleString()}</td>
                            <td>{produto.fornecedorProduto ? (produto.fornecedorProduto.preco * produto.quantidade).toLocaleString() : new Number().toLocaleString()}</td>
                            <td><Button color="danger" onClick={() => removeAction(produto)}>Remover</Button></td>
                        </tr>
                    })}
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="5">Total: R$ {[...produtosSelecionados].map(p => p.fornecedorProduto ? p.fornecedorProduto.preco * p.quantidade : 0.0).reduce((a, b) => a + b).toLocaleString()}</td>
                        <td><Button color="success" onClick={this.finalizarCompra} >Finalizar compra</Button></td>
                    </tr>
                </tfoot>
            </Table>
        </Container>
    }

}

export default withRouter(VendaForm);