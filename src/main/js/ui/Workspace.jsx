import React from 'react';
import {
    Box,
    Container,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import Header from "./Header";
import Login from "./Login";
import {
    Route,
    Switch,
    Redirect,
} from "react-router-dom"
import Home from "./Home";
import Registration from "./Registration";
import Playlists from "./Playlists";
import Tracks from "./Tracks";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
    container: {
        backgroundColor: theme.palette.background.paper,
        minHeight: "100vh",
        display: 'flex',
        flexDirection: 'column',
    },
    pages: {
        flexGrow: 1,
        width: '100%',
        display: 'flex',
    }
}));

function Workspace({history}) {
    const classes = useStyles();

    return (
        <Container maxWidth="lg" className={classes.container} disableGutters>
            <Header />
            <Box className={classes.pages}>
                <Switch>
                    <Route history={history} path='/auth' component={Login} />
                    <Route history={history} path='/home' component={Home} />
                    <Route history={history} path='/registration' component={Registration} />
                    <Route history={history} path='/playlists' component={Playlists} />
                    <Route history={history} path='/playlist' component={Tracks} />
                    <Redirect from='/' to='/home'/>
                </Switch>
            </Box>
        </Container>
    );
}

export default Workspace;