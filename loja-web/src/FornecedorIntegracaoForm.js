import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label, Table, Col, Row } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { fetchWithAuthorization } from '.';

class FornecedorIntegracaoForm extends Component {

    emptyItem = {
        url: '',
        mensagem: '',
        cabecalhos: [],
        cabecalho: {
            chave: '',
            valor: ''
        }
    }

    constructor(props) {
        super(props);
        this.state = {
            idFornecedor: props.match.params.id,
            item: this.emptyItem
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleChangeCabecalho = this.handleChangeCabecalho.bind(this);
        this.adicionarCabecalho = this.adicionarCabecalho.bind(this);
        this.removerCabecalho = this.removerCabecalho.bind(this);
    }

    async componentDidMount() {
        const {idFornecedor} = this.state;
        await fetchWithAuthorization(`/loja-api/fornecedores/${idFornecedor}/integracoes`).then((response) => {
            if (!response.ok) {
                return;
            }
            return response.json();
        }).then(item => this.setState({item: {...this.state.item, ...item}}));
    }

    componentWillReceiveProps(props) {
        this.setState({idFornecedor: (props.match.params.id || this.state.idFornecedor)});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item, idFornecedor} = this.state;
        await fetchWithAuthorization(`/loja-api/fornecedores/${idFornecedor}/integracoes`, 'POST', JSON.stringify(item));
        this.props.history.push('/fornecedores');
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    handleChangeCabecalho(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item.cabecalho[name] = value;
        this.setState({item});
    }

    adicionarCabecalho() {
        const {item} = this.state;
        const cabecalho = item.cabecalho;
        if (!cabecalho.chave || cabecalho.chave === '') {
            return;
        }
        let cabecalhos = item.cabecalhos.slice();
        const cabecalhoInserido = cabecalhos.find(c => c.chave === cabecalho.chave);
        if (cabecalhoInserido) {
            return;
        }        
        cabecalhos.push(item.cabecalho);
        this.setState({item: {...item, cabecalhos: cabecalhos, cabecalho: {chave: '', valor: ''}}});
    }

    removerCabecalho(cabecalho) {
        const {item} = this.state;
        let cabecalhos = item.cabecalhos.slice().filter(c => c.chave !== cabecalho.chave);
        this.setState({item: {...item, cabecalhos: cabecalhos}});
    }

    render() {
        const {item} = this.state;

        let cabecalhoTbody = item.cabecalhos.map(cabecalho => {
            return <tr>
                <td>{cabecalho.chave}</td>
                <td>{cabecalho.valor}</td>
                <td><Button color="danger" size="sm" onClick={() => this.removerCabecalho(cabecalho)}>Remover</Button></td>
            </tr>
        })

        if (item.cabecalhos.length === 0) {
            cabecalhoTbody = <tr>
                <td colspan="3">Nenhum cabeçalho adicionado.</td>
            </tr>
        }

        return <div>
            <AppNavbar/>
            <Container>
                <h2>Configurar Integração</h2>
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="url">URL</Label>
                        <Input type="text" name="url" id="url" onChange={this.handleChange} 
                            value={item.url || ''} autoComplete="url"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="mensagem">Mensagem</Label>
                        <Input type="textarea" name="mensagem" id="mensagem" onChange={this.handleChange} 
                            value={item.mensagem || ''} autoComplete="mensagem"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="chave">Cabeçalho</Label>
                        <Row>
                            <Col sm={5}>
                                <Input type="text" name="chave" id="chave" onChange={this.handleChangeCabecalho} 
                                    value={item.cabecalho.chave || ''} placeholder="Chave"/>
                            </Col>
                            <Col sm={5}>
                                <Input type="text" name="valor" id="valor" onChange={this.handleChangeCabecalho} 
                                    value={item.cabecalho.valor || ''} placeholder="Valor"/>
                            </Col>
                        </Row>
                    </FormGroup>
                    <FormGroup>
                        <FormGroup>
                            <Button className="clearfix " outline color="primary" onClick={this.adicionarCabecalho}>Adicionar</Button>
                        </FormGroup>
                        <FormGroup>
                            <Label>Cabeçalhos</Label>
                            <Table>
                                <thead>
                                    <tr>
                                        <th>Chave</th>
                                        <th>Valor</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {cabecalhoTbody}
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

export default withRouter(FornecedorIntegracaoForm);