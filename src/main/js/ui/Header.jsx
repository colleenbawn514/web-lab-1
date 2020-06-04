import React, {useEffect, useState, Fragment } from 'react';
import {
    Button,
    AppBar,
    Toolbar,
    Typography,
    Divider,
    Link as Clink,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import {Link, useLocation } from "react-router-dom";
import api from "../util/api";
import { AccountCircle, Album } from "@material-ui/icons";



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
    icon: {
        marginRight: 8,
        marginLeft: 16,
    },
    divider: {
        marginLeft: 16,
        marginRight: 16,
    },
    name: {
        textTransform: "capitalize",
    }
}));

function Header() {
    const classes = useStyles();
    const location = useLocation();
    console.log(location);
    const isAuth = localStorage.getItem("token");
    const [user, setUser] = useState();
    const [isDownload, setDownload] = useState();
    const handleLogout = ()=>{
        fetch(`http://localhost:8080/logout?token=${localStorage.getItem("token")}`, {
            method: 'POST'
        })
            .then((data) => {
                localStorage.removeItem("token");
                localStorage.removeItem("userId");
                window.location.href="http://localhost:8080/auth";
            });
    }

    useEffect(()=>{
        if(!isAuth) return;
        setDownload(true);
        api(
            `/api/users/get?id=${localStorage.getItem("userId")}`,
            'GET',
            (data) => {
                setUser(data);
                setDownload(false);
            },
            (data) => {
                console.error(data);
            }
        );
    }, []);

    return (
        <AppBar position="static" elevation={0} color="default">
            <Toolbar>
                <Clink variant="h6" className={classes.title} color="primary" to="/home" component={Link}>
                    Letdown
                </Clink>
                {isAuth && location.pathname!=='/playlists' && (
                    <Button
                        color="inherit"
                        to="/playlists"
                        component={Link}
                        startIcon={<Album/>}
                    >
                        Моя музыка
                    </Button>
                )}
                {user && user.name && (
                   <Fragment>
                       <AccountCircle className={classes.icon}/>
                       <Typography variant="h6" className={classes.name} >
                           {user.name}
                       </Typography>
                       <Divider orientation="vertical" flexItem className={classes.divider}/>
                   </Fragment>
                )}
                {isDownload && (
                    <Fragment>
                        <Typography variant="h6" className={classes.name} >
                            Загрузка...
                        </Typography>
                        <Divider orientation="vertical" flexItem className={classes.divider}/>
                    </Fragment>
                )}

                {!isAuth && location.pathname !== '/auth' && (
                    <Button color="inherit" to="/auth" component={Link}>Войти</Button>
                )}
                {!isAuth && location.pathname !== '/registration' && (
                    <Button color="inherit" to="/registration" component={Link}>Регистрация</Button>
                )}
                {isAuth && (
                    <Button color="inherit" onClick={handleLogout}>Выйти</Button>
                )}
            </Toolbar>
        </AppBar>
    );
}

export default Header;