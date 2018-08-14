import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { fetchWithAuthorization } from '.';

class PessoaEdit extends Component {

    emptyItem = {
        cpfCnpj: '',
        nome: '',
        email: '',
        telefone: '',
        usuario: {
            login: '',
            password: ''
        },
    };

    constructor(props) {
        super(props);
        this.state = {
            handleItem: props.handleItem,
            item: (props.item || this.emptyItem)
        };
        this.renderForm = this.renderForm.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleChangeUsuario = this.handleChangeUsuario.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.handleItem) {
            return;
        }
        const isComprador = this.props.match.params.id === 'comprador';
        this.setState({isComprador: isComprador});
        if (this.props.match.params.id !== 'new' && !isComprador) {
            const pessoa = await (await fetchWithAuthorization(`/loja-api/pessoas/${this.props.match.params.id}`)).json();
            this.setState({item: {...pessoa, usuario: {...pessoa.usuario, password: ''}}});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        if (this.state.handleItem) {
            this.state.handleItem(item);
        }
        this.setState({item});
    }

    handleChangeUsuario(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item.usuario[name] = value;
        if (this.state.handleItem) {
            this.state.handleItem(item);
        }
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item, isComprador} = this.state;
        await fetch('/loja-api/pessoas', {
            method: (item.idPessoa) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push(!isComprador ? '/pessoas' : '/home/login');
    }

    componentWillReceiveProps(props) {
        this.setState({item: (props.item || this.state.item)});
    }

    renderForm() {
        const {item, handleItem, isComprador} = this.state;
        const usuario = item.usuario

        const tipoUsuario = <FormGroup>
            <Label for="tipoUsuario">Tipo de Usuário</Label>
            <Input type="select" name="tipoUsuario" id="tipoUsuario" value={usuario.tipoUsuario} onChange={this.handleChangeUsuario} selectedIndex="0">
                <option>COMPRADOR</option>
                <option>VENDEDOR</option>
                <option>ADMIN</option>
            </Input>
        </FormGroup>;

        const footer = <FormGroup>
            <Button color="primary" type="submit">Salvar</Button>{' '}
            <Button color="secondary" tag={Link} to={!isComprador ? '/pessoas' : '/home'}>Cancelar</Button>
        </FormGroup>;

        return <Form onSubmit={this.handleSubmit}>
            <FormGroup>
                <Label for="cpfCnpj">CPF/CNPJ</Label>
                <Input type="text" name="cpfCnpj" id="cpfCnpj" value={item.cpfCnpj || ''}
                    onChange={this.handleChange} autoComplete="cpfCnpj"/>
            </FormGroup>
            <FormGroup>
                <Label for="nome">Nome</Label>
                <Input type="text" name="nome" id="nome" value={item.nome || ''}
                    onChange={this.handleChange} autoComplete="nome"/>
            </FormGroup>
            <FormGroup>
                <Label for="email">Email</Label>
                <Input type="email" name="email" id="email" value={item.email || ''}
                    onChange={this.handleChange} autoComplete="email"/>
            </FormGroup>
            <FormGroup>
                <Label for="telefone">Telefone</Label>
                <Input type="tel" name="telefone" id="telefone" value={item.telefone || ''}
                    onChange={this.handleChange} autoComplete="telefone"/>
            </FormGroup>

            <FormGroup>
                <Label for="login">Usuário</Label>
                <Input type="text"  name="login" id="login" value={usuario.login || ''}
                    onChange={this.handleChangeUsuario} autoComplete="login"/>
            </FormGroup>
            {(!handleItem && !isComprador) ? tipoUsuario : ''}
            <FormGroup>
                <Label for="password">Senha</Label>
                <Input type="password" name="password" id="password" value={usuario.password || ''}
                    onChange={this.handleChangeUsuario} autoComplete="password"/>
            </FormGroup>
            {!handleItem ? footer : ''}
        </Form>
    }

    render() {
        const {item, handleItem, isComprador} = this.state;
        const title = <h2>{item.idPessoa ? 'Editar Pessoa' : !isComprador ? 'Criar Pessoa' : 'Cadastre-se'}</h2>;

        if (handleItem) {
            return this.renderForm();
        }

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                {this.renderForm()}
            </Container>
        </div>        
    }
}

export default withRouter(PessoaEdit);