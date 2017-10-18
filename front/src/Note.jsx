import React, { Component } from 'react';
import ContentEditable from "react-contenteditable";
import auth from './helpers/auth.js'
import _ from 'lodash'
import FontIcon from 'material-ui/FontIcon';
import IconButton from 'material-ui/IconButton';
import Popover from 'material-ui/Popover';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import moment from 'moment'
import ReactMarkdown from'react-markdown';

const debounce = require('lodash/debounce');
const omit = require('lodash/omit')
const spotifyUri = require('spotify-uri');

class Note extends Component {
    constructor(props) {
        super(props);
        this.state = {
            ...this.props.info,
            open: false,
            anchorEl: null,
            showEditableContent: false,
        }

        this.exists = true
        this.delayedUpdate = debounce(this.update, 3000)
    }

    componentWillMount(){
        let parsed, uri;
        let conteudo = this.state.content
        if (/((?:https\:\/\/)|(?:http\:\/\/)|(?:www\.))?([a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\??)[a-zA-Z0-9\-\._\?\,\'\/\\\+&%\$#\=~]+)/.test(conteudo)) { /* return true */ 
            console.log("URL detectada");

            var source = (conteudo || '').toString();
            var urlArray = [];
            var url;
            var matchArray;
            var regexToken = /(((ftp|https?):\/\/)[\-\w@:%_\+.~#?,&\/\/=]+)|((mailto:)?[_.\w-]+@([\w][\w\-]+\.)+[a-zA-Z]{2,3})/g;
            // Iterate through any URLs in the text.
            while( (matchArray = regexToken.exec( source )) !== null ){
                var token = matchArray[0];
                urlArray.push( token );
            }
            console.log("URL extraida");
            var url2 = urlArray[0]
            var url1 = urlArray[0]
            if(/^(spotify:|https:\/\/[a-z]+\.spotify\.com\/)/.test(url2)){
                console.log("Spotify URL detectada")
                parsed = spotifyUri.parse(url2);
                uri = spotifyUri.formatEmbedURL(parsed);
                this.setState({ content : '<iframe src="' + uri + '" width="200" height="100" frameborder="0" allowtransparency="true"></iframe>'})
            } else {
                //this.setState({ content : '<iframe src="' + url1 + '" width = "200px" height = "200px"></iframe>'})
                // <a href="http://en.wikipedia.org/">Wikipedia</a><div class="box"><iframe src="http://en.wikipedia.org/" width = "500px" height = "500px"></iframe></div> 
                this.setState({ content : '<a href="'+url1+'">'+url+'</a>'})
            }
        } else {
            console.log("URL não detectada");
            this.setState({ content : this.state.content})
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState(nextProps.info)
        
    }

    handleOptionsOpen = (event) => {
        // This prevents ghost click.
        event.preventDefault();
        this.setState({
            open: true,
            anchorEl: event.currentTarget,
        })
    }

    handleOptionsClose = () => {
        this.setState({ open: false })
    }


    handleChange = (e) => {
        e.preventDefault()
        if (this.state[e.currentTarget.id] != e.target.value) {
            this.setState({ [e.currentTarget.id]: e.target.value });
            this.delayedUpdate()
        }
    }

    handleLockPress = () => {
        this.setState({ isPrivate: !this.state.isPrivate })
        this.delayedUpdate()
    }

    update = () => {
        if (this.exists) {
            this.setState({ showEditableContent: false })
            let updatedState = omit(this.state, ['open', 'anchorEl'])
            this.props.update(updatedState)
        }
    }

    handleRemove = () => {
        this.exists = false
        this.props.remove(this.props.info.id)
    }

    render() {
        const popOver = () => {
            return (
                <Popover
                    open={this.state.open}
                    anchorEl={this.state.anchorEl}
                    anchorOrigin={{ horizontal: 'left', vertical: 'bottom' }}
                    targetOrigin={{ horizontal: 'left', vertical: 'top' }}
                    onRequestClose={this.handleOptionsClose}
                >
                    <Menu>
                        <MenuItem
                            leftIcon={<FontIcon className="material-icons" > delete_forever </FontIcon>}
                            primaryText="Remover nota"
                            onClick={this.handleRemove} />

                        <MenuItem
                            onClick={this.handleLockPress}
                            leftIcon={
                                <FontIcon className="material-icons" >
                                    {this.state.isPrivate ? 'lock' : 'lock_open'}
                                </FontIcon>
                            }
                            primaryText={this.state.isPrivate ? "Tornar pública" : "Tornar privada"}
                        />

                    </Menu>
                </Popover>
            )
        }

        return (
            <div className='grid-item col s6 m4 l3'>
                <div className='card multiline'
                    style={{ backgroundColor: this.state.color, padding: 10 }}
                >
                    <div className='card-content'>
                        <div className="card-title">
                            <ContentEditable
                                id="title"
                                html={this.state.title}
                                disabled={auth.getUser().id === this.state.userId ? false : true}
                                onChange={this.handleChange}
                                style={{ wordWrap: 'break-word' }}
                            />
                            {auth.getUser().id === this.state.userId
                                ?
                                <IconButton onClick={this.handleOptionsOpen}>
                                    <FontIcon className="material-icons" > more_vert </FontIcon>
                                </IconButton>
                                : null
                            }
                            {popOver()}
                        </div>

                        {this.state.showEditableContent ?
                            <ContentEditable
                                id="content"
                                html={this.state.content}
                                disabled={auth.getUser().id === this.state.userId ? false : true}
                                onChange={this.handleChange}
                                className='note-content'
                            />
                            :
                            <div onClick={() => this.setState({ showEditableContent: true })}>
                                <ReactMarkdown source={this.state.content} />
                            </div>
                        }




                        <div className='divider' />
                        <div className='card-footer'>
                            {moment(this.state.updatedAt).isSame(moment(this.state.createdAt))
                                ? <span> Criado por <b>{this.state.ownerUsername} </b> {moment(this.state.createdAt).fromNow()} </span>
                                : <span> Atualizado por <b>{this.state.ownerUsername}</b> {moment(this.state.updatedAt).fromNow()} </span>
                            }
                            <div style={{ textAlign: 'right' }}>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Note;