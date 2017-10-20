import React, { Component } from 'react';
import logo from './logo.png';
import './css/App.css';
import './css/materialize.css'
import './css/style.css'
import store from './helpers/store'
import auth from './helpers/auth'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import Dialog from 'material-ui/Dialog'
import FlatButton from 'material-ui/FlatButton'
import RaisedButton from 'material-ui/RaisedButton'
import TextField from 'material-ui/TextField'
import _ from 'lodash'
import Avatar from 'material-ui/Avatar'
import Home from './Home'

class App extends Component {
  constructor(props){
    super(props);
    this.state = {
      auth : null,
      open : false,
      error : '',
      dialogState : 'login',
      imageFile : null,
      imageBlob : 'https://www.colorado.edu/ocg/sites/default/files/styles/small/public/people/person-placeholder_34.jpg?itok=XHQXiJa4',
      textFields : {
        email : '',
        password : '',
        userName : ''
      }
    }
  }

  componentWillMount() {
    auth.getSavedUser((user) => {
      this.setState({ auth : user })
    })
  }
  
  componentDidMount(){
  }

  handleOpen = () => {
    this.setState({open: true})
  }

  handleClose = () => {
    this.setState({open: false})
  }

  handleTextChange = (event) => {
    let text = this.state.textFields
    this.setState({ ...this.state, textFields: 
      { ...this.state.textFields,[event.target.id] : event.target.value }}
    )
  }

  handleLogin = () => {
    auth.login(this.state.textFields.email,this.state.textFields.password,(result) => {
      if (result.status == 401){
        this.setState({ error : "Senha incorreta. Verifique sua senha e tente novamente"})
      } else if (result.status == 404){
        this.setState({ error : "Usuário não encontrado. Verifique se essa conta está registrada"})
      } else if (result.status == 200){
        this.setState({ open : false, auth : auth.getUser()})
      }
    })
  }

  handleRegister = () => {
    auth.register(this.state.textFields.email,this.state.textFields.userName,this.state.textFields.password, this.state.imageBlob,(result) => {
      if (result.status == "SUCCESS"){
        this.setState({ open:false, auth: auth.getUser() })
      } else if (result.status == "FAILURE"){
        this.setState({ error : "Ocorreu um problema. Provavelmente um usuário com essas credenciais já existe"})
      }
    })
  }

  switchDialogContent = () => {
    if (this.state.dialogState === 'login'){
      this.setState({ dialogState : 'register' })
    } else {
      this.setState({ dialogState : 'login' })
    }
  }

  onFileChange = () => {
    let file = this.fileInput.files[0]
    let reader  = new FileReader();
    console.log(file)

    reader.addEventListener("load",  () => {
      this.setState({ imageBlob : reader.result, imageFile : file })
      
    }, false);
  
    if (file) {
      reader.readAsDataURL(file);
    }
  }

  render() {
    const actions = []

    const dialogContent = () => {
      if (this.state.dialogState === 'login'){
        return (
          <span> 
            <TextField
                className='col s12 centered'
                id='email'
                floatingLabelText="Email"
                value={this.state.textFields.email}
                onChange={this.handleTextChange}
                type='email'
              />
              <TextField
                className='col s12 centered'
                id='password'
                floatingLabelText="Senha"
                value={this.state.textFields.password}
                onChange={this.handleTextChange}
                type="password"
                onKeyPress={(ev) => {
                  if (ev.key === 'Enter') {
                    ev.preventDefault();
                    this.handleLogin();
                  }
                }}
              />
              <div className='centered col s12' style={{color : 'red'}}>
                {this.state.error}
              </div>
              <div className='centered col s12' style={{marginTop:10}}>
                <FlatButton
                  label="Login" 
                  onClick={this.handleLogin}
                  style={{margin:5,display:'block',margin:'0 auto'}}
                />
              </div>
              <div className='centered col s12'>
                <FlatButton
                  label="Criar conta" 
                  onClick={this.switchDialogContent}
                  style={{margin:5,display:'block',margin:'0 auto'}}
                />
              </div>

          </span>
        )} else {
          return (
            <span> 
              <TextField
                className='col s12 centered'
                floatingLabelText="Email"
                value={this.state.textFields.email}
                onChange={this.handleTextChange}
                type='email'
                id='email'
              />
              <TextField
                className='col s12 centered'
                floatingLabelText="Username"
                value={this.state.textFields.userName}
                onChange={this.handleTextChange}
                type='text'
                errorText={ this.state.textFields.userName.length > 16 ? "O tamanho máximo é 16 caracteres" : null }
                id='userName'
              />
              <TextField
                className='col s12 centered'
                id='password'
                floatingLabelText="Senha"
                value={this.state.textFields.password}
                onChange={this.handleTextChange}
                type="password"
                errorText={ this.state.textFields.password.length > 16 ? "O tamanho máximo é 16 caracteres" : null }
                onKeyPress={(ev) => {
                  if (ev.key === 'Enter') {
                    ev.preventDefault();
                    this.handleRegister();
                  }
                }}
              />
              <Avatar
                className='login-avatar'
                src={this.state.imageBlob} 
                onClick={() => { this.fileInput.click() }}
              />
              <input 
                name="myFile" 
                style={{display : 'none'}} 
                type="file" 
                ref={(input) => { this.fileInput = input }} 
                onChange={this.onFileChange} 
              />
              <div className='centered col s12' style={{color : 'red'}}>
                {this.state.error}
              </div>

              <div className='centered col s12' style={{marginTop:10}}>
                <FlatButton
                  label="Registrar"
                  onClick={this.handleRegister}
                  style={{margin:5,display:'block',margin:'0 auto'}}
                />
                <FlatButton
                  label="Voltar"
                  onClick={this.switchDialogContent}
                  style={{margin:5,display:'block',margin:'0 auto'}}
                />
              </div>
          </span>
          )
        }
    }

    return (
      <MuiThemeProvider>
        { this.state.auth 
        ? <Home/>
        : <div className="App">
          <div className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <h4> Lembraê </h4>
            <p> Nunca mais fique com essa cara </p>
          </div>
          
          <p className="App-intro" style={{margin:20}}>
            Pressione o botão abaixo para fazer Login
          </p>

          <RaisedButton label="LOGIN" onClick={this.handleOpen} />
          <Dialog
            title={_.capitalize(this.state.dialogState)}
            actions={actions}
            modal={false}
            open={this.state.open}
          > 
            {dialogContent()}
          </Dialog>
        </div>
        }
      </MuiThemeProvider>
    );
  }
}

export default App;
