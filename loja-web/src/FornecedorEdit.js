import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { fetchWithAuthorization } from '.';
import PessoaEdit from './PessoaEdit';

class FornecedorEdit extends Component {

    emptyItem = {
        pessoa: {
            cpfCnpj: '',
            nome: '',
            email: '',
            telefone: '',
            usuario: {
                login: '',
                password: ''
            }
        }
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            produtos: [],
            produtoSelecionado: undefined,
            produtosAdicionados: [],
            isLoading: true
        };
        this.handleChange = this.handleChange.bind(this);
        this.handlePessoa = this.handlePessoa.bind(this);
        this.handleChangeSelectProduto = this.handleChangeSelectProduto.bind(this);
        this.handleChangeProdutoPreco = this.handleChangeProdutoPreco.bind(this);
        this.adicionarProduto = this.adicionarProduto.bind(this);
        this.removerProduto = this.removerProduto.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);        
    }

    async componentDidMount() {
        fetchWithAuthorization('/loja-api/produtos')
            .then(response => response.json())
            .then(data => this.setState({produtos: data.elements, isLoading: false}));
        const idFornecedor = this.props.match.params.id;
        if (idFornecedor !== 'new') {
            const fornecedor = await (await fetchWithAuthorization(`/loja-api/fornecedores/${idFornecedor}`)).json();
            this.setState({item: fornecedor});
            const produtosFornecedor = await (await fetchWithAuthorization(`/loja-api/fornecedores/${idFornecedor}/produtos`)).json();
            let produtosAdicionados = [];
            produtosFornecedor.elements.forEach(pf => produtosAdicionados.push({...pf.produto, preco: pf.preco}));
            let updatedProdutos = [...this.state.produtos].filter(p => produtosAdicionados.find(pa => pa.idProduto === p.idProduto) === undefined);
            this.setState({produtos: updatedProdutos, produtosAdicionados: produtosAdicionados});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    handlePessoa(pessoa) {
        const {item} = this.state;
        this.setState({item: {...item, pessoa: pessoa}});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item, produtosAdicionados} = this.state;
        if (produtosAdicionados.length == 0) {
            return;
        }

        await fetchWithAuthorization('/loja-api/fornecedores', (item.idFornecedor) ? 'PUT' : 'POST', JSON.stringify(item))
                    .then(response => response.json()).then(fornecedor => this.salvarProdutos(item, fornecedor, produtosAdicionados));
        this.props.history.push('/fornecedores');
    }

    async salvarProdutos(item, fornecedor, produtosAdicionados) {
        let produtosFornecedor = [];
        produtosAdicionados.map(produto => {
            produtosFornecedor.push({
                produto: produto,
                fornecedor: fornecedor,
                preco: produto.preco
            });
        });
        await fetchWithAuthorization(`/loja-api/fornecedores/${fornecedor.idFornecedor}/produtos`, (item.idFornecedor) ? 'PUT' : 'POST',  JSON.stringify(produtosFornecedor));
    }

    handleChangeSelectProduto(event) {
        const target = event.target;
        const idProduto = target.options[target.options.selectedIndex].id;
        if (!idProduto) {
            this.setState({produtoSelecionado: undefined});
            return;
        }

        let produto = [...this.state.produtos].find(p => p.idProduto == idProduto);
        this.setState({produtoSelecionado: produto});
    }

    handleChangeProdutoPreco(event, idProduto) {
        const {produtosAdicionados} = this.state;
        const target = event.target;
        const value = target.value;
        let updatedProdutosAdicionados = [] 
        produtosAdicionados.forEach(produto => {
            if (produto.idProduto === idProduto){
                updatedProdutosAdicionados.push({...produto, preco: value});
                return;
            }
            updatedProdutosAdicionados.push(produto);
        });
        this.setState({produtosAdicionados: updatedProdutosAdicionados});
    }

    adicionarProduto() {
        const {produtos, produtoSelecionado, produtosAdicionados} = this.state;
        if (!produtoSelecionado) {
            return;
        }

        let updatedProdutosSelecionados = produtosAdicionados.slice();
        updatedProdutosSelecionados.push(produtoSelecionado);
        let updatedProdutos = [...produtos].filter(p => p.idProduto !== produtoSelecionado.idProduto);
        this.setState({produtos: updatedProdutos, produtosAdicionados: updatedProdutosSelecionados, produtoSelecionado: undefined});
    }

    removerProduto(idProduto) {
        const {produtos, produtosAdicionados} = this.state;

        let produtoRemovido = [...produtosAdicionados].find(p => p.idProduto === idProduto);
        let updatedProdutos = produtos.slice();
        updatedProdutos.push(produtoRemovido);        
        let updatedProdutosSelecionados = [...produtosAdicionados].filter(p => p.idProduto !== idProduto);
        this.setState({produtos: updatedProdutos, produtosAdicionados: updatedProdutosSelecionados, produtoSelecionado: undefined});
    }

    render() {
        const {item, produtosAdicionados, produtos} = this.state;
        const title = <h2>{item.idFornecedor ? 'Editar Fornecedor' : 'Criar Fornecedor'}</h2>;

        const produtoOptions = produtos.map(produto => {
            return <option id={produto.idProduto}>{produto.nome}</option>
        });

        let produtosTbody = produtosAdicionados.map(produto => {
            return <tr>
                <td>{produto.nome}</td>
                <td><Input type="text" name="preco" value={produto.preco || ''} onChange={(event) => this.handleChangeProdutoPreco(event, produto.idProduto)} /></td>
                <td><Button color="danger" size="sm" onClick={() => this.removerProduto(produto.idProduto)}>Remover</Button></td>
            </tr>
        });

        if (produtosTbody.length == 0) {
            produtosTbody = <tr>
                <td colspan="3">Nenhum produto adicionado.</td>
            </tr>
        }

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <PessoaEdit item={item.pessoa} handleItem={this.handlePessoa} />
                    <FormGroup>
                        <Label for="produtos">Produtos</Label>
                        <Input type="select" name="produtos" id="produtos" onChange={this.handleChangeSelectProduto} selectedIndex="0">
                            <option>Selecione</option>
                            {produtoOptions}
                        </Input>
                    </FormGroup>
                    <FormGroup>
                        <FormGroup>
                            <Button className="clearfix " outline color="primary" onClick={this.adicionarProduto}>Adicionar</Button>
                        </FormGroup>
                        <FormGroup>
                            <Label>Produtos Adicionados</Label>
                            <Table striped>
                                <thead>
                                    <tr>
                                        <th>Nome</th>
                                        <th>Preço</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {produtosTbody}
                                </tbody>
                            </Table>
                        </FormGroup>
                    </FormGroup>
                    
                    <FormGroup>
                        <Button color="primary" type="submit">Salvar</Button>{' '}
                        <Button color="secondary" tag={Link} to="/fornecedores">Cancelar</Button>
                    </FormGroup>
                        
                </Form>
            </Container>
        </div>        
    }
}

export default withRouter(FornecedorEdit);