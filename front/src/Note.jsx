import React, { Component } from 'react';
import {List, ListItem} from 'material-ui/List';
import ContentEditable from "react-contenteditable";
import TextField from 'material-ui/TextField'
import auth from './helpers/auth.js'
import _ from 'lodash'
import FontIcon from 'material-ui/FontIcon';
import IconButton from 'material-ui/IconButton';
import Popover from 'material-ui/Popover';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import moment from 'moment'
import ReactMarkdown from'react-markdown';
import YouTube from 'react-youtube';

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
            showComments: false,
            typedCommentary: '',
            youtubeArray: []
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
            var url = urlArray[0]
            if(/^(spotify:|https:\/\/[a-z]+\.spotify\.com\/)/.test(url)){
                console.log("Spotify URL detectada")
                parsed = spotifyUri.parse(url);
                uri = spotifyUri.formatEmbedURL(parsed);
                this.setState({ content : '<iframe src="' + uri + '" width="200" height="100" frameborder="0" allowtransparency="true"></iframe>'})
            } else {
                this.setState({ content : '<a href="' + url + '">'+ url +'</a><div class="box"><iframe src="'+ url +'" width = "100%" height = "100%"></iframe></div>'}) 
            }
        } else {
            console.log("URL não detectada");
            this.setState({ content : this.state.content})
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState(nextProps.info)
        
    }

    parseYoutubeIds(){
        
        
        let patt = new RegExp('(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/)([^"&?\/ ]{11})')

        let findId = patt.exec(this.state.content)

        console.log(findId,this.state)

        if(findId != null){
            let newYoutubeArray = this.state.youtubeArray
            newYoutubeArray.push(findId[1])
            this.setState({youtubeArray : newYoutubeArray})
        }
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
            this.setState({lastUser: auth.getUser().username})
            this.delayedUpdate()
            
        }
    }

    handleLockPress = () => {
        this.setState({ isPrivate: !this.state.isPrivate })
        this.delayedUpdate()
    }

    handleConclusion = () => {
        this.setState({ isConcluded: !this.state.isConcluded })
        this.delayedUpdate()
    }

    handleShowComments = () => {
        this.setState({ showComments: !this.state.showComments })
    }

    handleCommentaryChange = (event) => {
        this.setState({
            typedCommentary : event.target.value,
        });
    };

    handleCommentaryClick = (index) => {
        var jsonstr = this.state.commentary
        var obj = JSON.parse(jsonstr)
        obj.commentary.splice(index, 1)
        jsonstr = JSON.stringify(obj)
        this.setState({ 
            commentary : jsonstr,
        })
        this.delayedUpdate()
    };

    handleCommentarySubmit = (event) => {
        var jsonstr = this.state.commentary
        var obj = JSON.parse(jsonstr)
        obj["commentary"].push({"content":this.state.typedCommentary,"user":auth.getUser().username})
        jsonstr = JSON.stringify(obj)
        this.setState({ 
            commentary : jsonstr,
            typedCommentary : ''
        })
        this.delayedUpdate()
    }


    update = () => {
        if (this.exists) {
            let updatedState = omit(this.state,['open','anchorEl'])
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
                    {auth.getUser().id === this.state.userId ?
                        <div>
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
                            primaryText={this.state.isPrivate ? "Tornar pública" : "Tornar privada"} />

                        <MenuItem
                            leftIcon={<FontIcon className="material-icons" > check </FontIcon>}
                            primaryText={this.state.isConcluded ? "Resumir Nota" : "Concluir Nota"}
                            onClick={this.handleConclusion} />
                        </div>
                        : null
                    }
                    {this.state.isPrivate ? null :
                        <MenuItem
                            leftIcon={<FontIcon className="material-icons" > commentary </FontIcon>}
                            primaryText={this.state.showComments ? "Esconder Comentários" : "Mostrar Comentários"}
                            onClick={this.handleShowComments} />
                    }
                    </Menu>
                </Popover>
            )
        }

        if (this.state.isConcluded === true) {
            var checked = <FontIcon className="material-icons" color="green" > check </FontIcon>;
        } else {
            var checked = '';
        }
        
        const opts = {
            height: '200',
            width: '200',
            playerVars: { // https://developers.google.com/youtube/player_parameters
              autoplay: 0
            }
        }

        var obj = JSON.parse(this.state.commentary);
        var commentary = obj.commentary;

        return (
            <div className='grid-item col s6 m4 l3'>
                <div className='card multiline'
                    style={{ backgroundColor: this.state.color, padding: 10 }}
                >
                    <div className='card-content'>
                        <div className="card-title">
                            {checked}
                            <ContentEditable
                                id="title"
                                html={this.state.title}
                                disabled={this.state.isPrivate}
                                onChange={this.handleChange}
                            />
                            <IconButton onClick={this.handleOptionsOpen}>
                                <FontIcon className="material-icons" > more_vert </FontIcon>
                            </IconButton>
                            {popOver()}
                        </div>

                        {this.state.showEditableContent ?
                            <ContentEditable
                                id="content"
                                html={this.state.content}
                                disabled={this.state.isPrivate}
                                onChange={this.handleChange}
                                className='note-content'
                            />
                            :
                            <div onClick={() => this.setState({ showEditableContent: true })}>
                                <ReactMarkdown source={this.state.content} />
                            </div>
                        }
                        {this.state.youtubeArray.map((url) => <YouTube videoId={url} opts={opts}/>)}




                        <div className='divider' />
                        <div className='card-footer'>
                            {moment(this.state.updatedAt).isSame(moment(this.state.createdAt))
                                ? <span> Criado por <b>{this.state.ownerUsername} </b> {moment(this.state.createdAt).fromNow()} </span>
                                : <span> Atualizado por <b>{this.state.lastUser}</b> {moment(this.state.updatedAt).fromNow()} </span>
                            }
                            <div style={{ textAlign: 'right' }}>
                            </div>
                        </div>
                        {this.state.showComments ? <div className='divider' /> : null }
                        {this.state.showComments ?
                        <List>
                            {commentary.map((comment, index) => 
                            <ListItem key={'list-'+index} primaryText={comment.user} secondaryText={comment.content} onClick={this.handleCommentaryClick.bind(this, index)} />)}
                            <TextField
                                id="text-field-controlled"
                                value={this.state.typedCommentary}
                                onChange={this.handleCommentaryChange}
                                className='note-input-commentary' //fazer no css
                                hintText='Insira um comentário...'
                                underlineStyle={{width:95+'%',color:'#808080'}}
                                underlineFocusStyle={{width:95+'%',borderColor:'#808080'}}
                            />
                            <IconButton style={{position: "absolute", bottom: 0, right: 0}} onClick={this.handleCommentarySubmit}>
                                <FontIcon className="material-icons" >add</FontIcon>
                            </IconButton>
                        </List> : null }

                    </div>
                </div>
            </div>
        );
    }
}

export default Note;