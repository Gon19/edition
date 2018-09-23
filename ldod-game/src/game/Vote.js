import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL, SUBSCRIBE_URL, APP_PREFIX} from '../utils/Constants';
import './Vote.css';
import { Table} from 'react-bootstrap';
import SockJsClient from 'react-stomp';
import AppContext from '../app/AppContext';
import {Provider} from '../app/AppContext';

class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null,
            votes: [],
            previousVote: null,
            previousScore: null,
        };
        this.handleVote = this.handleVote.bind(this);
        this.handleMessageVote = this.handleMessageVote.bind(this);
    }
    
    componentDidMount(){
        this.setState({
            socket:  <SockJsClient
                        url={WEB_SOCKETS_URL}
                        topics={[SUBSCRIBE_URL + this.props.gameId +'/votes']}
                        ref={ (client) => { this.clientRef = client }}
                        onMessage={(message) => this.handleMessageVote(message)} />,
            votes: this.props.initialTags,
            seconds: this.props.seconds,
            topTag: this.props.topTag,
            winner: this.props.winner,
        })
        this.interval = setInterval(() => this.tick(), 1000);
    }

    componentWillUnmount() {
        clearInterval(this.interval);
        <AppContext>
            <Provider value={{winner: this.state.winner, top: this.state.topTag}}>  
                {this.props.children}
            </Provider>
        </AppContext>
    }

    componentDidUpdate(prevProps) {
        if (this.props.seconds !== prevProps.seconds) {
            this.setState({
                seconds: this.props.seconds,
                topTag: this.props.topTag,
            })
        }
    }
  
    tick() {
        this.setState(prevState => ({
          seconds: prevState.seconds - 1
        }));
    }

    handleVote = (param) => (e) =>{
        let vote;
        if (e.target.checked) {
            vote =  Math.round(1.0 + this.state.seconds/10);
        } else {
            vote = Math.round(-1.0 - this.state.seconds/10);
        }
        var res = vote.toFixed(2);
        this.sendMessage(param.tag, res); 
    }

    sendMessage = (msg, vote) => {
        try {
          this.clientRef.sendMessage(APP_PREFIX + this.props.gameId + '/votes', JSON.stringify({ gameId: this.props.gameId, voterId: this.props.userId, msg: msg, vote: vote}));
          return true;
        } catch(e) {
          return false;
        }
    }

    handleMessageVote(message) {
        var dictionary = this.state.votes;
        let copy = [...this.state.votes];
        var vote = Math.round(parseFloat(message[3]));
        var temp = { authorId: message[1], tag: message[2], vote: vote};
        for(var i in dictionary){
            if(dictionary[i].tag === temp.tag){
                copy.splice(i, 1, temp);
                this.setState(({
                    votes: copy,
                    topTag: message[4],
                    winner: message[5],
                }));
            }
        }
    }

    onChange = (param) => (e) => {
        let vote;
        vote =  Math.round(1.0 + (this.state.seconds/10));
        var res = vote.toFixed(2);
        this.setState({
            disabled: this.props.round !== 3 ? true : false,
            previousVote: param.tag,
            previousScore: -vote,
        })
        if ( this.state.previousVote !== null){
            var value = this.state.previousScore.toFixed(2);
            //console.log("value " + value);
            this.sendMessage(this.state.previousVote, value);     
            this.sendMessage(param.tag, res);     
        }else{
            this.sendMessage(param.tag, res); 
        }
    }

    render() {
        const voteViews = [];
        let votes = this.state.votes;
        // isNAN appearing in optionS??
        let top;
        let msg = "Choose which tag is better..."
        if(this.props.round !== 3){
            votes.forEach((m, index) => {
                    voteViews.push(
                        <div className="div-votes" key={index}>
                            <div>
                                <label>
                                    <span className="title">{m.tag}</span>
                                    <input name="voteGroup" type="radio" onChange={this.onChange(m)} disabled={this.state.disabled}></input>
                                </label>
                            </div>
                        </div>)
            });
        }

        else{
            //CHECK NAN ----THIS IS NOT WORKING
           top = <h3 className="text-center">Top tag: {this.state.topTag}</h3>;
           this.state.votes.forEach((m, index) => {
                
                    voteViews.push(
                        <div className="div-votes" key={index}>
                            <div>
                                <label>
                                    <span className="title">{m.tag}</span>
                                    <input name="voteGroup" type="radio" onChange={this.onChange(m)} disabled={this.props.hasEnded}></input>
                                    <span className="vote">{m.vote}</span>
                                </label>
                            </div>
                        </div>)
                
            });

        }

        return (
            <div>
                {this.state.socket}
                {top}
                <Table>
                    <thead>
                        <tr>
                            <th><span className="glyphicon glyphicon-tag"></span>    {this.props.round !== 3 ? msg : null}</th>
                        {this.props.round !== 3 ? <th> You suggested so far:  {this.props.userSuggestedTags} </th> : null}
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <div className="div-votes">
                                    {voteViews}
                                </div>			
                            </td>
                        </tr>
                    </tbody>
            	</Table>
            </div>
        );
    }
}


export default withRouter(Vote);