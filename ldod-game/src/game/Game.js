import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Well } from 'react-bootstrap';
import VirtualEdition from './VirtualEdition';
import WebSockets from './WebSockets';
import { getVirtualEditionIndex } from '../utils/APIUtils';import { notification } from 'antd';
import LoadingIndicator  from '../common/LoadingIndicator';

class Game extends Component {
    constructor(props, context) {
        super(props,  context);
        this.state = {
            virtualEdition: [],
        };

        this.loadVirtualEdition = this.loadVirtualEdition.bind(this);
    }

    componentDidMount(){
       this.loadVirtualEdition();
    }
    
    loadVirtualEdition(){
        this.setState({
            isLoading: true,
        })

        let request = getVirtualEditionIndex("LdoD-ok");

        request.then(response =>{
            this.setState({
                virtualEdition: response,
                isLoading: false
            })
        })
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }
        return ( 
            <div>
                
                <Well>
                    <VirtualEdition virtualEdition={this.state.virtualEdition}/>
                </Well>
            </div>
    );
  }
}

export default withRouter(Game);