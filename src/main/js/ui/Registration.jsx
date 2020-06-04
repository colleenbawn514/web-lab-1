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

function Registration() {
    const classes = useStyles();
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [passConfirm, setPassConfirm] = useState("");
    const [nameUser, setNameUser] = useState("");
    const [error, setError] = useState(false);

    const handleSubmit = (event)=>{
        event.preventDefault();
        console.log(login, password);
        fetch(`http://localhost:8080/api/users/create?login=${login}&password=${password}&name=${nameUser}`, {
            method: 'POST'
        })
            .then((data) => {
                if(!data.ok){

                    data.json().then((data)=>{
                        const computeError = {};
                        data.forEach((error)=>{
                            computeError[error.name]=(computeError[error.name] || "") + error.message + "; ";
                        })
                        console.log(computeError);
                        setError(computeError);
                    })
                } else {
                    data.json().then((data)=>{
                        localStorage.setItem("userId", data.userId);
                        location.href="http://localhost:8080/auth";
                    })
                }
            });
    }

    return (
        <Box className={classes.root} >
            <Card className={classes.card} variant="outlined">
                <form onSubmit={handleSubmit}>
                    <CardHeader title="Регистрация"/>
                    <CardContent>
                        <TextField
                            name="login"
                            label="Логин"
                            error={error && error.login}
                            helperText={error && error.login}
                            variant="outlined"
                            className={classes.input}
                            fullWidth
                            onChange={(event)=>{
                                setError(false);
                                setLogin(event.target.value);
                            }}
                        />
                        <TextField
                            type="password"
                            name="password"
                            label="Пароль"
                            helperText={(error && error.password || "")+ (password!==passConfirm && "Пароли не совпадают" || "")}
                            error={error && error.password || password!==passConfirm}
                            variant="outlined"
                            className={classes.input}
                            fullWidth
                            onChange={(event)=>{
                                setError(false);
                                setPassword(event.target.value);
                            }}
                        />
                        <TextField
                            type="password"
                            name="passConfirm"
                            label="Подтверждение пароля"
                            helperText={password!==passConfirm && "Пароли не совпадают"}
                            error={error && error.password || password!==passConfirm}
                            variant="outlined"
                            className={classes.input}
                            fullWidth
                            onChange={(event)=>{
                                setError(false);
                                setPassConfirm(event.target.value);
                            }}
                        />
                        <TextField
                            name="name"
                            label="Имя"
                            error={error && error.name}
                            helperText={error && error.name}
                            variant="outlined"
                            className={classes.input}
                            fullWidth
                            onChange={(event)=>{
                                setError(false);
                                setNameUser(event.target.value);
                            }}
                        />
                    </CardContent>
                    <CardActions>
                        <Button
                            type="submit"
                            disabled={
                                login.trim()===""
                                || password.trim()===""
                                || nameUser.trim()===""
                                || password!==passConfirm
                            }
                        >
                            Зарегестрироваться
                        </Button>
                    </CardActions>
                </form>
            </Card>
        </Box>
    );
}

export default Registration;