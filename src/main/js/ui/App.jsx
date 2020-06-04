import React from 'react';
import {
    CssBaseline,
    ThemeProvider
} from '@material-ui/core';
import theme from '../theme/theme';
import Workspace from "./Workspace";
import { Router } from "react-router-dom"
import {createBrowserHistory} from 'history'


function App() {
    const history = createBrowserHistory()

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Router history={history}>
                <Workspace />
            </Router>
        </ThemeProvider>
    );
}

export default App;