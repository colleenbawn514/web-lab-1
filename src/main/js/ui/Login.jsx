import React, {useState} from 'react';
import {
    Box,
    Card,
    CardContent,
    CardActions,
    CardHeader,
    Button,
    TextField,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        width: '100%',
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
    input: {
        marginBottom: 16,
    }
}));

function Login() {
    const classes = useStyles();
    const [login, setLogin] = useState();
    const [password, setPassword] = useState();
    const [error, setError] = useState(false);

    const handleSubmit = (event)=>{
        event.preventDefault();
        console.log(login, password);
        fetch(`http://localhost:8080/auth?login=${login}&password=${password}`, {
            method: 'POST'
        })
            .then((data) => {
                if(!data.ok){
                    setError(true);
                } else {
                    data.json().then((data)=>{
                        localStorage.setItem("token", data.token);
                        localStorage.setItem("userId", data.userId);
                        location.href="http://localhost:8080/home";
                    })
                }
            });
    }

    return (
        <Box className={classes.root} >
            <Card className={classes.card} variant="outlined">
                <form onSubmit={handleSubmit}>
                    <CardHeader title="Авторизация"/>
                    <CardContent>
                        <TextField
                            name="login"
                            label="Логин"
                            error={error}
                            fullWidth
                            variant="outlined"
                            className={classes.input}
                            onChange={(event)=>{
                                setError(false);
                                setLogin(event.target.value);
                            }}
                        />
                        <TextField
                            type="password"
                            name="password"
                            label="Пароль"
                            error={error}
                            variant="outlined"
                            className={classes.input}
                            fullWidth
                            onChange={(event)=>{
                                setError(false);
                                setPassword(event.target.value);
                            }}
                        />
                    </CardContent>
                    <CardActions>
                        <Button type="submit">Войти</Button>
                    </CardActions>
                </form>
            </Card>
        </Box>
    );
}

export default Login;