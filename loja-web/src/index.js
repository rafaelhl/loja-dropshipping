import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';

export function salvarUsuarioAutenticado(jwtAuthentication) {
    localStorage.setItem('jwtAuthentication', JSON.stringify(jwtAuthentication));
}

export function getUsuarioAutenticado() {
    var usuarioAutenticado = JSON.parse(localStorage.getItem('jwtAuthentication'));
    if (!usuarioAutenticado || usuarioAutenticado.message) {
        return undefined;
    }
    return usuarioAutenticado;
}

export function fetchWithAuthorization(url, method, body) {
    const jwtAuthentication = getUsuarioAutenticado();
    if (!jwtAuthentication) {
        window.location.href = '/home/login';
        return;
    }
    return fetch(url, {
        method: method || 'GET',
        headers: {
            'Authorization': `${jwtAuthentication.tokenType} ${jwtAuthentication.accessToken}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: body,
    }).then((response) => {
        if (response.status === 401) {
            removerToken();
            window.location.href = '/home/login';
            return;
        }
        return response;
    });
}

export function removerToken() {
    localStorage.removeItem('jwtAuthentication');
}

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
