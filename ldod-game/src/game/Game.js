import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import VirtualEdition from './VirtualEdition';
import { getVirtualEditionIndex } from '../utils/APIUtils';
import { Badge} from 'antd';
import { Grid, Jumbotron} from 'react-bootstrap';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
import AppContext, {Consumer} from '../app/AppContext';
export default props => (
    <Consumer>
      {context => <Game {...props} context={context} />}
    </Consumer>
);
class Game extends Component {
    constructor(props, context) {   
        super(props,  context);
        this.state = {
            socket: null,
            currentUsers: 0,
            game: null,
            gameId: null,
            userId: " ",
            isLoading: true,
            dateTime: 0,
        };
        this.connect = this.connect.bind(this);
        this.disconnect = this.disconnect.bind(this);
        this.onMessageReceive = this.onMessageReceive.bind(this);
    }

    async componentDidMount(){
        const gameId = this.props.match.params.id;
        var game = this.props.context.games.find(function(element) {
            return element.gameExternalId === gameId;
        });
        
        this.setState({
           game: game,
           gameId: gameId,
           userId: this.props.context.currentUser.username,
           dateTime: new Date(game.dateTime),
           socket: <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/config']}
                    ref={ (client) => { this.clientRef = client }}
                    onConnect={ () => { this.connect(gameId)}}
                    onDisconnect={ () => {this.disconnect(gameId)}}
                    onMessage={(message) => this.onMessageReceive(message)} /> 
        });
        
    }

    connect(gameId){
        try {
            this.clientRef.sendMessage('/ldod-game/connect', JSON.stringify({ userId: this.props.context.currentUser.username, gameId: gameId}));
            this.clientRef.sendMessage('/ldod-game/register', JSON.stringify({ userId: this.props.context.currentUser.username, gameId: gameId}));
            return true;
          } catch(e) {
            return false;
          }
        
    }

    disconnect(gameId){
        try {
            this.clientRef.sendMessage('/ldod-game/connect', JSON.stringify({ userId: this.props.context.currentUser.username, gameId: gameId, disconnect: "disconnected"}));
            return true;
          } catch(e) {
            return false;
          }
        
    }

    onMessageReceive(message) {
        var users = message[0];
        var command = message[1];
        if(command === "ready"){
            this.setState({
                currentUsers: users,
                isLoading: false,
            })
            return; 
        }
    }


    
    async loadVirtualEdition(){
        this.setState({
            isLoading: true,
        })

        let request = await getVirtualEditionIndex(this.props.context.game.virtualEditionAcronym);

        this.setState({
            virtualEdition: request,
        })
        
    }



    render() {
        if(this.state.isLoading) {
            return (
                <Grid fluid>
                    <Jumbotron  style={{ backgroundColor: 'white' }} >
                        <h2 className="text-center">
                            Loading resources and waiting for users to join...
                        </h2>
                    </Jumbotron>
                    {this.state.socket}
                    <div className="text-center">
                        <i className="fa fa-refresh fa-spin fa-5x fa-fw"></i>
                    </div>
                </Grid>
            );
        }
        return ( 
            <Grid fluid>
                <div>
                    <div>
                        <Badge count={this.state.currentUsers} title="Current online users"  style={{ backgroundColor: '#2ecc71', fontSize: '15px' }}>  
                        <span className="glyphicon glyphicon-user"  style={{ fontSize: '25px', }}></span>
                    </Badge>
                    </div>
                </div>
                <VirtualEdition userId={this.state.userId} gameId={this.state.gameId} game={this.state.game}/>
            </Grid>
    );
  }
}
//export default withRouter(Game);
