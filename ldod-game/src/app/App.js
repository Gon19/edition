/* React imports */
import React, { Component } from 'react';
import { Route, withRouter, Link, Switch } from 'react-router-dom';
import { Layout, notification } from 'antd';


/* Game imports */
import './App.css';
import { getCurrentUser, loadServerResources } from '../utils/APIUtils';
import { ACCESS_TOKEN, LDOD_MESSAGE } from '../utils/Constants';
import Login from '../user/login/Login';
import Profile from '../user/profile/Profile';
import AppHeader from '../common/AppHeader';
import Game from '../game/Game';
import GameLeadeboard from '../game/GameLeaderboard';
import NotFound from '../common/NotFound';
import PrivateRoute from '../common/PrivateRoute';
import FacebookLogin from '../social/FacebookLogin';
import LinkedinLogin from '../social/LinkedinLogin';
import GoogleLogin from '../social/GoogleLogin';
import TwitterLogin from '../social/TwitterLogin';
import { Jumbotron, Button } from 'react-bootstrap'; 

const { Content } = Layout;

class App extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false,
        }

        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);

        notification.config({
            placement: 'topRight',
            top: 80,
            duration: 2,
        });
    }
    
    loadCurrentUser() {
        this.setState({
            isLoading: true
        });
        getCurrentUser()
            .then(response => {
                localStorage.setItem("currentUser", response.username);
                this.setState({
                    currentUser: response,
                    isAuthenticated: true,
                    isLoading: false
                });
            }).catch(error => {
                this.setState({
                    isLoading: false
                });
            });
            localStorage.setItem("currentFragment",0);
    }

    componentWillUnmount() {
        localStorage.clear();
    }

    handleLogout(redirectTo="/", notificationType="success", description="You're successfully logged out.") {
        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.clear();
        
        this.setState({
            currentUser: null,
            isAuthenticated: false
        });
        
        this.props.history.push(redirectTo);
        
        notification[notificationType]({
            message: LDOD_MESSAGE,
            description: description,
        });
    }
    
    async handleLogin() {
        notification.success({
            message: LDOD_MESSAGE,
            description: "You're successfully logged in.",
        });
        await loadServerResources(localStorage.getItem("currentUser"),"LdoD-ok");
        this.loadCurrentUser();
        this.props.history.push("/");
        return 
    }
    
    render() {
        var styles ={
            fontFamily : 'Ubuntu',
            backgroundColor: '#3498db'
        }
    return (
        <Layout className="app-container">
            <AppHeader isAuthenticated={this.state.isAuthenticated}
            currentUser={this.state.currentUser}
            onLogout={this.handleLogout} />
            <Content className="app-content">
                <div className="container">
                    <Switch>
                        <Route exact path="/" render={() =>
                        <div>
                            <section className="intro">
                                <Jumbotron style={styles}>
                                    <h3 className="text-center">Welcome to the LdoD Game powered by LdoD Archive</h3>
                                </Jumbotron>
                                <div className="col-lg-6 col-sm-12 left">
                                    <Link to="/game">
                                        <Button bsStyle="primary">Classic game mode</Button>
                                    </Link>
                                </div>
                                <div className="col-lg-6 col-sm-12 right">
                                    <Link to="/todo">
                                        <Button bsStyle="primary">Custom game mode</Button>
                                    </Link>
                                </div>
                            </section>
                        </div>
                        }>
                        </Route>
                        <Route path="/login" render={(props) =>
                        <div>
                            <Login onLogin={this.handleLogin} {...props} />
                            SOCIAL NOT WORKING
                            <TwitterLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                            <GoogleLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                            <FacebookLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                            <LinkedinLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                        </div>}>
                        </Route>
                        <PrivateRoute path="/user/:username" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={Profile}>
                        </PrivateRoute>
                        <PrivateRoute path="/game" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={Game}>
                        </PrivateRoute>
                        <PrivateRoute path="/leaderboard" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={GameLeadeboard}>
                        </PrivateRoute>
                        <Route component={NotFound}></Route>
                    </Switch>
                </div>
             </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
