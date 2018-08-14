import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ProdutoList from './ProdutoList';
import ProdutoEdit from './ProdutoEdit';
import PessoaList from './PessoaList';
import PessoaEdit from './PessoaEdit';
import FornecedorList from './FornecedorList';
import FornecedorEdit from './FornecedorEdit';
import FornecedorIntegracaoForm from './FornecedorIntegracaoForm';

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/" exact={true} component={Home}/>
          <Route path="/home" exact={true} component={Home}/>
          <Route path="/home/:path" component={Home}/>
          <Route path="/produtos" exact={true} component={ProdutoList}/>
          <Route path='/produtos/:id' component={ProdutoEdit}/>
          <Route path='/pessoas' exact={true} component={PessoaList}/>
          <Route path='/pessoas/:id' component={PessoaEdit}/>
          <Route path='/fornecedores' exact={true} component={FornecedorList}/>
          <Route path='/fornecedores/:id' component={FornecedorEdit}/>
          <Route path='/integracoes/fornecedor/:id' component={FornecedorIntegracaoForm}/>
        </Switch>
      </Router>
    );
  }
}

export default App;
