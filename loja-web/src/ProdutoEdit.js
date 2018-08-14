import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, FormText, Input, Label, Media } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { fetchWithAuthorization } from '.';

class ProdutoEdit extends Component {

    emptyItem = {
        nome: '',
        descricao: '',
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleChangeFile = this.handleChangeFile.bind(this);
        this.readFileAsBase64 = this.readFileAsBase64.bind(this);
        this.renderProdutoImagem = this.renderProdutoImagem.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const produto = await (await fetchWithAuthorization(`/loja-api/produtos/${this.props.match.params.id}`)).json();
            this.setState({item: produto});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.changeItemValue(name, value);
    }

    handleChangeFile(event) {
        const target = event.target;
        this.readFileAsBase64(target.name, target.files[0]);      
    }

    readFileAsBase64(name, file) {
        if (!window.FileReader) {
            return;
        }
        let reader = new FileReader();
        reader.onloadend = (() => this.changeItemValue(name, reader.result.split(',')[1]));
        reader.readAsDataURL(file);
    }

    changeItemValue(name, value) {
        let item = { ...this.state.item };
        item[name] = value;
        console.log(item);
        this.setState({ item });
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        await fetchWithAuthorization('/loja-api/produtos', (item.idProduto) ? 'PUT' : 'POST', JSON.stringify(item));
        this.props.history.push('/produtos');
    }

    renderProdutoImagem(produto) {
        if (!produto.imagem) {
            return <FormText color="muted">Selecione uma imagem do produto</FormText>;
        }

        return <img alt={produto.nome} src={"data:image/png;base64, " + produto.imagem} width="30%" />;
    }

    render() {
        const item = this.state.item;
        const title = <h2>{item.idProduto ? 'Editar Produto' : 'Criar Produto'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="nome">Nome</Label>
                        <Input type="text" name="nome" id="nome" value={item.nome || ''}
                            onChange={this.handleChange} autoComplete="nome"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="descricao">Descrição</Label>
                        <Input type="text" name="descricao" id="descricao" value={item.descricao || ''}
                            onChange={this.handleChange} autoComplete="descricao"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="imagem">Imagem</Label>
                        <Input type="file" name="imagem" id="imagem" onChange={this.handleChangeFile} />
                        {this.renderProdutoImagem(item)}
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Salvar</Button>{' '}
                        <Button color="secondary" tag={Link} to="/produtos">Cancelar</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>        
    }
}

export default withRouter(ProdutoEdit);