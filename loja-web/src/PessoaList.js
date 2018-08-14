import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { fetchWithAuthorization } from '.';

class PessoaList extends Component {

    constructor(props) {
        super(props);
        this.state = {page: {}, isLoading: true};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetchWithAuthorization('loja-api/pessoas')
            .then(response => response.json())
            .then(data => this.setState({page: data, isLoading: false}));
    }

    async remove(id) {
        await fetchWithAuthorization(`/loja-api/pessoas/${id}`, 'DELETE').then((response) => {
            if (!response.ok) {
                return;
            }
            let updatedPessoas = [...this.state.page.elements].filter(p => p.idPessoa !== id)
            this.setState({page: {...this.state.page, elements: updatedPessoas}});
        });
    }

    render() {
        const {page, isLoading} = this.state;

        if (isLoading) {
            return <p>Carregando...</p>;
        }

        const pessoaList = page.elements.map(pessoa => {
            return <tr key={pessoa.idPessoa}>
                <td>{pessoa.cpfCnpj}</td>
                <td style={{whiteSpace: 'nowrap'}}>{pessoa.nome}</td>
                <td>{pessoa.email}</td>
                <td>{pessoa.usuario.login}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/pessoas/" + pessoa.idPessoa}>Editar</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(pessoa.idPessoa)}>Remover</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/pessoas/new">Cadastrar Pessoa</Button>
                    </div>
                    <h3>Cadastro de Pessoas</h3>
                    <Table className="mt-4">
                        <thead>
                            <tr>
                                <th>CPF/CNPJ</th>
                                <th width="20%">Nome</th>
                                <th>Email</th>
                                <th>Login</th>
                                <th width="10%">Ações</th> 
                            </tr>
                        </thead>
                        <tbody>
                            {pessoaList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );

    }

}

export default PessoaList;