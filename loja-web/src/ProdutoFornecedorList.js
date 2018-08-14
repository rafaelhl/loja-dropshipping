import React, { Component } from 'react';

class ProdutoFornecedorList extends Component {

    constructor(props) {
        super(props);
        this.state = {idFornecedor: props.idFornecedor, idProduto: props.idProduto, renderFornecedorProduto: props.render, page: {}, isLoading: true};
    }

    componentDidMount() {
        this.setState({isLoading: true});

        const {idFornecedor, idProduto} = this.state;
        if (idFornecedor) {
            fetch(`/loja-api/fornecedores/${idFornecedor}/produtos`)
                .then(response => response.json())
                .then(data => this.setState({page: data, isLoading: false}));
            return;
        }

        if (idProduto) {
            fetch(`/loja-api/produtos/${idProduto}/fornecedores`)
                .then(response => response.json())
                .then(data => this.setState({page: data, isLoading: false}));
            return;
        }
    }

    render() {
        const {page, isLoading, renderFornecedorProduto} = this.state;
        if (isLoading) {
            return <p>Carregando...</p>;
        }

        return renderFornecedorProduto(page);
    }

}

export default ProdutoFornecedorList;