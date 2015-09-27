class Setup {
  isComplete() {
    return this.getScriptorToken() !== null;
  }

  getScriptorToken() {
    return localStorage.getItem('scriptor:api_key');
  }

  getScriptorHost() {
     return localStorage.getItem('scriptor:api_host');
  }

  setScriptorToken(token) {
    localStorage.setItem('scriptor:api_key', token);
  }

  setScriptorHost(host) {
    localStorage.setItem('scriptor:api_host', host);
  }
}

export default new Setup();
