import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import Paragraph from './Paragraph';
import Tag from './Tag';
import Vote from './Vote';
import './Fragment.css';
import { Alert } from 'react-bootstrap';
var ReactCountdownClock = require("react-countdown-clock")
class Fragment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            heteronyms: [],
            dates: [],
            hasLdoDLabel: false,
            number: 0,
            urlId: "",
            text: "",
            splitText: [],
            index: 0,
            seconds: 5,
            round: 1,
            tags: [],
        };
        this.paragraphSplit = this.paragraphSplit.bind(this);
        this.nextParagraph = this.nextParagraph.bind(this);
        
    }
    
    componentDidMount() {
        this.setState({
            title: this.props.fragment.title,
            number: this.props.fragment.number,
            urlId: this.props.fragment.urlId,
            text: this.props.fragment.text,
            splitText: this.paragraphSplit(this.props.fragment.text),
        });

    }
    paragraphSplit(text){
        var res = [];
        var paragraph = text.split("<br></p>");
        if((paragraph[0].indexOf("<u>") > -1) ){
            paragraph.splice(0,1);
        }
        for (var i = 0; i < paragraph.length; i++) {
            if (/\S/.test(paragraph[i])) {
                res.push(paragraph[i]);
            }
        }
        return res;
    }

    nextParagraph(){
        if(this.state.index < this.state.splitText.length - 1){
            this.setState((prevState, props) => ({
                index: prevState.index + 1,
                seconds: prevState.seconds + 0.0000001,
            }));
        }
        else if( this.state.index === this.state.splitText.length-1){
            this.setState((prevState, props) => ({
                index: 0,
                seconds: prevState.seconds - 0.0000001,
                round: prevState.round + 1,
            }));
        }
    }
    
    handleMessageTag(message) {
        var dictionary = this.state.tags;
        let copy = [...this.state.tags];
        var repeated = false;
        var temp = { fragmentUrlId: message[0], authorId: message[1], tag: message[2], vote: message[3]};
        for(var i in dictionary){
            if(dictionary[i].tag === temp.tag){
                copy.splice(i, 1, temp);
                this.setState(({
                    tags: copy,
                }));
                repeated = true;
            }
        }
        if(!repeated){
            this.setState(({
                tags: [...this.state.tags, temp]
            }));
        }
    }


    render() {
        let roundRender;
        if (this.state.round === 2) {
            roundRender = 
            <div>
                <span className="text-r2">Round 2:</span>
                <div className="clock">
                    <ReactCountdownClock seconds={this.state.seconds}
                    color="#8e44ad"
                    size={90}
                    onComplete={this.nextParagraph}
                    />
                </div>
                <Paragraph text={this.state.splitText[this.state.index]} title={this.state.title}/>    
                <Vote id={this.state.urlId} initialTags={this.state.tags}/>
            </div>
          } else {
            roundRender =
            <div>
                <span className="text-r1">Round 1:</span>
                <div className="clock">
                    <ReactCountdownClock seconds={this.state.seconds}
                    color="#2ecc71"
                    size={90}
                    onComplete={this.nextParagraph}
                    />
                </div>
                <Paragraph text={this.state.splitText[this.state.index]} title={this.state.title}/>
                <Tag id={this.state.urlId} handleMessageTag={this.handleMessageTag.bind(this)}/>
            </div>
        }

        if( this.state.round === 3){
            setTimeout(()=>this.props.nextFragment(), 5000);
            return(
                <div>
                    <Alert bsStyle="info">
                        <strong>Next fragment will start in 5 seconds</strong>
                    </Alert>
                </div>
            );
        }
        return (
            <div>
                {roundRender}
            </div>
        );
    }
}


export default withRouter(Fragment);