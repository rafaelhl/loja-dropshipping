import React, { Component } from 'react';
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink, Dropdown, DropdownToggle, DropdownMenu, DropdownItem, Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import withRouter from 'react-router-dom/withRouter';
import { removerToken, getUsuarioAutenticado } from '.';
import Login from './Login';

class AppNavbar extends Component {

    initialState = {isOpen: false, dropdownOpen: false, showLogin: false};

    constructor(props) {
        super(props);
        this.state = {...this.initialState};
        this.toggle = this.toggle.bind(this);
        this.renderDropdown = this.renderDropdown.bind(this);
        this.login = this.login.bind(this);
        this.logout = this.logout.bind(this);
        this.renderLoginLogout = this.renderLoginLogout.bind(this);
    }

    componentWillReceiveProps() {
        this.setState({showLogin: false});
    }

    toggle() {
        this.setState(prevState => ({
            isOpen: !this.state.isOpen,
            dropdownOpen: !prevState.dropdownOpen
        }));
    }

    renderDropdown() {
        const jwtAuthentication = getUsuarioAutenticado();
        if (!jwtAuthentication || jwtAuthentication.authorization !== 'ADMIN') {
            return;
        }
        return <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
            <DropdownToggle caret>Cadastro</DropdownToggle>
            <DropdownMenu>
                <DropdownItem tag={Link} to="/pessoas">Cadastro de Pessoas</DropdownItem>
                <DropdownItem divider />
                <DropdownItem tag={Link} to="/produtos">Cadastro de Produtos</DropdownItem>
                <DropdownItem divider />
                <DropdownItem tag={Link} to="/fornecedores">Cadastro de Fornecedores</DropdownItem>
            </DropdownMenu>
        </Dropdown>;
    }

    login() {
        this.setState({showLogin: true});
    }

    logout() {
        removerToken();
        this.props.history.push("/home");
    }

    renderLoginLogout() {
        const jwtAuthentication = getUsuarioAutenticado();
        return <NavItem>
            <NavLink href="#" onClick={!jwtAuthentication ? this.login: this.logout}>{!jwtAuthentication ? 'Entrar' : 'Sair'}</NavLink>
        </NavItem>;
    }

    componentWillReceiveProps() {
        this.setState({showLogin: false});
    }

    render() {
        return <Navbar color="dark" dark expand="md">
            <NavbarBrand tag={Link} to="/home">Home</NavbarBrand>
            {this.renderDropdown()}
            <NavbarToggler onClick={this.toggle}/>
            <Collapse isOpen={this.state.isOpen} navbar>
                <Nav className="ml-auto" navbar>
                    <NavItem>
                        <NavLink href="https://github.com/rafaelhl">GitHub</NavLink>
                    </NavItem>
                    <Login show={this.state.showLogin && !getUsuarioAutenticado()} />
                    {this.renderLoginLogout()}
                </Nav>
            </Collapse>
        </Navbar>;
    }

}

export default withRouter(AppNavbar);