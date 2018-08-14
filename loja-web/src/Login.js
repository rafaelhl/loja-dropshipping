import React, { Component } from 'react';
import { withRouter, Redirect } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label, Modal, ModalHeader, ModalBody, ModalFooter, Alert } from 'reactstrap';
import { salvarUsuarioAutenticado } from '.';

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            redirectToCadastroPessoa: false,
            modal: props.show,
            usuario: {
                login: '',
                password: ''
            }
        }
        this.handleChange = this.handleChange.bind(this);
        this.login = this.login.bind(this);
        this.toggle = this.toggle.bind(this);
        this.cadastroUsuario = this.cadastroUsuario.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let usuario = {...this.state.usuario};
        usuario[name] = value;
        this.setState({usuario});
    }

    async login() {
        const {usuario} = this.state;
        const jwtAuthentication = await (await fetch('/loja-api/auth/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuario)
        })).json();
        if (!jwtAuthentication || jwtAuthentication.message) {
            this.setState({message: jwtAuthentication.message});
            return;
        }
        this.setState({message: undefined});
        salvarUsuarioAutenticado(jwtAuthentication);
        this.props.history.push('/home');
        this.setState({modal: false});
    }

    componentWillReceiveProps(nextProps) {
        this.setState({modal: nextProps.show});
    }

    toggle() {
        this.setState({modal: !this.state.modal});
    }

    cadastroUsuario() {
       this.setState({redirectToCadastroPessoa: true});
    }

    render() {
        const {usuario, message, modal, redirectToCadastroPessoa} = this.state;
        return <Modal isOpen={modal} toggle={this.toggle}>
                {message ? <Alert color="danger">{message}</Alert> : ''}
                <ModalHeader toggle={this.toggle}>Entrar</ModalHeader>
                <ModalBody>
                    <FormGroup>
                        <Label for="login">Usuário</Label>
                        <Input type="text" name="login" id="login" 
                            value={usuario.login || ''} onChange={this.handleChange} />
                    </FormGroup>
                    <FormGroup>
                        <Label for="password">Senha</Label>
                        <Input type="password" name="password" id="password" 
                            value={usuario.password || ''} onChange={this.handleChange} />
                    </FormGroup>                    
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" type="button" onClick={this.login}>Entrar</Button>{' '}
                    {redirectToCadastroPessoa ? <Redirect to='/pessoas/comprador'/> : ''}
                    <Button color="info" type="button" onClick={this.cadastroUsuario}>Cadastre-se</Button>{' '}
                    <Button color="secondary" onClick={this.toggle}>Cancelar</Button>
                </ModalFooter>
        </Modal>
    }

}

export default withRouter(Login);